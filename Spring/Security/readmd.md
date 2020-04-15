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
