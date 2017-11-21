package onextent.akka.demo.sangria

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import onextent.akka.demo.sangria.models.{JsonSupport, Message}
import onextent.akka.demo.sangria.routes.{StarwarsRoute, StarwarsSegmentRoute}
import scala.concurrent.ExecutionContextExecutor

object Main extends LazyLogging with JsonSupport with ErrorSupport {

  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("SangriaStarwarsDemo-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val route =
      HealthCheck ~
      StarwarsRoute.apply ~
      StarwarsSegmentRoute.apply

    Http().bindAndHandle(route, "0.0.0.0", port)
  }
}

