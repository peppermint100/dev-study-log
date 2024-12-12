package org.example.adaptor;

public class Client {

    // 기존에 있던 Siri의 질의를 새로운 OpenAI 질의와 호환되도록 한다.
    public static void main(String[] args) {

        OpenAIAgentImpl openAIAgent = new OpenAIAgentImpl();

        SiriAdaptor siriAdaptor = new SiriAdaptor(openAIAgent);

        // Adaptor를 통해 SiriAgent에 맞는 QueryType을 OpenAIAgent에게 질의한다.
        String answer = siriAdaptor.ask(SiriQueryType.WEATHER);
        System.out.println("answer = " + answer);
    }
}

/*
어댑터 패턴은 클래스를 호환을 위한 어댑터로 사용한다. 110v 제품을 어댑터를 통해 220v로
교체하는 원리와 같다. 즉 호환성이 없는 인터페이스를 호환되도록 도와준다.

- 기존에 있는 시스템에 새로운 서드파티 라이브러리를 추가 할 때
- 레거시 인터페이스를 새로운 인터페이스로 교체할 때
사용하면 코드의 재사용성을 높일 수 있다.

SRP, OCP를 만족한다. 어댑터는 변환 역할을 수행하며 기존 클래스를 건들지 않고 확장의
형태로 클래스를 호환시킨다.
 */
