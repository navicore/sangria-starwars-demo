package onextent.akka.demo.sangria.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server._
import com.typesafe.scalalogging.LazyLogging
import onextent.akka.demo.sangria.models.{CharacterRepo, SchemaDefinition}
import sangria.execution.deferred.DeferredResolver
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.sprayJson._
import sangria.parser.QueryParser
import spray.json._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object StarwarsRoute extends LazyLogging with Directives with HttpSupport {

  def apply()(implicit ec: ExecutionContext): Route =
    (post & path(urlpath)) {
      entity(as[JsValue]) { requestJson ⇒
        val JsObject(fields) = requestJson

        val JsString(query) = fields("query")

        val operation = fields.get("operationName") collect {
          case JsString(op) ⇒ op
        }

        val vars = fields.get("variables") match {
          case Some(obj: JsObject) ⇒ obj
          case _ ⇒ JsObject.empty
        }

        QueryParser.parse(query) match {

          // query parsed successfully, time to execute it!
          case Success(queryAst) ⇒
            complete(
              Executor
                .execute(
                  SchemaDefinition.StarWarsSchema,
                  queryAst,
                  new CharacterRepo,
                  variables = vars,
                  operationName = operation,
                  deferredResolver =
                    DeferredResolver.fetchers(SchemaDefinition.characters)
                )
                .map(OK → _)
                .recover {
                  case error: QueryAnalysisError ⇒
                    BadRequest → error.resolveError
                  case error: ErrorWithResolver ⇒
                    InternalServerError → error.resolveError
                })

          // can't parse GraphQL query, return error
          case Failure(error) ⇒
            complete(BadRequest, JsObject("error" → JsString(error.getMessage)))
        }
      }
    } ~
      get {
        getFromResource("graphiql.html")
      }

}
