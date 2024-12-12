package org.example.adaptor;

enum SiriQueryType {
    // Siri에게는 날씨, 위치, 웹 검색을 시킬 수 있다.
    WEATHER, LOCATION, WEB_SEARCH;
}

interface SiriAgent {
    String ask(SiriQueryType query);
}

class SiriAgentImpl implements SiriAgent {

    // Siri에게 질문한다. 답변을 리턴한다.
    public String ask(SiriQueryType query) {
        System.out.println("Asking Siri About " + query.toString());
        return "Answer From Siri About " + query.toString();
    }
}