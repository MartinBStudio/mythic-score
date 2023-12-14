package bs.windrunner.dashboard.service.utils;

import bs.windrunner.dashboard.model.DungeonModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class Utils {

    public int countDungeonScore(List<DungeonModel> dungeons) {
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
