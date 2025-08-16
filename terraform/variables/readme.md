## terraform 환경마다 다른 변수 사용하게 하기
```bash
terraform plan -var-file={filename.tfvars}
```

- tfvars로 끝나는 파일 이름을 -var-file의 옵션의 값으로 넣어주면 다른 varibles들을 무시하고 이 값을 var 객체에 담아서 사용한다.
