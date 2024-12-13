package org.example.singleton;


public class InnerClassSingleton {

    private InnerClassSingleton() {}

    private static class SingletonHolder {
        private static final InnerClassSingleton SHARED = new InnerClassSingleton();
    }

    /*
    단순하면서 지연 초기화와 스레드 세이프한 싱글톤 패턴을 구현할 수 있다.
    자바 리플렉션과 직렬화를 통해 싱글톤이 파괴될 수 있다.
    - 리플렉션은 private 생성자에도 접근할 수 있기 때문
    - 직렬화 후 파일에 저장하고, 역직렬화를 하면 싱글톤 보장이 깨진다.
     */
    public static InnerClassSingleton getInstance() {
        return SingletonHolder.SHARED;
    }
}
