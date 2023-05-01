
# Junit Lifecycle
## @Test
- 테스트용 메소드임을 표현

## @BeforeEach
- 각 테스트 메소드가 시작되기 전에 실행되어야 하는 메소드임을 표현

## @AfterEach
- 각 테스트 메소드가 시작된 후 실행되어야 하는 메소드임을 표현

## @BeforeAll
- 해당 테스트 시작 전에 실행되어야 하는 메소드를 표현
- static 처리가 필요 할 때 

## @AfterAll
- 해당 테스트 종료 후에 실행되어야 하는 메소드를 표현
- static 처리가 필요 할 때 

# Junit Main Annotation

## SpringBootTest
- @SpringBootApplicatioㅇ을 찾아서 모든 Bean을 스캔함
- 스캔 이후 Test 용도의 ApplicationContext를 만들어 Bean을 추가하며 MockBean이 있다면 교체한다.
- 통합 테스트 용도로 사용

## @ExtendWith
- 메인으로 실행될 Class를 지정
- @SpringBootTest는 기본적으로 @ExtendsWith를 가지고 있음
- JUnit4의 @Runwith과 동일

## @WebMvcTest(클래스명.class)
- () 의 클래스만 로드하여 테스트
- ()의 디폴트 값은 컨트롤러 관련 Bean만 해당
- 스프링의 모든 Bean을 로드하는 @SpringBootTest 대신 컨트롤러 관련코드(@Controller, @RestController, @RestControllerAdvice 등) 만 로드하여 테스트

## @MockBean
- 가짜 객체를 생성하여 주입

## @AutoConfigureMockMvc
- spring.test.mockmvc의 설정을 로드하면서 MockMvc의존성을 주입
- MockMvc는 Rest API를 테스트함

## @Import
- 필요한 Class들을 Configuration으로 만들어 사용할 수 있음
- Import 된 Class들은 의존성 주입하여 사용 가능

