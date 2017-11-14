package services

import com.typesafe.config.{Config, ConfigValue}
import java.awt.Color
import java.time.{Instant, Duration => JDuration}
import java.util.{Locale, Map => JMap}
import scala.concurrent.duration._
import scala.util.Try
import akka.http.scaladsl.model.Uri

trait Configured {
  import scala.collection.JavaConversions._
  import Configured._

  def configuredStringList(key: String)(implicit config: Config): List[String] =
      if (config.hasPath(key)) config.getStringList(key).toList else Nil

  def configuredStringOption(key: String)(implicit config: Config): Option[String] =
      if (config.hasPath(key)) Some(config.getString(key)) else None

  def configuredIntList(key: String)(implicit config: Config): List[Int] =
      if (config.hasPath(key)) config.getIntList(key).toList.map(_.intValue) else Nil

  def configuredIntOption(key: String)(implicit config: Config): Option[Int] =
      if (config.hasPath(key)) Some(config.getInt(key)) else None

  def configuredBooleanList(key: String)(implicit config: Config): List[Boolean] =
      if (config.hasPath(key)) config.getBooleanList(key).toList.map(_.booleanValue) else Nil

  def configuredBooleanOption(key: String)(implicit config: Config): Option[Boolean] =
      if (config.hasPath(key)) Some(config.getBoolean(key)) else None

  def configuredConfigOption(key: String)(implicit config: Config): Option[Config] =
      if (config.hasPath(key)) Some(config.getConfig(key)) else None

  def configuredConfigList(key: String)(implicit config: Config): List[Config] =
      if (config.hasPath(key)) config.getConfigList(key).toList else Nil

  def configuredLocale(key: String)(implicit config: Config): Option[Locale] =
      if (config.hasPath(key)) Some(Locale.forLanguageTag(config.getString(key))) else None

  def configuredLocales(key: String)(implicit config: Config): List[Locale] =
      configuredObjectKeys(key) map Locale.forLanguageTag

  def configuredColorOption(key: String)(implicit config: Config): Option[Color] =
      if (config.hasPath(key)) Try(Color.decode(config.getString(key))).toOption else None

  def configuredUri(key: String)(implicit config: Config): Uri =
      if (config.hasPath(key)) Uri(config.getString(key)) else Uri("")

  def configuredUriOption(key: String)(implicit config: Config): Option[Uri] =
      if (config.hasPath(key)) Some(configuredUri(key)) else None

  def configuredDateOption(key: String)(implicit config: Config): Option[Instant] =
    if (config.hasPath(key)) Some(Instant.parse(config.getString(key))) else None

  def configuredDuration(key: String)(implicit config: Config): FiniteDuration =
      if (config.hasPath(key)) config.getDuration(key): FiniteDuration else Duration.Zero

  def configuredDurationOption(key: String)(implicit config: Config): Option[FiniteDuration] = 
      if (config.hasPath(key)) Some(configuredDuration(key)) else None

  def configuredStringMap(key: String)(implicit config: Config): Map[String, String] = {
    if (config.hasPath(key)) {
      val tree: Map[String, AnyRef] = mapAsScalaMap(config.getConfig(key).root.unwrapped).toMap
      tree.foldLeft(Map.empty[String, String]) { case (map, (key, value)) =>
        map + (key -> value.toString)
      }
    }
    else Map.empty
  }

  def configuredObjectKeys(key: String)(implicit config: Config): List[String] = {
    if (config.hasPath(key)) mapAsScalaMap(config.getConfig(key).root.unwrapped).toMap.keys.toList
    else Nil
  }
}


object Configured {
  implicit def javaDurationToScala(d: JDuration): FiniteDuration =
      Seq(
        d.toDays.days,
        d.toHours.hours,
        d.toMinutes.minutes,
        (d.toMillis / 1000).seconds,
        d.toMillis.millis
      ) find (_.length != 0L) match {
        case Some(fd) => fd
        case None     => Duration.Zero
      }


  object ConfigHelper extends Configured
}