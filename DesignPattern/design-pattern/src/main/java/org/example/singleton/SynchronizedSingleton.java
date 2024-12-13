package org.example.singleton;

public class SynchronizedSingleton {

    private static SynchronizedSingleton shared;

    private SynchronizedSingleton() {}

    public static synchronized SynchronizedSingleton getInstance() {
        /*
        synchronized 키워드를 선언하면, 이 메서드에 한 번에 하나의 스레드만 접근이 가능하다.
        이로 인해 Thread safe해지지만 여러 스레드가 동시에 접근하려고 할 때
        Lock을 통한 동기화 작업 때문에 성능 저하가 발생할 수 있다.
         */
        if (shared == null) {
            shared = new SynchronizedSingleton();
        }

        return shared;
    }
}
