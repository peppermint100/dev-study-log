# Linux Network Commands

## nslookup

---

DNS 서버에 질의하는 명령어로 도메인의 정보를 조회한다.

```bash
nslookup [도메인] # 도메인의 ip 정보 출력
nslookup [ip] # ip의 네임서버 정보 출력
nslookup [네임서버] # 네임서버의 ip 정보 출력
nslookup -type=[타입] [도메인] # 도메인의 타입별 레코드 조회
```

타입에는 MX, TXT 등이 있다. MX는 메일 전송에 사용되는 정보를 의미하고 TXT는 텍스트 문자열을 저장한다. 일반적으로 SPF(Sender Policy Framework) 레코드를 통해 메일 보안, 인증에 사용된다.

DNS 타입은 [https://en.wikipedia.org/wiki/List_of_DNS_record_types](https://en.wikipedia.org/wiki/List_of_DNS_record_types) 여기에서 더 확인할 수 있다.

## nmap

---

namp은 네트워크 스캐너로. 대상 네트워크에 패킷을 보내고 응답을 통해 네트워크를 스캐닝하는 명령어 이다. 설치해서 사용해야 한다.

```bash
yum install nmap
```

일반적으로 `nmap [옵션] [타겟 도메인 혹은 ip]` 의 형태로 사용한다.

```bash
nmap -sP google.com # ping 을 통해 스캔한다.
nmap -sP -PT80 google.com # 80번 포트를 스캔한다.
nmap -sS google.com # Stealth Scan으로 탐색하며 RST 패킷을 사용한다. 추적이 어렵다
nmap -sU google.com # udp 포트를 스캔한다.
nmap -O # 운영체제를 알아볼 수 있다.
nmap -v # 자세한 정보를 보여준다 verbose 옵션
nmap -PI # ICMP 핑을 사용한다.
namp -PB # ICMP와 TCP 핑을 동시에 이용
nmap -o # 스캔 결과를 파일로 저장
```

- RST 패킷: 리셋 패킥으로 회선 양끝에 동시에 강제 리셋 신호를 보내 세션을 끊는다.
- ICMP (Internet Control Messgae Protocol): IP는 패킷을 목적지에 도달 시키기만 하는데 ICMP 프로토콜을 사용하면 에러 발생시 IP의 헤더에 호스트 정보를 읽고 목적지에 대한 정보를 돌려줄 수 있다.

## tcpdump

---

tcpdump는 리눅스, 유닉스 계열의 운영체제에서 네트워크 인터페이스를 통해 송수신되는 패킷의 정보를 출력해준다.

```bash
tcpdump -i [숫자] # 숫자만큼 패킷 출력
tcpdump -i [네트워크 인터페이스 이름(interface)] # 해당 네트워크 인터페이스를 거치는 패킷만 출력
tcpdump -w [파일이름] # 파일에 tcpdump 출력결과 저장
tcpdump -v # 패킷 내용을 자세히 출력합니다. verbose
```

tcpdump를 그냥 사용하면 모든 패킷을 다 잡고 -i로 인터페이스 제한을 주더라도 많은 통신을 하는 인터페이스는 내용이 많을 수 있다. tcpdump는 이를 필터하기 위해 범위 값 기능을 제공한다.

```bash
tcpdump src [srcip] # host의 src 필터링
tcpdump dst [dstip] # host의 목적지 ip로 필터링
tcpdump net [cidr] # cidr 블록으로 필터링
tcpdump tcp # tcp만 필터링, udp 도 가능
tcpdump port [port number] # 포트번호로 필터링
```

위 범위값들은 `and, or, not` 을 통해 연산하여 표시할 수 있다.

## telent

---

telent은 목적 ip의 포트가 접속 가능한지 확인합니다.

```bash
telent [목적지 ip] [포트] # 포트 디폴트 값 23
```

telent은 설치해야 사용 가능하며 ssh, ftp에 비해서 보안성이 좋지 않기 때문에 급한 상황에서만 사용한다.

## telnet과 ping의 차이

---

ping은 도메인으로 ICMP 패킷을 보내고 다시 돌려받는다. 즉 IP에서 부터 응답을 받을 수 있는지 얼마나 걸리는지를 체크한다. 

telent은 원격 로그인을 하는 것으로 서버 접속은 되지 않으나 ping 테스트는 통과한 경우 해당 어플리케이션이 종료되었는지 확인할 때 사용한다. 따라서 포트를 지정하게 된다.

```bash
ping => 목적지 서버를 통하는 네트워크 체크
telnet => 목적지 서버의 어플리케이션까지 확인
traceroute => 출발지와 목적지 사이의 라우터를 모두 추적
```

## traceroute

---

traceroute는 패킷이 출발지에서 목적지까지 도달하는 경로를 표시한다. 기본적으로 최대 30개 까지 표시한다.

```bash
traceroute -d # 주소를 호스트 이름으로 확인하지 않는다. 다음 라우트로 진행이 빨라진다.
traceroute -h [number] # 기본 라우트를 30개에서 number개로 늘린다.
traceroute -w [timeout] # 응답 대기 시간을 제한한다.
traceroute -4 # ipv4 사용
traceroute -6 # ipv6 사용
```

## mtr

---

mtr은 ping과 traceroute의 기능을 통합한 프로그램이다. traceroute보다 더 자세한 결과를 보여준다.

```bash
mtr [도메인 혹은 ip]
```

사용하면 패킷이 이동하는 자세한 경로를 볼 수 있으며 각 컬럼의 의미는 아래와 같다.

```bash
Host: 목적지까지 가는 Gateway IP, 도메인
%Loss: 시스템의 패킷 손실 비율
Snt: 보낸 패킷 수
Last: 마지막 traceroute 패킷의 왕복 시간
Avg: 패킷 응답 평균 시간
Best: 패킷 응답 가장 빠른 시간
Wrst: 패킷 응답 가장 느린 시간
StDev: 표준 편차
```

```bash
mtr -n # 도메인 대신 ip 주소로 출력
mtr -b # 도메인, ip 함께 표시
mtr -c [number] # 핑 수 제한
mtr --tcp or --udp # 핑에서 icmp가 아닌 tcp 혹은 udp 사용
mtr -m # traceroute 에서처럼 홉 수 지정(기본 라우트)
mtr -i [number] # icmp 요청 간격 조정
```

## dig

---

dig은 DNS를 질의한다. nslookup 보다 편한 인터페이스와 사용법을 제공한다.

```bash
dig [질의] [레코드] [@네임서버] [옵션]
```

[질의]에 들어갈 수 있는 정보는 아래와 같다.

```bash
a : 도메인의 아이피 정보
any : 지정된 도메인의 모든 정보
mx : 지정한 도메인의 메일서버 정보
ns : 네임서버 정보
soa : soa 정보
```

[옵션]에 들어갈 수 있는 값은 아래와 같다.

```bash
+short # 간단히 출력
+dnssec # dnssec 서명 적용 확인 dnssec은 네임서버가 dns 데이터에 대한 디지털 서명이 유효, 무결한지 확인
+trace # dns 질의 과정을 모두 표시
-p [number] # 특정 포트만 확인
+multiline # 보기 좋게 출력
```

## netstat

---

netstat은 네트워크의 연결 상태정보, 라우팅 테이블 정보, 네트워크 인터페이스 상태 등의 통계 정보를 보여준다.

```bash
netstat [option] [address_family_option]
```

option에 들어갈 수 있는 값은 아래와 같다.

```bash
-a : 모든 소켓 정보 출력
-n : 호스트명이나 포트명 대신 숫자로 표시(예를 들어 www는 80으로 출력)
-p : 소켓에 대한 PID 및 프로그램명 출력
-r : 라우팅 정보 출력
-l : Listening(대기)하고 있는 포트 출력
-i : 네트워크 인터페이스 테이블 출력
-s : 네트워크 통계 정보 출력
-c : 네트워크 정보를 주기적으로 계속 출력
-t : TCP 기반 접속 목록 출력
-u : UDP 기반 접속 목록 출력
-g : 멀티캐스트 그룹 정보 출력
```

address_family_option에 들어갈 수 있는 값은 아래와 같다.

```bash
–protocol=프로토콜 이름 : inet, unix, ipx, ax25 등 특정 프로토콜 관련 정보 출력
–inet, –ip : IP 주소 기반 연결 정보 출력(--protocol=inet과 같은 결과)
–unix : Unix Domain Socket 정보 출력(--protocol=unix과 같은 결과)
```

컬럼 의미

```bash
- Proto : 프로토콜 종류. TCP / UDP / RAW
- Recv-Q : 해당 process가 현재 받는 바이트 표기
- Send-Q : 해당 process가 현재 보내는 바이트 표기
- Local Address : 출발지 주소 및 포트. 자신의 주소 및 포트
- Foreign Address : 목적지 주소 및 포트
- State : 포트의 상태 표기.
=> CLOSED
=> CLOSED_WAIT
=> CLOSING
=> ESTABLISHED : 연결된 상태
=> FIN_WAIT1
=> FIN_WAIT2
=> LAST_ACK: 연결 종료, 승인 대기
=> LISTEN :  대기 포트. 포트 open
=> SYN_RECV: 서버가 클라이언트의 SYN 패킷 요청 수신 후 클라이언트에게 ACK 전송 후 클라이언트의 ACK 수신 대기 상태
=> SYN_SENT: 클라이언트가 서버에 SYN 패킷 전송 후 연결 요청 상태
=> TIME_WAIT: 연결은 종료되었으나 소켓은 열려있는 상태
=> UNKNOWN
```

컬럼 중 type은 소켓 형식을 의미한다.

1. STREAM: 양방향으로 바이트 스트림을 전송할 수 있는 연결 지향형 소켓으로 양쪽 어플리케이션이 모두 데이터를 주고 받을 수 있다. TCP 프로토콜을 사용한다.
2. DGRAM: 데이터 그램 소켓으로 연결을 맺지 않는 비연결형 소켓읻.  UDP 프로토콜을 사용한다

## scp

---

scp는 ssh를 기반으로 하는 파일 전송 프로토콜이다. 

```jsx
scp [옵션] [파일명] [타겟 서버 id]@[타겟 서버 ip]:[파일 수신 디렉토리]
```

이런식으로 사용한다.  파일을 여러개 보내고 싶으면 띄어쓰기로 파일명을 구분해서 여러개 넣으면 된다.

옵션은 아래와 같다.

```jsx
scp -p # 원본 권한 유지
scp -P # 포트 번호 지정
scp -c # 압축
scp -v # 복사 과정 출력
scp -a # 아카이브 모드
scp -r # 하위 파일을 포함한 디렉터리 전송
```

원격에 있는 파일을 받고 싶으면 구문의 위치를 변경해주면 된다

```jsx
scp [옵션] [타겟 서버 id]@[타겟 서버 ip]:[파일] [로컬 받는 위치]
```

## 출처

---

[https://ciksiti.com/ko/chapters/8993-mtr-a-network-diagnostic-tool](https://ciksiti.com/ko/chapters/8993-mtr-a-network-diagnostic-tool)

[https://idchowto.com/mtr-traceroute-ping-사용법/](https://idchowto.com/mtr-traceroute-ping-%EC%82%AC%EC%9A%A9%EB%B2%95/)

[https://server-talk.tistory.com/126](https://server-talk.tistory.com/126)

[https://snowdeer.github.io/linux/2018/08/13/how-to-use-netstat/](https://snowdeer.github.io/linux/2018/08/13/how-to-use-netstat/)

[https://blog.skills.kro.kr/9](https://blog.skills.kro.kr/9)

[https://blog.voidmainvoid.net/201](https://blog.voidmainvoid.net/201)

[https://c0mp.tistory.com/562](https://c0mp.tistory.com/562)

[https://wlsvud84.tistory.com/11](https://wlsvud84.tistory.com/11)