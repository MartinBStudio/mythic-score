package bs.windrunner.dashboard;

import bs.windrunner.dashboard.model.CharacterModel;
import bs.windrunner.dashboard.model.DungeonModel;
import bs.windrunner.dashboard.raiderioapi.RaiderIoApi;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GreetingsController {
    private final RaiderIoApi raiderIoApi;

    @GetMapping("/dashboard")
    public String greeting(){
        return "dashboard";
    }
    @GetMapping("/characters")
    @ResponseBody
    public String characters(@RequestParam(name = "name", required = true, defaultValue = "default") String name, Model model) throws InterruptedException {
        // Your existing logic to fetch character data goes here
        var characters = new ArrayList<CharacterModel>();
        String[] nameArray = name.split("\\s+");
        // Convert the array to a list
        List<String> nameList = new ArrayList<>(Arrays.asList(nameArray));
        for (String s : nameList) {
            log.info("Name:" +s);
            var characterModel = raiderIoApi.getEntity(s);
            characters.add(characterModel);
            Thread.sleep(2000);
        }
        // Modify the return statement as needed
        return new Gson().toJson(characters);
    }

}