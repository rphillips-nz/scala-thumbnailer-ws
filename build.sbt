name := "scala-thumbnailer-ws"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "nz.co.rossphillips" % "scala-thumbnailer" % "0.4.0"
)     

play.Project.playScalaSettings
