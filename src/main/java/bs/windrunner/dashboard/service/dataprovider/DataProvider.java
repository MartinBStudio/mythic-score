package bs.windrunner.dashboard.service.dataprovider;

import bs.windrunner.dashboard.service.dataprovider.components.DummyDataComponent;
import bs.windrunner.dashboard.service.dataprovider.components.RaiderIoComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataProvider {
    private final DummyDataComponent dummyDataComponent;
    private final RaiderIoComponent raiderIoComponent;

    public String provedeJsonStringData(String name, boolean isDummyMode, boolean isCurrentWeek) throws InterruptedException {
        return !isDummyMode?raiderIoComponent.getData(name, isCurrentWeek): dummyDataComponent.getData(name);
    }

}