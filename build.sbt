name := """play-scala-starter-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.h2database" % "h2" % "1.4.196"
libraryDependencies += "org.scalatest"                 %% "scalatest"                    % "3.0.4"         % Test
libraryDependencies += "org.scalatestplus.play"        %% "scalatestplus-play"          % "3.1.2"         % Test
libraryDependencies += "com.typesafe.scala-logging"   %% "scala-logging"        % "3.5.0"