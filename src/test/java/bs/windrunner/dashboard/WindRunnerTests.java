package bs.windrunner.dashboard;

import groovy.util.logging.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest
@Slf4j
class WindRunnerTests extends AbstractTestNGSpringContextTests {

    @Test
    public void anyTest() {
        logger.info("Test");
    }

}
