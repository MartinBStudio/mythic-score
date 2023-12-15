package bs.windrunner.dashboard;

import bs.windrunner.dashboard.model.DungeonModel;
import bs.windrunner.dashboard.service.utils.Utils;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@SpringBootTest
@Slf4j
class WindRunnerTests extends AbstractTestNGSpringContextTests {
    @Autowired
    Utils utils;

    @Test
    public void namesFormatter() {
        final var names = "Petr Martin Libor";
        var formattedNames = utils.formatNamesParam(names);
        List<String> expectedList = Arrays.asList("Petr", "Martin", "Libor");

        // Check if the formattedNames list contains all values from names as single objects
        assertTrue(formattedNames.containsAll(expectedList));
        // You can also check the size if needed
        assertEquals(expectedList.size(), formattedNames.size());
    }

    @Test
    public void numbers() {
        List<Integer> numberList = List.of(1, 8, 6, 23, 15, 80);
        assertEquals(80, utils.findHighestNumber(numberList));
        assertEquals(1, utils.findLowestNumber(numberList));
    }

    @Test
    public void points() {
        int[] points = {22, 19, 14, 4, 1};
        int[] expectedResults = {40, 30, 20, 10, 0};

        for (int i = 0; i < points.length; i++) {
            int result = utils.countPointsForNumber(points[i]);
            assertEquals(expectedResults[i], result);
        }
    }
    @Test
    public void tiersScoreCalculation() {
        List<Integer> points = List.of(22, 19, 14, 4, 1,2,8,6,5,36,11);
        var dungeons = new ArrayList<DungeonModel>();
        for(Integer i:points){
            dungeons.add(DungeonModel.builder().finishedKeyLevel(i).build());
        }
        var tierOneResult = utils.countTierOne(dungeons);
        assertEquals(40,tierOneResult);
        var tierFourResult = utils.countTierFour(dungeons);
        assertEquals(20,tierFourResult);
        var tierEightResult = utils.countTierEight(dungeons);
        assertEquals(10,tierEightResult);
    }


}
