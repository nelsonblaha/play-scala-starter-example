package services

import java.util.Locale

import scala.concurrent.Future
import scala.util.{Failure, Success}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import plugins.ServicesModule

class ComponentSpec extends PlaySpec with GuiceOneAppPerSuite {
//class ComponentSpec extends PlaySpec with ServicesSpec with GuiceOneAppPerSuite {

  val environment = app.environment.asJava
  val servicesModule: ServicesModule = app.injector.instanceOf[ServicesModule]

//  val channels = services.channels

  implicit val locale = Locale.US // For methods returning localized fields (i.e.: #Channel.name())

  "Tests" when {

    "injecting services" in {
      true must be(true)
    }

  }

}
