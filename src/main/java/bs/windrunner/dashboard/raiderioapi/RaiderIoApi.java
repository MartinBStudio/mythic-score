package bs.windrunner.dashboard.raiderioapi;

import bs.windrunner.dashboard.model.CharacterModel;
import bs.windrunner.dashboard.model.DungeonModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RaiderIoApi {

    public CharacterModel getEntity(String charName) {
        return buildModel(charName, getApiResponse(charName));
    }

    public CharacterModel getFakeEntity(String charName) {
        return buildFakeModel(charName);
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

    private CharacterModel buildModel(String charName, Response response) {
        if (response.getStatusCode() == 200) {
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
            List<DungeonModel> sortedDungeons = finishedDungeons.stream()
                    .sorted(Comparator.comparingInt(DungeonModel::getFinishedKeyLevel).reversed())
                    .toList();
            // Get the top 8 values
            List<DungeonModel> top8Dungeons = sortedDungeons.stream()
                    .limit(8)
                    .toList();
            return CharacterModel.builder().name(name).dungeons(top8Dungeons).score(countDungeonScore(top8Dungeons)).build();
        } else {
            return CharacterModel.builder().name(charName + " character not found on Raider.io").dungeons(List.of()).build();
        }

    }

    public CharacterModel buildFakeModel(String charName) {
        var finishedDungeons = new ArrayList<DungeonModel>();
        for (int i = 0; i < 8; i++) {
            String dungeonName = "FakeDungeon";
            String shortName = "FD";
            int level = i == 0 || i == 1 || i == 2 || i == 3 || i == 4 ? 20 : 2;
            finishedDungeons.add(DungeonModel.builder().shortName(shortName).name(dungeonName).finishedKeyLevel(level).build());
        }
        List<DungeonModel> sortedDungeons = finishedDungeons.stream()
                .sorted(Comparator.comparingInt(DungeonModel::getFinishedKeyLevel).reversed())
                .toList();
        // Get the top 8 values
        List<DungeonModel> top8Dungeons = sortedDungeons.stream()
                .limit(8)
                .toList();
        return CharacterModel.builder().name(charName).dungeons(top8Dungeons).score(countDungeonScore(top8Dungeons)).build();
    }

    private int countDungeonScore(List<DungeonModel> dungeons) {
        int biggerOrEqualTwenty = 0;
        int betweenEighteenAndTwenty = 0;
        int betweenTenAndSeventeen = 0;
        int betweenTwoAndNine = 0;
        // Sorting the list in descending order based on finishedKeyLevel

        for (DungeonModel d : dungeons) {
            var keyLevel = d.getFinishedKeyLevel();

            if (keyLevel >= 20) {
                biggerOrEqualTwenty++;
            } else if (keyLevel >= 18) {
                betweenEighteenAndTwenty++;
            } else if (keyLevel >= 10) {
                betweenTenAndSeventeen++;
            } else if (keyLevel >= 2) {
                betweenTwoAndNine++;
            }
        }

        var sumCore = 0;

        //20
        log.info("20:{}", biggerOrEqualTwenty);
        var twentyScore = 0;
        if (biggerOrEqualTwenty >= 8) {
            twentyScore = 120;
        } else if (biggerOrEqualTwenty >= 4) {
            twentyScore = 80;
        } else if (biggerOrEqualTwenty >= 1) {
            twentyScore = 40;
        }
        log.info("20 - count {} score:{}", biggerOrEqualTwenty, twentyScore);
        sumCore += twentyScore;
        //18
        var eighteenScore = 0;
        if (betweenEighteenAndTwenty >= 8) {
            eighteenScore += 90;
        } else if (betweenEighteenAndTwenty >= 4) {
            eighteenScore += 60;
        } else if (betweenEighteenAndTwenty >= 1) {
            eighteenScore += 30;
        }
        log.info("18-19 - count {} score:{}", betweenEighteenAndTwenty, eighteenScore);
        sumCore += eighteenScore;
        //10
        var tenScore = 0;
        if (betweenTenAndSeventeen >= 8) {
            tenScore += 60;
        } else if (betweenTenAndSeventeen >= 4) {
            tenScore += 40;
        } else if (betweenTenAndSeventeen >= 1) {
            tenScore += 20;
        }
        log.info("10-17 - count {} score:{}", betweenTenAndSeventeen, tenScore);
        sumCore += tenScore;
        //2
        var twoScore = 0;
        if (betweenTwoAndNine >= 8) {
            twoScore += 30;
        } else if (betweenTwoAndNine >= 4) {
            twoScore += 20;
        } else if (betweenTwoAndNine >= 1) {
            twoScore += 10;
        }
        log.info("2-9 - count {} score:{}", betweenTwoAndNine, twoScore);
        sumCore += twoScore;
        return sumCore;
    }

}