package onextent.akka.demo.sangria

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.typesafe.scalalogging.LazyLogging
import onextent.akka.demo.sangria.http.{HttpSupport, StarwarsRoute}

object Main extends App with LazyLogging with HttpSupport with Directives {
  implicit val system: ActorSystem = ActorSystem("sangria-server")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  import system.dispatcher

  val route: Route =
    logRequest(urlpath) {
      //handleErrors {
        cors(corsSettings) {
          StarwarsRoute()
        }
      //}
    }

  Http().bindAndHandle(route, "0.0.0.0", port)
}
