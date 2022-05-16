package uk.co.automatictester.gatling.computerdatabase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Steps {

    public static ChainBuilder home() {
        return exec(http("home")
                .get("/computers"))
                .pause(3, 7);
    }

    public static ChainBuilder search() {
        FeederBuilder<String> searchFeeder = csv("search.csv").random();

        return feed(searchFeeder)
                .exec(http("search")
                        .get("/computers?f=#{searchCriterion}"))
                .pause(3, 7);
    }

    public static ChainBuilder browse(int i) {
        if (i < 1 || i > 3) {
            throw new IllegalArgumentException("Index out of range");
        }

        List<Integer> pageIds = Stream.iterate(1, x -> x + 1)
                .limit(i)
                .collect(Collectors.toList());

        return foreach(pageIds, "n").on(
                exec(http("browse #{n}")
                        .get("/computers?p=#{n}&n=10&s=name&d=asc"))
                        .pause(2, 4));
    }

    public static ChainBuilder add() {
        return exec(http("add")
                .get("/computers/new"))
                .pause(6, 10)
                .exec(http("post")
                        .post("/computers")
                        .formParam("name", "my macbook")
                        .formParam("introduced", "2022-04-16")
                        .formParam("discontinued", "2022-04-18")
                        .formParam("company", "1")
                )
                .pause(2, 4);
    }
}
