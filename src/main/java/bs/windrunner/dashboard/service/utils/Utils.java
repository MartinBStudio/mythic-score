package bs.windrunner.dashboard.service.utils;

import bs.windrunner.dashboard.model.CharacterModel;
import bs.windrunner.dashboard.model.DungeonModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class Utils {



    private int countTierOne(List<DungeonModel> dungeons) {
        if (dungeons.size() >= 1) {
            List<Integer> numbers = new ArrayList<>();
            for (DungeonModel d : dungeons) {
                numbers.add(d.getFinishedKeyLevel());
            }
            return countPointsForNumber(findHighestNumber(numbers));
        }
        return 0;
    }
    private int findLowestNumber(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }

        int min = numbers.get(0);
        for (int number : numbers) {
            if (number < min) {
                min = number;
            }
        }
        return min;
    }
    private  int findHighestNumber(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }

        int max = numbers.get(0);
        for (int number : numbers) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }
    private int countPointsForNumber(int number){

            if (number >= 20) {
                return 40;
            } else if (number >= 18) {
                return 30;
            } else if (number >= 10) {
                return 20;
            } else if (number >= 2) {
                return 10;
            }

        return 0;
    }

    private int countTierFour(List<DungeonModel> dungeons) {
        if (dungeons.size() >= 4) {
            List<Integer> numbers = new ArrayList<>();
            for (DungeonModel d : dungeons) {
                numbers.add(d.getFinishedKeyLevel());
            }
            Collections.sort(numbers,Collections.reverseOrder());
            List<Integer> fourHighestNumbers = numbers.subList(0, Math.min(numbers.size(), 4));
            return countPointsForNumber(findLowestNumber(fourHighestNumbers));
        }
        return 0;
    }

    private int countTierEight(List<DungeonModel> dungeons) {
        if (dungeons.size() >= 8) {
            List<Integer> numbers = new ArrayList<>();
            for (DungeonModel d : dungeons) {
                numbers.add(d.getFinishedKeyLevel());
            }
            Collections.sort(numbers,Collections.reverseOrder());
            List<Integer> fourHighestNumbers = numbers.subList(0, Math.min(numbers.size(), 8));
            Collections.sort(fourHighestNumbers);
            return countPointsForNumber(findLowestNumber(fourHighestNumbers));
        }
        return 0;
    }

    public List<String> formatNamesParam(String names) {
        String[] nameArray = names.split("\\s+");
        return new ArrayList<>(Arrays.asList(nameArray));
    }

    public CharacterModel buildCharacter(List<DungeonModel> finishedDungeons, String name, String characterRealm, String characterClass) {
        List<DungeonModel> top8Dungeons = finishedDungeons.stream()
                .sorted(Comparator.comparingInt(DungeonModel::getFinishedKeyLevel).reversed())
                .toList().stream()
                .limit(8)
                .toList();

        var tierOne = countTierOne(top8Dungeons);
        var tierFour = countTierFour(top8Dungeons);
        var tierEight = countTierEight(top8Dungeons);
        var total = tierOne + tierFour + tierEight;


        return CharacterModel.builder()
                .name(name)
                .realm(characterRealm)
                .characterClass(characterClass)
                .dungeons(top8Dungeons)
                .tierOneScore(tierOne)
                .tierFourScore(tierFour)
                .tierEightScore(tierEight)
                .totalScore(total)
                .build();
    }
}
