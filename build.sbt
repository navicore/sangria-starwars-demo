name := "SangriaStarwarsDemo"

fork := true
javaOptions in test ++= Seq(
  "-Xms512M", "-Xmx2048M",
  "-XX:MaxPermSize=2048M",
  "-XX:+CMSClassUnloadingEnabled"
)

parallelExecution in test := false

version := "1.0"

scalaVersion := "2.12.4"
val akkaVersion = "2.5.6"
val akkaHttpVersion = "10.0.10"

libraryDependencies ++=
  Seq(
    "ch.megard" %% "akka-http-cors" % "0.2.1",

    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "com.typesafe" % "config" % "1.2.1",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",

    "org.sangria-graphql" %% "sangria" % "1.3.2",
    "org.sangria-graphql" %% "sangria-spray-json" % "1.0.0",

    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,

    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )

dependencyOverrides ++= Seq(
  "com.typesafe.akka" %% "akka-actor"  % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion
)

mainClass in assembly := Some("onextent.akka.demo.sangria.Main")
assemblyJarName in assembly := "SangriaStarwarsDemo.jar"

