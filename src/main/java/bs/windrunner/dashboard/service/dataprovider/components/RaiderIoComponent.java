package bs.windrunner.dashboard.service.dataprovider.components;

import bs.windrunner.dashboard.model.CharacterModel;
import bs.windrunner.dashboard.model.DungeonModel;
import bs.windrunner.dashboard.service.utils.Utils;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unchecked")
public class RaiderIoComponent {
    private final Utils utils;
    private final String currentWeekField = "mythic_plus_weekly_highest_level_runs";
    private final String previousWeekField = "mythic_plus_previous_weekly_highest_level_runs";


    public String getData(String namesParam, boolean currentWeek) throws InterruptedException {
        List<String> nameList = utils.formatNamesParam(namesParam);
        var notFoundCharacters = new ArrayList<CharacterModel>();
        var characters = new ArrayList<CharacterModel>();
        for (String s : nameList) {
            RestAssured.baseURI = "https://raider.io/api/v1/characters/profile";

            // Define the query parameters
            String region = "eu";
            String realm = "drak-thul";
            String backupRealm = "burning-blade";
            String fields = currentWeek ? currentWeekField : previousWeekField;
            var responseList = new ArrayList<Response>();
            // Make the API request
            responseList.add(RestAssured.given()
                    .queryParam("region", region)
                    .queryParam("realm", realm)
                    .queryParam("name", s)
                    .queryParam("fields", fields)
                    .get());
            responseList.add(RestAssured.given()
                    .queryParam("region", region)
                    .queryParam("realm", backupRealm)
                    .queryParam("name", s)
                    .queryParam("fields", fields)
                    .get());
            if(checkIfBothRequestsFailed(responseList)){
                notFoundCharacters.add(utils.buildCharacter(List.of(DungeonModel.builder().shortName("Character not found.").build()),s,"",""));
            }
            else{
                for (Response r : responseList) {
                    JsonPath jsonPath = new JsonPath(r.getBody().asString());
                    if (r.getStatusCode() == 200) {
                        characters.add(processResponseJson(jsonPath, currentWeek));
                    }
                }
            }
            Thread.sleep(100);
        }
        characters.addAll(notFoundCharacters);
        return new Gson().toJson(characters);
    }
    private boolean checkIfBothRequestsFailed(List<Response> list) {
        for (Response item : list) {
            if (item.statusCode()!=400) {
                return false;
            }
        }
        return true;
    }
    private CharacterModel processResponseJson(JsonPath jsonPath, boolean currentWeek) {
        final String currentWeekField = "mythic_plus_weekly_highest_level_runs";
        final String previousWeekField = "mythic_plus_previous_weekly_highest_level_runs";
        var name = jsonPath.getString("name");
        var characterClass = jsonPath.getString("class");
        var characterRealm = jsonPath.getString("realm");
        var dungeons = currentWeek ? jsonPath.getList(currentWeekField) : jsonPath.getList(previousWeekField);
        var finishedDungeons = new ArrayList<DungeonModel>();
        for (Object d : dungeons) {
            if (d instanceof Map) {
                Map<String, Object> dungeonMap = (Map<String, Object>) d;
                String dungeonName = (String) dungeonMap.get("dungeon");
                String shortName = (String) dungeonMap.get("short_name");
                int level = (int) dungeonMap.get("mythic_level");
                finishedDungeons.add(DungeonModel.builder().shortName(shortName).name(dungeonName).finishedKeyLevel(level).build());
            }
        }
        return utils.buildCharacter(finishedDungeons,name,characterRealm,characterClass);
    }


}
