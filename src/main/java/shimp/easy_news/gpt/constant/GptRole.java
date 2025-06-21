package shimp.easy_news.gpt.constant;

import lombok.Getter;

public enum GptRole {

    SUMMARY(
            "당신은 뉴스 요약 도우미입니다. 핵심만 간단히 요약해 주세요.",
            """
            아래는 '%s' 카테고리의 뉴스 목록입니다:
    
            %s
    
            각 뉴스의 공통된 핵심 흐름을 중심으로 3~4문장으로 요약해 주세요.
            - 문장은 '-습니다' 형태로 정중하게 작성해 주세요.
            - 불필요한 수식 없이 간결하고 명확한 문장을 사용해 주세요.
            """
    ),

    EXPLANATION(
            "당신은 뉴스 설명 도우미입니다. 사용자가 이해하기 쉽게 뉴스의 배경과 핵심 키워드를 중심으로 설명해 주세요.",
            """
            아래는 '%s' 카테고리에 해당하는 주요 헤드라인 뉴스입니다:
        
            %s
        
            위 뉴스들을 바탕으로 아래 항목에 따라 설명해 주세요:
        
            1. 사회적 배경과 시대적 흐름
               - 기술이 등장하게 된 배경과 시기를 설명해 주세요.
               - 관련된 사회적 변화나 문제 상황이 있다면 함께 설명해 주세요.
        
            2. 핵심 키워드 해설
               - 뉴스에서 반복적으로 언급되는 주요 용어를 나열해 주세요.  
               - 각 키워드에 대해 의미와 활용 예시를 간단히 설명해 주세요.
        
            3. 관련 기술 개념 소개  
               - 뉴스에서 다뤄지는 기술, 알고리즘, 장치 등과 같은 개념이 있다면 초보자도 이해할 수 있도록 풀어 주세요.
        
            ※ 설명은 마크다운이나 특수 서식(**, ##)을 사용하지 말고 일반 텍스트 형식으로 작성해 주세요.  
            ※ 가능한 한 항목별로 구분하여 보기 쉽게 작성해 주세요.
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
