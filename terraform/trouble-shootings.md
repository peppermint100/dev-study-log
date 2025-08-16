## Terraform destory 할 때 관리하고 있는 리소스가 없는 에러
- 현재 상태 파일이 현재 aws 리소스와 최신상태와 동기화되지 않았음
```bash
mv terraform.tfstate.backup terraform.tfstate
terraform state list
```
테라폼 backup 파일로 state 파일을 복구하여 해결

## default vpc가 없어서 ec2 실행 불가능
```bash
echo $AWS_ACCESS_KEY_ID
echo $AWS_SECRET_ACCESS_KEY
echo $AWS_SESSION_TOKEN
echo $AWS_PROFILE
```
현재 로컬 머신의 환경 변수가 `aws configure`의 정보를 오버라이드하여 다른 AWS 계정을 보고 있음

```bash
unset AWS_ACCESS_KEY_ID
unset AWS_SECRET_ACCESS_KEY
unset AWS_SESSION_TOKEN
unset AWS_PROFILE
```
환경변수를 초기화 하여 해결
