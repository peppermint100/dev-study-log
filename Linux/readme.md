## chmod
r, 4 : 읽기 권한  
w, 2 : 쓰기 권한
x, 1 : 실행 권한

```
chmod 644 file.txt
```
=> 사용자에게 4+2 권한, 그룹에게 4권한, 그 외 모든 사람에게 4권한

```
chmod g+w file.txt
```
=> file.txt를 이용하는 group에게 w 권한 부여


## tar
tar는 파일들을 묶어주는 역할을 한다.  
- 옵션
1. cvf : c는 생성, v는 출력, f는 파일을 의미하며 파일을 묶어준다.
2. tvf : t는 테이블이란 의미로 tar로 묶인 파일 목록을 보여준다.
3. xvf : x는 추출의 의미로 tar 파일을 풀어준다.
```
tar wrapfile.tar *(filename)
```

tar는 파일을 원래 백업용으로 만들어져 파일들을 묶어줄 뿐 압축해주지는
않는다. 다만 한 번에 묶어서 압축하면 관리가 편하므로 많은 리눅스 파일 배포가
.tar.gZ 형태로 되어 있다.

## gzip
gzip은 파일을 압축하는데 사용 된다.
- 옵션
1. d : 압축을 푼다
2. r : 현재 디렉토리 부터 하위 디렉토리 까지 전부 압축한다.
3. 9 : 최대 압축률로 압축한다.

## ps
현재 시스템에서 실행 중인 모든 프로세스의 상태를 보여준다. 각 프로세스의 목록과 상태, 크기, 이름, 소유자, cpu 시간, 실제 시간등을 보여준다.  
거의 모든 경우에서 -aux 옵션을 사용한다.

## shell
사용자의 명령을 shell이 해석해서 kernel에 명령을 보내고 hardware가 동작한다.
사용자의 명령을 바로 kernel에 보내지 않는 이유는 여러가지 shell이 존재하고 사용자의 명령을 바로 커널에 보내면 위험할 수 있기 때문에 안정성, 신뢰성을 위해 shell이라는 레이어를 나눈다.

## curl
1. HTTP 
2. FTP(File transfer Protocol)
```
curl <API_ENDPOINT>
```

- 옵션
1. -i : 헤더 정보도 가져온다.
2. --head, -I : 헤더 정보만 가져온다.
3. -o : txt파일에 쓴다.
4. -O : 확장자 파일 없이 파일에 쓴다.
5. --limit-rate <number> : 파일 용량 제한.
6. -L : 리다이렉트된 url을 이용한다.
7. -T <FILENAME> : FILENAME이라는 이름으로 업로드
8. -O : 다운로드
9. -u  <USERID>:<PASSWORD>: 유저 정보 입력

## wget
```
wget <DOWNLOAD_LINK>
```
다운로드 링크에 의해 다운받아지는 파일을 다운로드
- 옵션  
1. -o <FILENAME> : 파일 이름을 설정하여 다운로드(기본적으로 download라는 이름으로 다운됌)

## find
```
sudo find / -name nginx.conf
```
/ 라는 디렉토리(루트) 부터 nginx.conf라는 이름을 가진 파일을 찾아서 위치를 리턴한다.
  

## history
이전까지 실행했던 명령어를 보여준다.


## !
!! : 이전에 실행했던 명령어를 실행한다.
!<character> : <character>로 시작하는 이전 명령어를 실행한다.

## ll
ls -al

## .vimrc
vim 설정
```
set smartindent
set tabstop=4
set expandtab
set shiftwidth=4
```
등의 vim 설정가능

## .bashrc

.bashrc 파일 내에
```
alias s1='~/s1.sh`
```
이렇게 alias 명령을 주고

`. .bashrc` 명령어를 통해 최신화를 시켜준다음 `s1` 명령어를 이용할 수 있다. 파일을 살펴보면 `ll`은 `ls -la`로 aliasing이 되어 있는 것을 확인할 수 있다.

## Date

```
date "+%Y-%m-%d %H:%M:%S"
```
로 date를 포맷팅할 수 있다.

```
date "+%Y-%m-%d %H:%M:%S" --date=yesterday

date "+%Y-%m-%d %H:%M:%S" --date='1 day ago'

date "+%Y-%m-%d %H:%M:%S" --date='1 week ago'


date "+%Y-%m-%d %H:%M:%S" --date='1 month'
```
등으로 미래, 과거의 시간도 지정 및 포맷팅이 가능하다.





