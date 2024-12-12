package org.example.adaptor;

// SiriAdaptor는 SiriAgent를 구현한다.
public class SiriAdaptor implements SiriAgent {

    private OpenAIAgent openAIAgent;

    public SiriAdaptor(OpenAIAgent openAIAgent) {
        this.openAIAgent = openAIAgent;
    }

    @Override
    public String ask(SiriQueryType query) {
        OpenAIQueryType openAIQueryType = switch (query) {
            // 날씨와 위치는 디바이스가 가지고 있는 정보로 OpenAI의 SYSTEM 정보에 해당한다.
            // 이를 통해 SiriQueryType의 WEATHER, LOCATION을 OpenAI의 SYSTEM에
            // Adapt한다.
            case WEATHER, LOCATION -> OpenAIQueryType.SYSTEM;
            // 웹 검색은 웹 검색으로 넘긴다.
            case WEB_SEARCH -> OpenAIQueryType.WEB_SEARCH;
        };

        return openAIAgent.ask(openAIQueryType);
    }
}
