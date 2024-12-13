package org.example.singleton;

public enum EnumSingleton {
    SHARED;
    /*
    스레드 세이프하며,
     리플렉션(인스턴스 생성 불가), 직렬화가 방어된다.(자동 Serializable 구현)
    복잡한 초기화가 필요한 경우 적합하지 않다.
    */
    public void doSomething() {
        // 로직
    }

    /*
    사용
    EnumSingleton singleton = EnumSingleton.SHARED;
    singleton.doSomething();
     */
}
