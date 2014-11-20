name := "api-scraper"

version := "1.0"

scalaVersion := "2.11.4"

mainClass in Compile := Some("MainApp")

resolvers ++= Seq(
  "CB Central Mirror" at "http://repo.cloudbees.com/content/groups/public",
  "Apache" at "https://repository.apache.org/content/repositories/snapshots/",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= {
  Seq(
    "mysql" % "mysql-connector-java" % "5.1.31" % "compile",
    "ch.qos.logback" % "logback-classic" % "1.0.6",
    "com.googlecode.scalascriptengine" % "scalascriptengine" % "1.3.9-2.11.0" % "compile",
    "com.typesafe.akka" % "akka-actor_2.11" % "2.3.7" % "compile",
    "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.2" % "compile",
    "commons-logging" % "commons-logging" % "1.2" % "compile",
    "org.apache.httpcomponents" % "httpclient" % "4.3.6" % "compile",
    "org.eclipse.persistence" % "eclipselink" % "2.5.2" % "compile",
    "org.eclipse.persistence" % "javax.persistence" % "2.1.0" % "compile"
  )
}