ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "scala-playground"
  )

assemblyJarName in assembly := "lambda-scala-playground.jar"
test in assembly := {}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _ @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}


val awsLambdaVersion = "1.2.1"
val awsLambdaEventsVersion = "3.11.0"
val circeVersion = "0.14.2"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-core" % awsLambdaVersion,
  "com.amazonaws" % "aws-lambda-java-events" % awsLambdaEventsVersion,
  "com.couchbase.client" %% "scala-client" % "1.3.1",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scalatest" %% "scalatest" % "3.2.12" % Test,
)
