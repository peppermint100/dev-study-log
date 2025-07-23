## URI(Uniform Resource Identifier)

- 자원 식별자의 의미(인터넷에 있는 자원을 나타낸다.)
- 정보 리소스를 고유하게 식별하고 위치를 지정할 수 있다.
- 인터넷 프로토콜(http, https)에 항상 붙어 있는 개념이다.
- URL, URN의 부모 개념(상위 개념)

## URL(Uniform Resource Locator)

- 특정 서버의 한 리소스에 대한 구체적인 위치(locatiton)
- http://localhost:3000/src/index.html이 URL에 해당한다.
- http://localhost:3000/data?id=1은 URI이지만 URL이 아니다. 왜냐하면 id가 식별의 역할을 하기 때문

## URN(Uniform Resource Name)

- 리소스의 위치 정보와 상관없이 리소스의 이름을 이용하여 접근

- 리소스가 unavailable해지더라도 유효하다. 그런 경우 404 응답을 받는다.

## Conclusion

만약 http://www.naver.com/cafe 라고하면 이는 URI 이면서 URL이다. cafe라는 식별자를 더함으로서 URI에 해당하고 cafe라는 네이버의 기능 안에 존재하는 cafe들을 보여주기 때문이다. 하지만 만약 http://www.naver.com/cafe?name=javascript 라고 한다면 javascript라는 이름을 가진 카페들만 찾도록새로운 식별자를 넣어주었으므로 URL은 아니지만 URI가 된다고 할 수 있다.

이 세 가지 개념 자체에도 굉장히 많은 의견들이 있다. 오래된 자료에는(W3C) URL과 URI의 의미는 같다라는 의견도 있다. 하지만 대부분은 URI가 URL과 URN의 개념을 포함하는 의미가 대부분이다. 구글링, 스택오버플로우를 통해 다양한 의견을 보았지만 결론적으로 이 세 가지를 정확히 구분하는 것은 중요하지 않다. 다만 URL은 파일, 리소스의 위치만을 표시하는 것을 뜻하고 URI는 그보다 상위 개념으로 리소스를 구분짓는(identificate) 형태를 뜻하는 정도는 알아 둘 필요가 있다.
