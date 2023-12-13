package bs.windrunner.dashboard.raiderioapi;

import bs.windrunner.dashboard.model.CharacterModel;
import bs.windrunner.dashboard.model.DungeonModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RaiderIoApi {

    public CharacterModel getEntity(String charName) {
        return buildModel(charName,getApiResponse(charName));
    }

    private Response getApiResponse(String charName) {
        RestAssured.baseURI = "https://raider.io/api/v1/characters/profile";

        // Define the query parameters
        String region = "eu";
        String realm = "drak-thul";
        String name = charName;
        String fields = "mythic_plus_weekly_highest_level_runs";

        // Make the API request
        return RestAssured.given()
                .queryParam("region", region)
                .queryParam("realm", realm)
                .queryParam("name", name)
                .queryParam("fields", fields)
                .get();
    }

    private CharacterModel buildModel(String charName,Response response) {
        if(response.getStatusCode()==200){
            // Print the response
            System.out.println("Response Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody().asString());
            JsonPath jsonPath = new JsonPath(response.getBody().asString());
            var name = jsonPath.getString("name");
            var dungeons = jsonPath.getList("mythic_plus_weekly_highest_level_runs");
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
            return CharacterModel.builder().name(name).dungeons(finishedDungeons).build();
        }
        else{
            return CharacterModel.builder().name(charName + " character not found on Raider.io").dungeons(List.of()).build();
        }

    }
}