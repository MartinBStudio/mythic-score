package bs.windrunner.dashboard.model;

import lombok.Getter;

public enum PredefinedCharTypes {
    ALL("WIND_RUNNER_ALL"), R1("WIND_RUNNER_R1"), R2("WIND_RUNNER_R2");
    @Getter
    private String envVariable;

    PredefinedCharTypes(String envVariable) {
        this.envVariable = envVariable;
    }
}
