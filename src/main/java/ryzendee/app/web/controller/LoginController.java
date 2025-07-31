package ryzendee.app.web.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginView(HttpSession httpSession, Model model) {
        // Для демонстрации токена на стронице
        String token = (String) httpSession.getAttribute("token");

        if (token != null) {
            model.addAttribute("successMessage", "Your token: " + token);
        }

        return "login-view";
    }
}

