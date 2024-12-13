package org.example.singleton;

public class BasicSingleton {

    private static BasicSingleton shared;

    // 외부에서 생성할 수 없도록 생성자를 private으로 처리한다.
    private BasicSingleton() {
    }

    // 접근 할 때 싱글톤 객체 생성
    public static BasicSingleton getInstance() {
        /*
         하지만 이 코드는 Thread safe하지 않다.
         스레드 A가 getInstance에 최초로 접근하여 shared를 생성하는 동안
         스레드 B가 접근하면 if의 조건을 통과할 수 있기 때문이다.
         */
        if (shared == null) {
            shared = new BasicSingleton();
        }

        return shared;
    }
}
