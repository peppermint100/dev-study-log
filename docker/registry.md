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





