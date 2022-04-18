package uk.co.automatictester.gatling.computerdatabase;

import io.gatling.javaapi.http.HttpProtocolBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static io.gatling.javaapi.core.CoreDsl.AllowList;
import static io.gatling.javaapi.core.CoreDsl.DenyList;
import static io.gatling.javaapi.http.HttpDsl.http;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProtocolBuilderFactory {

    public static HttpProtocolBuilder getHttpProtocol() {
        return http.baseUrl("https://computer-database.gatling.io")
                .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*detectportal\\.firefox\\.com.*"))
                .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .acceptEncodingHeader("gzip, deflate")
                .acceptLanguageHeader("en")
                .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:99.0) Gecko/20100101 Firefox/99.0");
    }
}
