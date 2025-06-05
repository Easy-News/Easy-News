package shimp.easy_news.news.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shimp.easy_news.news.constant.SubCategory;
import shimp.easy_news.news.dto.NewsDescriptionReqDto;
import shimp.easy_news.news.dto.NewsDescriptionResDto;
import shimp.easy_news.news.service.NewsService;

//@RestController
//@RequestMapping("/news")
//@RequiredArgsConstructor
//public class NewsController {
//
//    private final NewsService newsService;
//
//    @GetMapping("/news-description")
//    public ResponseEntity<NewsDescriptionResDto> getNewsDescription(@RequestParam Long userId,
//                                                                    @RequestParam SubCategory subCategory) {
//        NewsDescriptionReqDto newsDescriptionResDto = newsService.buildDescriptionDataBySubCategory(userId, subCategory);
//        return ResponseEntity.ok(newsDescriptionResDto);
//
//    }
//
//}
