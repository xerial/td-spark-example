import sbt.Keys.version

val SCALA_2_11       = "2.11.12"
// Note: Spark 2.4.x requires Java8
val SPARK_VERSION    = "2.4.5"

scalaVersion in ThisBuild := SCALA_2_11

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val example =
  project
    .in(file("."))
    .enablePlugins(PackPlugin)
    .settings(
      name := "td-spark-example",
      version := "0.1-SNAPSHOT",
      libraryDependencies ++= Seq(
        "org.apache.spark"   %% "spark-core"   % SPARK_VERSION,
        "org.apache.spark"   %% "spark-sql"    % SPARK_VERSION
      ),
      packResourceDir := Map(file("lib") -> "lib"),
      packMain := Map("td-spark-example"     -> "example.TDSparkExample"),
      test in assembly := {},
      assemblyJarName in assembly := s"${name.value}-${version.value}-assembly.jar",
      assemblyOption in assembly := (assemblyOption in assembly).value
        .copy(includeScala = false),
      assemblyMergeStrategy in assembly := {
        case PathList("META-INF", xs @ _*) => MergeStrategy.discard
        case x                             => MergeStrategy.first
      }
    )
