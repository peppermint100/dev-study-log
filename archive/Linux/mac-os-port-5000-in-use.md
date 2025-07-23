
1. Mac에서 포트 5000번이 이미 사용중인 문제 발견
2. `lsof -i :5000`으로 해당 포트가 어떤 PID를 실행 중인지 확인
3. `ps -ef | grep [PID]` 로 어떤 프로세스 실행 중인지 확인
4. 시스템 내에서 ControlCeneter를 실행 중인 정보 확인
5. [Airplay를 종료하면 된다는 글](https://hyunsoft.tistory.com/entry/Monterey%EC%9D%98-Control-Center-LISTEN-5000)을 발견하여 종료하여 해결
