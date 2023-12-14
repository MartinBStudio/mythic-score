package bs.windrunner.dashboard;

import bs.windrunner.dashboard.service.dataprovider.DataProvider;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest
@Slf4j
class WdApplicationTests extends AbstractTestNGSpringContextTests {
    @Autowired
    DataProvider dataProvider;

    @Test
    void test() {

    }


}
