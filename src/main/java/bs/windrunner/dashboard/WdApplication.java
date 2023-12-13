package bs.windrunner.dashboard;

import bs.windrunner.dashboard.raiderioapi.RaiderIoApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class WdApplication {
    public static void main(String[] args) {
        SpringApplication.run(WdApplication.class, args);
    }

}
