# Docker Network 
도커는 호스트와 컨테이너를 브릿지를 통해서 연결시킨다.
기본적으로 172.17.0.1에 CIDR를 통해서 컨테이너가 생성될 때마다 하나씩 생성한다.

이렇게 할당 되는 ip는 변경할 수 없지만 브릿지의 ip, cidr는 정할 수 있다.

```

$ docker network create --driver bridge --subnet 192.168.100.0/24 --gateway 192.168.100.254 [네트워크이름]
$ docker network ls
```

위 명령어는 192.168.100.0/24 의 네트워크 대역을 사용하고 브릿지의 네트워크(gateway)로는 192.168.100.254를 사용하게 된다.

이 후

```
$ docker run -d --name [이름] --net [네트워크이름] -p 80:80 -ip 192.168.100.100
```
로 컨테이너를 실행하면 할당되는 ip, 브릿지의 ip를 docker network를 등록함으로서 설정할 수 있게 된다.



