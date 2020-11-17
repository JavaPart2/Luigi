package be.vdab.luigi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalTime;

@Controller
@RequestMapping("/")
class IndexController {
    @GetMapping
    public ModelAndView index(){
        var morgenOfMiddagOfAvond = LocalTime.now().getHour() < 12 ? "morgen" : LocalTime.now().getHour() > 18 ? "avond" : "middag";
        return new ModelAndView("index", "moment", morgenOfMiddagOfAvond);
    }
}
