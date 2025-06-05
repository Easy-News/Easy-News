package shimp.easy_news.gpt.constant;

import lombok.Getter;

public enum GptRole {

    SUMMARY(
            "당신은 뉴스 요약 도우미입니다. 핵심만 간단히 요약해 주세요.",
            """
            아래는 '%s' 카테고리의 뉴스 목록입니다:
    
            %s
    
            위 뉴스를 3~4문장으로 요약해 주세요.
            """
    ),

    EXPLANATION(
            "당신은 뉴스 설명 도우미입니다. 사용자가 이해하기 쉽게 뉴스의 배경과 핵심 키워드를 중심으로 설명해 주세요.",
            """
            아래는 '%s' 카테고리에 해당하는 주요 헤드라인 뉴스입니다:
        
            %s
        
            위 뉴스들을 바탕으로,
            - 사회적 배경과 시대적 맥락을 함께 설명해 주세요.
            - 자주 등장하는 핵심 키워드를 중심으로 풀어서 설명해 주세요.
            - 관련된 주요 개념이나 용어가 있다면 초보자도 이해할 수 있게 예시와 함께 설명해 주세요.
        
            일반인이 쉽게 이해할 수 있도록 친절하고 상세하게 설명해 주세요.
            """
    );


    @Getter
    private final String systemMessage;
    private final String userPromptTemplate;

    GptRole(String systemMessage, String userPromptTemplate) {
        this.systemMessage = systemMessage;
        this.userPromptTemplate = userPromptTemplate;
    }

    public String formatUserPrompt(String categoryName, String newsText) {
        return userPromptTemplate.formatted(categoryName, newsText);
    }
}
