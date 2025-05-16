package shimp.easy_news.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import shimp.easy_news.domain.User;
import shimp.easy_news.domain.UserClicks;
import shimp.easy_news.repository.UserRepository;

@Controller
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute User user) {
        user.setUserClicks(UserClicks.ofZero());
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
    public String hello() {
        return "home";
    }

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
}
