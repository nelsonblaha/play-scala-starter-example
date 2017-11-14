package plugins

import javax.inject.{Inject, Singleton}
import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.StrictLogging
import play.api.libs.concurrent.{ActorSystemProvider, Execution}
import play.api.{Application, Configuration, DefaultApplication}
import play.Environment
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}
import services._
import controllers.Assets
import play.api.http._
import play.api.inject._
import play.api.libs.Files.{DefaultTemporaryFileCreator, TemporaryFileCreator}
import play.api.routing.Router

@Singleton
class ServicesModule @Inject() (val environment: Environment, val config: Config, ec: ExecutionContext, actorSystem: ActorSystem) extends Module with StrictLogging with Configured {

  implicit val implicitConfig: Config = config

  def bindings(env: play.api.Environment, configuration: Configuration): Seq[Binding[_]] = {
    def dynamicBindings(factories: ((play.api.Environment, Configuration) => Seq[Binding[_]])*) = {
      factories.flatMap(_(env, configuration))
    }

    Seq(
      bind[Configuration].toProvider[ConfigurationProvider],
      bind[HttpConfiguration].toProvider[HttpConfiguration.HttpConfigurationProvider],

      // Application lifecycle, bound both to the interface, and its implementation, so that Application can access it
      // to shut it down.
      bind[DefaultApplicationLifecycle].toSelf,
      bind[ApplicationLifecycle].to(bind[DefaultApplicationLifecycle]),
      bind[Application].to[DefaultApplication],
      bind[play.Application].to[play.DefaultApplication],
      bind[Router].toProvider[RoutesProvider],
      bind[ActorSystem].toProvider[ActorSystemProvider],
      bind[TemporaryFileCreator].to[DefaultTemporaryFileCreator]
    ) ++ dynamicBindings(
      HttpErrorHandler.bindingsFromConfiguration,
      HttpFilters.bindingsFromConfiguration,
      HttpRequestHandler.bindingsFromConfiguration
    )
  }

  val enabled = true
  
}
