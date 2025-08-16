# Vert.X 부하테스트


# 개요
- 새로 배포되는 Vert.X 기반의 앱의 부하를 테스트한다.
- 서버 성능을 튜닝하여 TPS를 최대한 끌어올린다.
- 환경: EC2 t4g.medium, c6g.large, 200ms~800ms 딜레이를 갖는 Mock API 서버에 Network I/O 로직 포함

# 최초 시도
- grafana k6를 통해 테스트

## K6 Request Script
```javascript
import http from 'k6/http';
import { check } from 'k6';

export const options = {
  scenarios: {
    high_tps: {
      executor: 'constant-arrival-rate',
      rate: 30000,
      timeUnit: '1s',
      duration: '30s',
      preAllocatedVUs: 1000,
      maxVUs: 10000,
    },
  },
  discardResponseBodies: true,
};

export default function () {
  const res = http.get(
    'http://app/api/ads?param=param'
  );
  check(res, {
    'is status 200': (r) => r.status === 200,
  });
}
```
- 1초에 30,000번 요청, 가상 유저수는 1000에서 시작하여 10000명까지 증가
- t4g.medium, verticle 개수 2대(t4g.medium의 코어수와 동일하게 설정)

## 결과
```sql
█ TOTAL RESULTS checks_total.......................: 94841 2875.978176/s checks_succeeded...................: 100.00% 94841 out of 94841 checks_failed......................: 0.00% 0 out of 94841 ✓ is status 200 HTTP http_req_duration.......................................................: avg=3.24s min=493.72ms med=2.59s max=9.05s p(90)=6.03s p(95)=7.42s { expected_response:true }............................................: avg=3.24s min=493.72ms med=2.59s max=9.05s p(90)=6.03s p(95)=7.42s http_req_failed.........................................................: 0.00% 0 out of 94841 http_reqs...............................................................: 94841 2875.978176/s EXECUTION dropped_iterations......................................................: 805168 24416.081607/s iteration_duration......................................................: avg=3.25s min=514.6ms med=2.59s max=10.09s p(90)=6.03s p(95)=7.5s iterations..............................................................: 94841 2875.978176/s vus.....................................................................: 0 min=0 max=10000 vus_max.................................................................: 10000 min=6615 max=10000 NETWORK data_received...........................................................: 53 MB 1.6 MB/s data_sent...............................................................: 24 MB 733 kB/s
```
-> TPS 3000


# 추가 튜닝 후 재시도
### Verticle HttpServerOptions 수정
```kotlin
val options = HttpServerOptions()
options
    .setCompressionSupported(true) // http 응답 압축
    .setCompressionLevel(1) // 압축레벨 최저(속도 우선)
    .setAcceptBacklog(8192) // 대기 연결 큐 크기
    .setHandle100ContinueAutomatically(true) // HTTP 100-Continue 요청 자동처리
    .setTcpFastOpen(true) // 3 way handshake 없이 데이터 전송 시작
    .setTcpNoDelay(true) // Nagle 알고리즘 비활성화, 작은 패킷도 바로 전송
    .setTcpQuickAck(true) // ACK 패킷 빠르게 전송
    .setReuseAddress(true) // 소켓 주소 재사용 허용
    .setIdleTimeout(30000)
```
- 외부에서 Vert.X로 들어오는 수신 요청을 처리하는 HTTP 서버 설정

### WebClient 서버 설정
```kotlin
@Single
fun webClient(
    vertx: Vertx,
    @Property("vertx.web.max-pool-size") maxPoolSize: Int,
    @Property("vertx.web.http2-max-pool-size") http2MaxPoolSize: Int,
): WebClient = WebClient.create(
    vertx,
    WebClientOptions()
        .setUserAgent("Anypoint-SSP/1.0.0")
        .setMaxPoolSize(maxPoolSize)
        .setHttp2MaxPoolSize(http2MaxPoolSize)
)
```
- Vert.X 웹에서 외부 API 요청하는 WebClient 서버 설정
- maxPoolSize, http2MaxPoolsize를 각각 500으로 증가

### Koin @Single -> @Factory 설정
```kotlin
@Factory
fun webClient(
    vertx: Vertx,
    @Property("vertx.web.max-pool-size") maxPoolSize: Int,
    @Property("vertx.web.http2-max-pool-size") http2MaxPoolSize: Int,
```
- 앱에서 사용되는 Koin 객체의 타입을 변경
- 앱 전체에서 모든 버티클이 하나의 WebClient를 공유하는 것을 방지

### EventLoop 수 증가
```kotlin
val coresCount = Runtime.getRuntime().availableProcessors()

val vertxOptions = VertxOptions()
        .setEventLoopPoolSize(coresCount)
```
- 디폴트로 Vert.X는 코어수 2배만큼 이벤트 루프를 할당한다. 이 수를 낮춰준다.

#### 이벤트 루프
- 이벤트 루프는 CPU 스레드와 물리적으로 관계가 있으며 작업을 처리한다.
- 이벤트 루프 수가 코어 수보다 많으면, 들어온 요청들을 이벤트 루프가 하나씩 가져가서 처리하지만 그 내부적으로는 코어가 처리하므로 물리적인 코어가 결국 컨텍스트 스위칭을 해야한다.
- 이벤트 루프가 코어 수보다 적으면 하드웨어 자원을 다 사용하지 않음.

### Verticle 수 증가
```kotlin
val verticlesCount = coresCount * 2
vertx.deployVerticle(
    { MainVerticle(koin = koin) },
    DeploymentOptions().setInstances(verticlesCount)
```
- 버티클의 개수를 코어 개수의 2배로 늘린다.

#### 버티클
- 버티클은 이벤트루프가 처리할 작업 내용을 담고있다.
- vert.x가 적절히 버티클을 이벤트 루프에 배치한다.
- 코어 수의 두 배로 버티클을 지정하면 한 버티클의 작업 내용을 이벤트루프가 처리하는 동안 다른 버티클이 다음 작업을 준비할 수 있다.

### NativeTranport 설정 추가
```kotlin
val vertxOptions = VertxOptions()
    .setPreferNativeTransport(true)
```
- Vert.X가 네트워크 요청을 처리할 때 디폴트로 설정된 Java NIO 방식 대신 운영체제가 제공하는 Native 방식을 사용하도록 설정.
- Linux의 경우 Epoll, MacOS의 경우 KQueue라는 기술 사용
- NIO를 사용하면 안정적이지만 해석 단계를 한 단계 더 거치므로 미세한 지연이 발생하며 OS 고급기능은 사용하기 어렵다.

### Kotlin 라이브러리 설정
```kotlin
implementation(variantOf(libs.netty.transport.native.epoll) { classifier("linux-aarch_64") })
```
- Vert.X에서 Native 통신을 위해 라이브러리 세팅
- 배포되는 EC2는 그라비톤이므로 ARM 아키텍쳐 용으로 설정해주었다.

### 서버 커널 파라미터 튜닝
```bash
sudo sysctl -w fs.file-max=110000 # 스템에서 가질 수 있는 파일 디스크립터 개수
sudo sysctl -w fs.nr_open=110000 # 시스템에서 가질 수 있는 파일 디스크립터 개수
sudo ulimit -n 110000 # 한 프로세스가 가질 수 있는 파일 디스크립터 개수

# Increase tcp buffers
sudo sysctl -w net.ipv4.tcp_mem="100000000 100000000 100000000"

# Increase socket connection limit
sudo sysctl -w net.core.somaxconn=10000

# Increase backlog size for tcp connections in SYN state
sudo sysctl -w net.ipv4.tcp_max_syn_backlog=10000 # 소켓 연결 대기열
```
- Native 방식의 전송을 사용하기 위하여 서버 커널 파라미터를 수정해주어야 한다. 
- EC2에서 위 커맨드를 실행해준다.
- 파일 디스크립터의 개수를 늘린다.
- 시스템에서 사용할 수 있는 TCP 메모리 총량을 늘린다.
- 네트워크 소켓 연결 대기열 개수를 늘린다.

#### 파일 디스크립터
- 리눅스에서는 모든 네트워크 연결을 파일로 취급한다. 파일 디스크립터는 서버가 열어둔 각 파일/소켓에 붙이는 번호이다.

## EC2 스펙 변경
- t4g.medium(2코어) -> c6g.large(4코어)

## wrk로 부하 테스트 툴 변경
```
wrk -t8 -c10000 -d30s http://localhost:8080/static/4k](http://app/api/ads?param=param
```
- 스레드 수 8개, 커넥션 10000개, 30초 간 진행

```bash
 // 결과 1
  8 threads and 20000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.42s   461.30ms   2.00s    71.67%
    Req/Sec     1.88k     2.43k   15.36k    86.69%
  261017 requests in 30.05s, 140.14MB read
  Socket errors: connect 0, read 0, write 0, timeout 131117
Requests/sec:   8686.65
Transfer/sec:      4.66MB


// 결과 2
  8 threads and 20000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.46s   469.90ms   2.00s    80.91%
    Req/Sec     2.00k     2.54k   17.01k    87.20%
  271712 requests in 30.10s, 145.89MB read
  Socket errors: connect 0, read 0, write 0, timeout 100435
Requests/sec:   9026.45
Transfer/sec:      4.85MB
```
-> TPS 9000으로 증가
-> htop으로 EC2 확인한 결과 코어를 골고루 사용함, 메모리는 3g 정도사용

# 결론
- Vert.X 서버를 튜닝하여 최초 시도보다 TPS를 3배 증대시킴.
- WebClient 요청을 제외하면 TPS가 10만, 11만까지 올라가지만 I/O 요청을 포함해서 TPS에 한계가 있음