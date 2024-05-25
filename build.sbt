ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.0",
  "dev.zio" %% "zio-test" % "2.0.0" % Test,
  "dev.zio" %% "zio-test-sbt" % "2.0.0" % Test,
  "net.java.dev.jna" % "jna" % "5.14.0"
)

lazy val root = (project in file("."))
  .settings(
    name := "zio2-linux-template",
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
