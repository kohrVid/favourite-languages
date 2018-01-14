name := "Favourite Languages"

organization := "kohrVid"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.2"

resolvers += Resolver.bintrayRepo("findify", "maven")

libraryDependencies ++= Seq(
  "org.scalafx"   %% "scalafx"   % "8.0.102-R11",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "org.json4s" %% "json4s-native" % "3.5.0",
  "com.typesafe" %% "config" % "1.3.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test" //http://www.scalatest.org/download
)

shellPrompt := { state => System.getProperty("user.name") + "> " }

// set the main class for the main 'run' task
// change Compile to Test to set it for 'test:run'
mainClass in (Compile, run) := Some("favourite.languages.ScalaFXHelloWorld")


// Fork a new JVM for 'run' and 'test:run' to avoid JavaFX double initialization problems
fork := true
