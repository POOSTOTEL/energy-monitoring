package bsuir.backend.generator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/report/")
@Slf4j
public class ReportController {

    @GetMapping("")
    public String index() {
        return "index";
    }
}
