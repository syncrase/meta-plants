import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the ClassificationCronquist entity.
 */
class ClassificationCronquistGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://localhost:8081"""

    val httpConf = http
        .baseUrl(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")
        .silentResources // Silence all resources like css or css so they don't clutter the results

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authentication = Map(
        "Content-Type" -> """application/json""",
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "Authorization" -> "${access_token}"
    )

    val scn = scenario("Test the ClassificationCronquist entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))
        ).exitHereIfFailed
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authenticate")
        .headers(headers_http_authentication)
        .body(StringBody("""{"username":"admin", "password":"admin"}""")).asJson
        .check(header("Authorization").saveAs("access_token"))).exitHereIfFailed
        .pause(2)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get all classificationCronquists")
            .get("/services/plantsms/api/classification-cronquists")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new classificationCronquist")
            .post("/services/plantsms/api/classification-cronquists")
            .headers(headers_http_authenticated)
            .body(StringBody("""{
                "superRegne":"SAMPLE_TEXT"
                , "regne":"SAMPLE_TEXT"
                , "sousRegne":"SAMPLE_TEXT"
                , "rameau":"SAMPLE_TEXT"
                , "infraRegne":"SAMPLE_TEXT"
                , "superEmbranchement":"SAMPLE_TEXT"
                , "division":"SAMPLE_TEXT"
                , "sousEmbranchement":"SAMPLE_TEXT"
                , "infraEmbranchement":"SAMPLE_TEXT"
                , "microEmbranchement":"SAMPLE_TEXT"
                , "superClasse":"SAMPLE_TEXT"
                , "classe":"SAMPLE_TEXT"
                , "sousClasse":"SAMPLE_TEXT"
                , "infraClasse":"SAMPLE_TEXT"
                , "superOrdre":"SAMPLE_TEXT"
                , "ordre":"SAMPLE_TEXT"
                , "sousOrdre":"SAMPLE_TEXT"
                , "infraOrdre":"SAMPLE_TEXT"
                , "microOrdre":"SAMPLE_TEXT"
                , "superFamille":"SAMPLE_TEXT"
                , "famille":"SAMPLE_TEXT"
                , "sousFamille":"SAMPLE_TEXT"
                , "tribu":"SAMPLE_TEXT"
                , "sousTribu":"SAMPLE_TEXT"
                , "genre":"SAMPLE_TEXT"
                , "sousGenre":"SAMPLE_TEXT"
                , "section":"SAMPLE_TEXT"
                , "sousSection":"SAMPLE_TEXT"
                , "espece":"SAMPLE_TEXT"
                , "sousEspece":"SAMPLE_TEXT"
                , "variete":"SAMPLE_TEXT"
                , "sousVariete":"SAMPLE_TEXT"
                , "forme":"SAMPLE_TEXT"
                }""")).asJson
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_classificationCronquist_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created classificationCronquist")
                .get("/services/plantsms${new_classificationCronquist_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created classificationCronquist")
            .delete("/services/plantsms${new_classificationCronquist_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(Integer.getInteger("users", 100)) during (Integer.getInteger("ramp", 1) minutes))
    ).protocols(httpConf)
}
