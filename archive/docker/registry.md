## Docker Registry
- 컨테이너 이미지를 보관하는 장소
- Docker hub, private registry

## docker hub
- Official Image(도커가 직접 관리), verified publisher 등 존재
- `docker search [keyword]` 로 도커 허브의 이미지 검색 가능
- 이미지를 도커 허브에 push 할 때는 태그를 달아주어야 한다. `docker tag httpd peppermint100/httpd`

## private registry
- `docker run -d -p 5000:5000 --restart always registry:2`
- private registry로 부터 이미지를 다운로드 받을 때는 [레지스트리 엔드포인트]/[이미지이름]:[태그] 의 형식을 지켜야 한다.

## Amazon Elastic Container Registry
- AWS에서 제공하는 Registry를 사용할 수도 있다.

1. Elastic Container Registry 콘솔 진입
2. Repository 생성
3. 원하는 이미지에 태그를 Repositry와 같게 달아 준 후 push

### ECR Authentication
- `docker push aws.repository.name` 에서 no basic credentials라는 메시지가 뜨며 이미지 푸시가 안되는 경우가 있다.

1. awcli를 설치한다.
2. `aws configure`를 통해 터미널에서 aws를 로그인한다.
3. `aws ecr get-login-password | docker login --username AWS --password-stdin [AWS-ACCOUNT-ID].dkr.ecr.us-east-1.amazonaws.com`를 통해 aws 로그인 정보를 도커로 넘긴다.





