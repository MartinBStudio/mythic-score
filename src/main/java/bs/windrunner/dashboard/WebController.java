package bs.windrunner.dashboard;

import bs.windrunner.dashboard.model.CharacterModel;
import bs.windrunner.dashboard.raiderioapi.RaiderIoApi;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebController {
    private final RaiderIoApi raiderIoApi;
    private final boolean isDummyMode = false;

    @GetMapping("/dashboard")
    public String greeting() {
        return "dashboard";
    }

    @GetMapping("/characters")
    @ResponseBody
    public String characters(@RequestParam(name = "name") String name, @RequestParam(name = "currentWeek") Boolean currentWeek, Model model) throws InterruptedException {
        var characters = new ArrayList<CharacterModel>();
        String[] nameArray = name.split("\\s+");
        List<String> nameList = new ArrayList<>(Arrays.asList(nameArray));
        for (String s : nameList) {
            var characterModel = isDummyMode ? raiderIoApi.getFakeEntity(s) : raiderIoApi.getEntity(s, currentWeek);
            characters.addAll(characterModel);
            Thread.sleep(1000);
        }
        return new Gson().toJson(characters);
    }

}
