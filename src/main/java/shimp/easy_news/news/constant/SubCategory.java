package shimp.easy_news.news.constant;

public enum SubCategory {
    DOMESTIC_POLITICS,                // 국내정치
    ELECTION_AND_PRESIDENTIAL,        // 선거·대선
    INTERNATIONAL_POLITICS_AND_DIPLOMACY, // 국제정치·외교
    ECONOMIC_POLICY,                  // 경제정책
    CORPORATE_AND_INDUSTRY_TRENDS,    // 기업·산업 동향
    FINANCE_AND_SECURITIES,           // 금융·증권
    IT_AND_SCIENCE_TECHNOLOGY,        // IT·과학기술
    TELECOMMUNICATION_AND_MOBILE,     // 통신·모바일
    SOCIETY_AND_WELFARE,              // 사회일반·복지
    INCIDENT_AND_ACCIDENT,            // 사건·사고
    LEGAL_AND_SECURITY,               // 법조·안보
    ENVIRONMENT_AND_CLIMATE,          // 환경·기후
    CULTURE_AND_ART,                  // 문화·예술
    ENTERTAINMENT_AND_BROADCASTING,   // 연예·방송
    SPORTS,                           // 스포츠
    HEALTH_AND_MEDICAL,               // 건강·의료
    EDUCATION_AND_ADMISSIONS,         // 교육·입시
    REAL_ESTATE_AND_CONSTRUCTION,     // 부동산·건설
    TRAVEL_AND_LEISURE,               // 여행·레저
    COLUMN_AND_OPINION;               // 칼럼·오피니언

    @Override
    public String toString() {
        return name();
    }
}
