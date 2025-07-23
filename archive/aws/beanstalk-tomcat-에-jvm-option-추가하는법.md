
Beanstalk은 템플릿을 다 만들어주어서 편리하지만 실제로 디테일 한 세팅을 할 때는
빈스톡만의 방식을 따라야 하기 때문에 불편하다.

JVM 관련 세팅은 Beanstalk Console -> Configuration -> Software 에서 진행한다.

Tomcat 플랫폼 기준으로 Container Options에서 대부분의 세팅이 가능하고 프록시 서버 선택, Xmx, Xms는
콘솔에 존재한다. 이 외의 설정을 하려면

JVM options에 나열하면 된다. 형식이 따로 명시가 되어 있지 않은데,
예를 들어 메모리 에러 시 힙덤프를 뜨고 그 파일의 경로를 저장하는 설정은 아래와 같다.


`-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp`

즉 JVM 실행 옵션에 붙이는 방식과 같으며 각 방식에 , 와 같은 세퍼레이터를 넣지 않는다.

