package bs.windrunner.dashboard.service.dataprovider.components;

import bs.windrunner.dashboard.model.CharacterModel;
import bs.windrunner.dashboard.model.DungeonModel;
import bs.windrunner.dashboard.service.utils.Utils;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
@RequiredArgsConstructor
public class RaiderIoComponent {
    private final Utils utils;
    private final String currentWeekField = "mythic_plus_weekly_highest_level_runs";
    private final String previousWeekField = "mythic_plus_previous_weekly_highest_level_runs";

    public String getData(String name,boolean isCurrentWeek) throws InterruptedException {
        var characters = new ArrayList<CharacterModel>();
        String[] nameArray = name.split("\\s+");
        List<String> nameList = new ArrayList<>(Arrays.asList(nameArray));
        for (String s : nameList) {
            characters.addAll(getEntity(s,isCurrentWeek));
            Thread.sleep(1000);
        }
        return new Gson().toJson(characters);
    }

    public List<CharacterModel> getEntity(String charName, boolean currentWeek) {
        return buildModel(charName, currentWeek, getApiResponse(charName, currentWeek));
    }
    private List<Response> getApiResponse(String charName, boolean currentWeek) {
        RestAssured.baseURI = "https://raider.io/api/v1/characters/profile";

        // Define the query parameters
        String region = "eu";
        String realm = "drak-thul";
        String backupRealm = "burning-blade";
        String name = charName;
        String fields = currentWeek ? currentWeekField : previousWeekField;
        var responseList = new ArrayList<Response>();
        // Make the API request
        responseList.add(RestAssured.given()
                .queryParam("region", region)
                .queryParam("realm", realm)
                .queryParam("name", name)
                .queryParam("fields", fields)
                .get());
        responseList.add(RestAssured.given()
                .queryParam("region", region)
                .queryParam("realm", backupRealm)
                .queryParam("name", name)
                .queryParam("fields", fields)
                .get());
        return responseList;
    }

    private List<CharacterModel> buildModel(String charName, boolean currentWeek, List<Response> responses) {
        var characters = new ArrayList<CharacterModel>();
        for (Response r : responses) {
            if (r.getStatusCode() == 200) {
                // Print the response
                System.out.println("Response Code: " + r.getStatusCode());
                System.out.println("Response Body: " + r.getBody().asString());
                JsonPath jsonPath = new JsonPath(r.getBody().asString());
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
                // Get the top 8 values
                List<DungeonModel> top8Dungeons = finishedDungeons.stream()
                        .sorted(Comparator.comparingInt(DungeonModel::getFinishedKeyLevel).reversed())
                        .toList().stream()
                        .limit(8)
                        .toList();
                var character = CharacterModel.builder().name(name).realm(characterRealm).characterClass(characterClass).dungeons(finishedDungeons.stream()
                        .sorted(Comparator.comparingInt(DungeonModel::getFinishedKeyLevel).reversed())
                        .toList().stream()
                        .limit(8)
                        .toList()).score(utils.countDungeonScore(top8Dungeons)).build();
                characters.add(character);
            }
        }
        return characters;

    }
}
