lazy val root = (project in file(".")).
  settings(
    organization := "com.learningobjects",
    name := "casal",
    version := "0.1.2",
    scalaVersion := "2.11.2"
  )

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"

libraryDependencies += "net.liftweb" %% "lift-json" % "2.6"

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
