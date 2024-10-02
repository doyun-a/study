package mysqltest.test1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/") // 루트 URL에 대한 GET 요청 처리
    public String home() {
        return "home"; // templates/index.html로 이동
    }
}