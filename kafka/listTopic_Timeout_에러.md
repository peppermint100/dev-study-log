
kafka 폴더의 config/server.properties 파일 맨 아래

listeners=PLAINTEXT://localhost:9092
를 추가

이 후 캐시 삭제를 위해 server.properties에 보면 log.dirs 값이 있는데 이 경로에 접근하여
kafka_logs 폴더를 삭제후 카프카 서버를 다시 재기동
