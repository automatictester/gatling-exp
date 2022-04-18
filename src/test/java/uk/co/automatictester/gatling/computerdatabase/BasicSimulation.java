package uk.co.automatictester.gatling.computerdatabase;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static uk.co.automatictester.gatling.computerdatabase.ProtocolBuilderFactory.getHttpProtocol;
import static uk.co.automatictester.gatling.computerdatabase.Steps.*;

public class BasicSimulation extends Simulation {

    private final HttpProtocolBuilder http = getHttpProtocol();

    private final ScenarioBuilder searchScn = scenario("search")
            .exec(
                    home(),
                    search()
            );

    private final ScenarioBuilder browseScn = scenario("browse")
            .exec(
                    home(),
                    browse(3)
            );

    private final ScenarioBuilder addScn = scenario("add")
            .exec(
                    home(),
                    add()
            );

    {
        setUp(
                searchScn.injectClosed(
                        rampConcurrentUsers(0).to(10).during(10),
                        constantConcurrentUsers(10).during(30)
                ),
                browseScn.injectClosed(
                        rampConcurrentUsers(0).to(10).during(10),
                        constantConcurrentUsers(10).during(30)
                ),
                addScn.injectClosed(
                        rampConcurrentUsers(0).to(2).during(10),
                        constantConcurrentUsers(2).during(30)
                )
        ).protocols(http)
                .assertions(
                        global().successfulRequests().percent().is(100.0),
                        details("home").responseTime().percentile3().lte(2000),
                        details("search").responseTime().percentile3().lte(300),
                        details("browse 1").responseTime().percentile3().lte(300),
                        details("browse 2").responseTime().percentile3().lte(300),
                        details("browse 3").responseTime().percentile3().lte(300),
                        details("search").responseTime().percentile3().lte(300),
                        details("add").responseTime().percentile3().lte(300),
                        details("post").responseTime().percentile3().lte(300)
                );
    }
}
