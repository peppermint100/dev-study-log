package org.example.singleton;

public class DoubleCheckSingleton {

    /*
    volatile 키워드는 변수의 값이 수정되었을 때 다른 스레드에서도 그 변경사항을 즉시 볼수 있도록 한다.
    이 키워드가 없으면 각 스레드가 CPU 캐시에 값을 저장해두고 사용할 수 있기 때문에
    최신값이 보이지 않을 가능성이 있다.

    또 이 키워드는 컴파일러와 CPU가 명령어를 재정렬하지 못하게 막기 때문에
    더블 체크 락킹에서 중요하다.
    1. 메모리 할당
    2. 생성자 호출
    3. 변수에 메모리 주소 저장

    이 과정에서 2,3의 순서가 바뀌면 생성자가 호출되기전의 값을 참조할 수 있기 때문이다.
     */
    private static volatile DoubleCheckSingleton shared;

    private DoubleCheckSingleton() {}

    public static DoubleCheckSingleton getInstance() {

        if (shared == null) {
            /*
            if 문에 접근하여 초기화 할 때만 동기화 작업을 수행한다.
            volatile 키워드를 통해 최신 값을 볼 수 있도록 보장 받는다.
             */
            synchronized (DoubleCheckSingleton.class) {
                if (shared == null) {
                    shared = new DoubleCheckSingleton();
                }
            }
        }

        return shared;
    }
}
