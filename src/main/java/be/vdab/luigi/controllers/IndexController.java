package be.vdab.luigi.controllers;

import be.vdab.luigi.domain.Adres;
import be.vdab.luigi.domain.Persoon;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/")
class IndexController {
    private AtomicInteger aantalKeerBekeken = new AtomicInteger();
    @GetMapping
    public ModelAndView index(){
        var morgenOfMiddagOfAvond =
                LocalTime.now().getHour() < 12 ? "morgen" : LocalTime.now().getHour() > 18 ? "avond" : "middag";
        var modelAndView = new ModelAndView("index", "moment", morgenOfMiddagOfAvond);
        modelAndView.addObject("zaakvoerder",
                new Persoon("Luigi", "Peperone", 7, true,
                        LocalDate.of(1966, 1, 31),
                        new Adres("Grote markt", "3", 9700, "Oudenaarde")));
        modelAndView.addObject("aantalkeer",aantalKeerBekeken.incrementAndGet());
        return modelAndView;
    }
}
