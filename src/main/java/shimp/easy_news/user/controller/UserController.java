package shimp.easy_news.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import shimp.easy_news.user.repository.UserRepository;
import shimp.easy_news.user.domain.User;
import shimp.easy_news.user.domain.UserClicks;
=======
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shimp.easy_news.news.constant.SubCategory;
import shimp.easy_news.news.domain.News;
import shimp.easy_news.news.repository.NewsRepository;
import shimp.easy_news.recommendation.VisitLogService;
import shimp.easy_news.user.dto.HomeNewsResponse;
import shimp.easy_news.user.repository.UserRepository;
import shimp.easy_news.user.domain.User;
import shimp.easy_news.user.domain.UserClicks;
import shimp.easy_news.user.service.NewsCheckService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
>>>>>>> origin/feat/newsCheck

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
<<<<<<< HEAD
=======
    private final NewsRepository newsRepository;
    private final NewsCheckService newsService;
    private final VisitLogService visitLogService;
    private final NewsCheckService newsCheckService;
>>>>>>> origin/feat/newsCheck

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute User user) {
        user.setUserClicks(UserClicks.ofZero());
        System.out.println("username = " + user.getUsername());
        System.out.println("password = " + user.getPassword());
        System.out.println("email = " + user.getEmail());
        System.out.println("category = " + user.getInterested());
        userRepository.save(user);
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginForm") LoginForm form,
                               HttpSession session, Model model) {
        User user = userRepository.findByUsername(form.getUsername()).orElse(null);
        if (user != null && user.getPassword().equals(form.getPassword())) {
            session.setAttribute("loginUser", user);
            log.error("LOGIN SUCCESS");
            return "redirect:/home";
        }
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/home")
<<<<<<< HEAD
    public String hello() {
        return "home";
    }

=======
    public String home(Model model,
                       HttpSession session,
                       @RequestParam(defaultValue = "5") int size) {

        User loginUser = (User) session.getAttribute("loginUser");
        HomeNewsResponse homeNews;

        if (loginUser != null) {
            homeNews = newsService.getHomeNews(0, size, loginUser);
            model.addAttribute("userName", loginUser.getUsername());
        }else{
            homeNews = newsService.getHomeNews(0,size,null);
            model.addAttribute("userName", "게스트");
        }

        NewsCheckForm newsCheckForm = new NewsCheckForm();
        newsCheckForm.setSize(size);

        model.addAttribute("newsCheckForm", newsCheckForm);
        model.addAttribute("personalizedNews", homeNews.getPersonalizedNews());
        model.addAttribute("headlineNews", homeNews.getHeadlineNews());
        model.addAttribute("realTimeNews", homeNews.getRealTimeNews());

        return "home";
    }

    @PostMapping("/news/click")
    @ResponseBody
    public ResponseEntity<String> recordNewsClick(@RequestParam Long newsId,
                                                  HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            News news = newsRepository.findById(newsId).orElse(null);
            if (news != null) {
                // SubCategory 에 따라 해당 클릭 수 증가
                updateUserClicks(loginUser, news.getSubCategory());
            }
        }
        return ResponseEntity.ok("success");
    }

    @GetMapping("/article/{newsId}")
    public String showArticle(@PathVariable Long newsId, Model model, HttpSession session) {
        Optional<News> newsOptional = newsRepository.findById(newsId);

        if (newsOptional.isEmpty()) {
            return "redirect:/home";
        }

        News news = newsOptional.get();

        // 모든 이미지 URL 추출
        List<String> imageUrls = newsCheckService.extractAllImageUrls(news.getImageUrl());
        for (String img : imageUrls) {
            System.out.println("뉴스 이미지: " + img);
        }
        System.out.println("추출된 이미지 개수: " + imageUrls.size());

        User loginUser = (User) session.getAttribute("loginUser");
//        if (loginUser != null) {
//            // 클릭 로깅 (비동기로 처리)
//            CompletableFuture.runAsync(() -> {
//                try {
//                    visitLogService.logVisit(loginUser.getUserId(), "/news/" + newsId);
//                } catch (Exception e) {
//                    log.error("클릭 로깅 실패", e);
//                }
//            });
//        }

        model.addAttribute("news", news);
        model.addAttribute("imageUrls", imageUrls);
        model.addAttribute("userName", loginUser != null ? loginUser.getNickname() : "게스트");

        return "article";
    }


    private void updateUserClicks(User user, SubCategory subCategory) {
        UserClicks userClicks = user.getUserClicks();

        switch (subCategory) {
            case DOMESTIC_POLITICS:
                userClicks.incrementDomestic_politics_clicks();
                break;
            case ELECTION_AND_PRESIDENTIAL:
                userClicks.incrementElection_and_presidential_clicks();
                break;
            case INTERNATIONAL_POLITICS_AND_DIPLOMACY:
                userClicks.incrementInternational_politics_and_diplomacy_clicks();
                break;
            case ECONOMIC_POLICY:
                userClicks.incrementEconomic_policy_clicks();
                break;
            case CORPORATE_AND_INDUSTRY_TRENDS:
                userClicks.incrementCorporate_and_industry_trends_clicks();
                break;
            case FINANCE_AND_SECURITIES:
                userClicks.incrementFinance_and_securities_clicks();
                break;
            case IT_AND_SCIENCE_TECHNOLOGY:
                userClicks.incrementIt_and_science_technology_clicks();
                break;
            case TELECOMMUNICATION_AND_MOBILE:
                userClicks.incrementTelecommunication_and_mobile_clicks();
                break;
            case SOCIETY_AND_WELFARE:
                userClicks.incrementSociety_and_welfare_clicks();
                break;
            case INCIDENT_AND_ACCIDENT:
                userClicks.incrementIncident_and_accident_clicks();
                break;
            case LEGAL_AND_SECURITY:
                userClicks.incrementLegal_and_security_clicks();
                break;
            case ENVIRONMENT_AND_CLIMATE:
                userClicks.incrementEnvironment_and_climate_clicks();
                break;
            case CULTURE_AND_ART:
                userClicks.incrementCulture_and_art_clicks();
                break;
            case ENTERTAINMENT_AND_BROADCASTING:
                userClicks.incrementEntertainment_and_broadcasting_clicks();
                break;
            case SPORTS:
                userClicks.incrementSports_clicks();
                break;
            case HEALTH_AND_MEDICAL:
                userClicks.incrementHealth_and_medical_clicks();
                break;
            case EDUCATION_AND_ADMISSIONS:
                userClicks.incrementEducation_and_admissions_clicks();
                break;
            case REAL_ESTATE_AND_CONSTRUCTION:
                userClicks.incrementReal_estate_and_construction_clicks();
                break;
            case TRAVEL_AND_LEISURE:
                userClicks.incrementTravel_and_leisure_clicks();
                break;
            case COLUMN_AND_OPINION:
                userClicks.incrementColumn_and_opinion_clicks();
                break;
            default:
                // 알 수 없는 카테고리인 경우 로그 출력
                System.out.println("Unknown SubCategory: " + subCategory);
                break;
        }

        System.out.println("SubCategory: " + subCategory);

        userRepository.save(user);
    }


>>>>>>> origin/feat/newsCheck
    // Login form backing object
    public static class LoginForm {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
<<<<<<< HEAD
=======

    public static class NewsCheckForm {
        private List<Long> selectedNewsIds;
        private String category;
        private int size;

        public NewsCheckForm() {
            this.selectedNewsIds = new ArrayList<>();
            this.size = 5;
        }

        public List<Long> getSelectedNewsIds() {
            return selectedNewsIds;
        }

        public void setSelectedNewsIds(List<Long> selectedNewsIds) {
            this.selectedNewsIds = selectedNewsIds;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

>>>>>>> origin/feat/newsCheck
}
