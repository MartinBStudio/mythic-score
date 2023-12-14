package bs.windrunner.dashboard.controller;

import bs.windrunner.dashboard.service.dataprovider.DataProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebController {
    private final DataProvider dataProvider;

    @GetMapping("/dashboard")
    public String greeting() {
        return "dashboard";
    }

    @GetMapping("/characters")
    @ResponseBody
    public String characters(@RequestParam(name = "name") String name, @RequestParam(name = "currentWeek") Boolean currentWeek,@RequestParam(name = "dummyMode") Boolean isDummyMode, Model model) throws InterruptedException {
        return dataProvider.provideJsonStringData(name, isDummyMode, currentWeek);
    }

}