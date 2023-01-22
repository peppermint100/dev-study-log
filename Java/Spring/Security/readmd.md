config에서 anMathers.permitAll()이 먹히지 않는다.
이 경우엔 GetMapping은 다 가능하지만 PostMapping이 막힌다.

```java
   @Override
    public void configure(WebSecurity web) throws Exception
    {
        // static files does not need to be authenticated always work with out authentication
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/signin");
    }
```

위 코드에서 처럼 허용하고 싶은 url을 추가해주면 임시적으로
해결이 가능하나 왜 permitall이 안먹히는지는 모르겠다.

참고링크
https://github.com/spring-projects/spring-security/issues/4368

Spring security가 적용된 스프링 웹 어플리케이션에 post요청을 보내려면
csrf 토큰이 필요하다. Postman에는 기본적으로 csrf토큰이 탑재되어 있지
않아서 요청을 받을 수 없었던 것이다....(몇 시간 삽질함)

postman에서 csrf 토큰을 함께 보내는 방법도 있지만 개발 단계에서는 간단하게
spring security의 설정(config 파일)에

```java
http.csrf().disable().authorizeRequests()
```

이렇게 csrf를 잠깐 비활성화 하면 정상적으로 permitall이 작동한다.
