# Process Monitoring Commands

# ps

---

현재 실행 중인 프로세스의 정보를 얻어온다. 프로세스의 CPU, 메모리 점유 등을 확인할 수 있다.

```jsx
ps [option]
```

의 형태로 사용한다. 

```jsx
ps -e # 모든 프로세스
ps -f # 자세한 정보를 전부 출력

# BSD 계열
ps a # 전체 사용자의 프로세스 출력
ps u # 프로세스를 실행한 사용자 정보와 프로세스 시작 시간 출력
ps x # 사용자와 연관된 프로세스를 모두 출력, a 옵션은 전체 사용자를 전부 뜻하므로 함께 사용하면 모든 프로세스를 출력
```

대표적으로 위 옵션을 많이 사용한다. BSD는 버클리 대학에서 개발한 유닉스의 파생판을 의미한다. 대표적으로 Apple의 Mac OS가 이에 속한다. 자세한 프로세스 정보를 보기 위해 `ps -ef`  BSD에선 `ps aux` 의 형태로 사용하며, `grep` 명령어와 파이프라인을 통해 필요한 정보만을 찾아낸다.

컬럼의 의미는 아래와 같다.

```jsx
PID: 프로세스 ID
USER: 프로세스를 실행한 리눅스 유저
CPU: CPU 점유율
MEM: 메모리 점유율
VSZ: Kib 단위의 버추얼 메모리 크기(vsize)
RSS:프로세스가 사용하는 메모리의 크기
STAT: 프로세스의 상태(R, S, D, T, Z W, N) 러닝, 슬립, 페이징, 데드 등의 상태가 있음
COMMAND: 사용자가 실행한 명령어
```

# top

---

top은 현재 시스템, OS의 상태를 알려준다. ps와 다른점은 top은 계속 업데이트되어 실시간에 근접한 내용을 보여준다. 또 top 명령어 결과의 최상단에 모든 프로세스 갯수, 실행중인 프로세스, 슬립 상태인 프로세스, 스레드 개수, 메모리 등 전체적인 상태를 총 정리해서 보여준다.

즉 실시간으로 현재 상태를 계속 확인하고 싶을 때 사용한다.

상단에 Load Avg라는 섹션이 있는데, 이곳은 평균 실행/대기 중인 프로세스 의 수를 나타낸다. 처음에 3개의 숫자가 있는데 각각 1분, 5분, 15분에 대한 평균 개수이다. 이 개수가 CPU 코어수 보다 작아야한다.

전체적인 메모리 상황도 알려주는데 total은 전체 메모리양, used는 사용된 메모리, free는 남은 메모리 buff/cache는 버퍼, 캐시 메모리이다.

cpu정보도 알려주는데. us는 현재 유저 공간에서 사용중인 cpu, sys는 system, id는 유휴상태를 의미한다. 

또 아래와 같은 옵션이 있다.

```jsx
top -u [user] # 특정 유저의 프로세스만 보여줌
top -n [number] # 실행주기 설정
top -p [pid] # 특정 프로세스 아이디만 표시
```

또 내부에서 특정 명령을 더 실행할 수 있다.

```jsx
u: 특정 유저의 프로세스만 표시(이 후에 유저네임 입력)
k: 특정 프로세스 종료(이 후에 pid입력)
f: 필드별로 정렬
a: 메로리 사용량에 따른 정렬
shift + p: cpu 사용률 내림차순
shift + m: 메모리 사용률 내림차순
shift + t: 프로세스가 실행된 시간 순
```

# htop

---

# lsof

---

lsof는 list open files의 약자로 열려 있는 파일의 목록을 알려준다. 각 열의 의미는 아래와 같다.

```jsx
COMMAND: 실행한 명령어
PID: 프로세스 id
USER: 사용자
FD: 파일 설명
	- cwd: current working directory
	- rtd: root directory
	- mem: memory mapped file
	- text: program te4xt
	- lnn: Libary reference
	- m86: dos merge mapped file
	- pd: parent directory
	- [number]u: 숫자는 FD의 번호이고 r은 읽기, w는 쓰기, u는 둘 읽기/쓰기
TYPE: 파일 종류
	- DIR: 디렉토리
	- CHR: character special file
	- REG: regular file
	- UNIX: 유닉스 도메인 소켓
	- BLK: Block Special File
	- IPv4, IPv6: 각 IP 소켓
	- inet: internet domain socket
	- NODE: 로컬파일의 노드 번호
DEVICE: 장치 번호
SIZE/OFF: 파일크기
```

옵션도 제공한다

```jsx
lsof -u [username] # 특정유저의 파일만 출력
lsof +D [directory path] # 특정 디렉토리 밑의 파일만 표시
lsof -i TCP:22 # 22번포트 TCP 명령만 확인
lsof -c httpd # httpd가 오픈한 파일만 출력
lsof -i TCP:22-80 # 22번에서 80번 포트 모두 확인
lsof -i TCP # TCP 만 표시
```
