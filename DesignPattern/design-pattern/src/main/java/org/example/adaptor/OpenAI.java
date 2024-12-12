package org.example.adaptor;


enum OpenAIQueryType {
    // OPEN AI는 SYSTEM, 즉 디바이스 장치 관련 질문과 웹 검색을 할 수 있다.
    SYSTEM, WEB_SEARCH
}

interface OpenAIAgent {
    String ask(OpenAIQueryType query);
}

class OpenAIAgentImpl implements OpenAIAgent {
    public String ask(OpenAIQueryType query) {
        System.out.println("Asking OpenAI About " + query.toString());
        return "Answer From OpenAI About " + query.toString();
    }
}