package test.cobaltforge.lab;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        String host = request.getHeader("Host");
        model.addAttribute("secure", request.isSecure());
        model.addAttribute("host", host != null ? host : request.getServerName());
        model.addAttribute("javaVersion", Runtime.version().toString());
        return "index";
    }
}
