package uk.co.automatictester.gatling;

import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;

public class Engine {

    public static void main(String[] args) {
        GatlingPropertiesBuilder props = new GatlingPropertiesBuilder()
                .resourcesDirectory(IDEPathHelper.mavenResourcesDirectory.toString())
                .resultsDirectory(IDEPathHelper.resultsDirectory.toString())
                .binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString())
                .simulationClass("uk.co.automatictester.gatling.computerdatabase.BasicSimulation");
        Gatling.fromMap(props.build());
    }
}
