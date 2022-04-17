package uk.co.automatictester.gatling.computerdatabase;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class BasicSimulation extends Simulation {

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://computer-database.gatling.io")
            .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*detectportal\\.firefox\\.com.*"))
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
            .acceptEncodingHeader("gzip, deflate")
            .acceptLanguageHeader("en")
            .doNotTrackHeader("1")
            .upgradeInsecureRequestsHeader("1")
            .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:99.0) Gecko/20100101 Firefox/99.0");

    private final ScenarioBuilder search = scenario("search")
            .exec(http("home")
                    .get("/computers")
            )
            .exitHereIfFailed()
            .pause(3, 7)
            .exec(http("search")
                    .get("/computers?f=macbook+pro")
            );

    private final ScenarioBuilder browse = scenario("browse")
            .exec(http("home")
                    .get("/computers")
            )
            .exitHereIfFailed()
            .pause(3, 7)
            .exec(http("browse 1")
                    .get("/computers?p=1&n=10&s=name&d=asc")
            )
            .exitHereIfFailed()
            .pause(2, 4)
            .exec(http("browse 2")
                    .get("/computers?p=2&n=10&s=name&d=asc")
            )
            .exitHereIfFailed()
            .pause(2, 4)
            .exec(http("browse3")
                    .get("/computers?p=3&n=10&s=name&d=asc")
            );

    private final ScenarioBuilder add = scenario("add")
            .exec(http("home")
                    .get("/computers")
            )
            .exitHereIfFailed()
            .pause(3, 7)
            .exec(http("add")
                    .get("/computers/new")
            )
            .exitHereIfFailed()
            .pause(6, 10)
            .exec(http("post")
                    .post("/computers")
                    .formParam("name", "my macbook")
                    .formParam("introduced", "2022-04-16")
                    .formParam("discontinued", "2022-04-17")
                    .formParam("company", "1")
            );

    {
        setUp(
                search.injectClosed(
                        rampConcurrentUsers(0).to(10).during(10),
                        constantConcurrentUsers(10).during(30)
                ),
                browse.injectClosed(
                        rampConcurrentUsers(0).to(10).during(10),
                        constantConcurrentUsers(10).during(30)
                ),
                add.injectClosed(
                        rampConcurrentUsers(0).to(2).during(10),
                        constantConcurrentUsers(2).during(30)
                )
        ).protocols(httpProtocol);
    }
}
