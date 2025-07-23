
# Junit의 구조
JUnit 5 = JUnit Platform, JUnit Jupiter + JUnit VIntage 로 이루어져 있다.

## Junit Platform
- Test를 실행하기 위한 뼈대
- Test를 발견하고 Test 계획을 생성하는 TestEngine 인터페이스 제공(Test Engine API)
- 커맨드라인에서 Test를 실행하기 위한 Console Launher 제공
- JUnit 4기반에서 Test Engine을 구동하기 위한 JUnit4 Based Runner 제공
=> JUnit Platform = TestEngine API + Console Launcher + JUnit Based Runner

## JUnit Jupiter
- TestEngine API의 구현체
- 개발자가 테스트 코드를 작성하는데 사용

## JUnit Vintage
- 기존 JUnit 3, 4 TestEngine API 구현체