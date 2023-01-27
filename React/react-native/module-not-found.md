'react-native-dotenv' 패키지를 다운로드 받은 후 [공식 Npm 패키지 사이트](https://www.npmjs.com/package/react-native-dotenv)를 보고
설정했음에도 계속 `unable to find module @env` 라는 에러가 발생

env는 플러그인으로 빌드 할 때 env 환경변수가 들어가서 앱이 빌드되어야 하는데, 핫 리로드 기능을 이용할 땐
빌드 파일이 전체적으로 캐싱되어 있어서 새로 들어간 @env를 읽지 못하는 것으로 판단

`npx expo -c`로 캐시 초기화하고 재 빌드하니 제대로 작동함
