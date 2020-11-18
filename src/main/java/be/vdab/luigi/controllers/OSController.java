package be.vdab.luigi.controllers;

import be.vdab.luigi.domain.Adres;
import be.vdab.luigi.domain.Persoon;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Controller
@RequestMapping("os")
class OSController {
    private static final String[] oss = {"Windows","Macintosh","Android","Linux"};
    @GetMapping
    public ModelAndView oso(@RequestHeader("User-Agent") String userAgent){
        var modelAndView = new ModelAndView("os");
        Arrays.stream(oss).filter(os -> userAgent.contains(os)).findFirst()
                .ifPresent(os -> modelAndView.addObject("os", os));
        return modelAndView;
    }
}
