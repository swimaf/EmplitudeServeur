name := """emplitude"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  jdbc,
  evolutions
)
libraryDependencies +="com.typesafe.play" %% "play-mailer" % "5.0.0-M1"
libraryDependencies += "postgresql" % "postgresql" % "9.1-901-1.jdbc4"

fork in run := true

