
## 도커 컨테이너에서 파일 복사하기

```
$ docker cp <path> <to container>:<path>
$ docker cp <from container>:<path> <path>
$ docker cp <from container> <to container>:<path>
```

로컬부터 컨테이너, 컨테이너로부터 로컬, 컨테이너와 컨테이너 어디든 파일을 복사할 수 있다
