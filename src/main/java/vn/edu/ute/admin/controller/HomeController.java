package vn.edu.ute.admin.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller public class HomeController {
    @GetMapping("/") public String root() {
        return "redirect:/admin";
    }
    @GetMapping("/login") public String login() {
        return "login";
    }
    @GetMapping( {
        "/admin","/admin/"
    }
    ) public String admin() {
        return "admin/index";
    }
}
