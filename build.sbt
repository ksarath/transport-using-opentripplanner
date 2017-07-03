name            := "transportation-using-opentripplanner"

version         := "1.0.0-beam-SNAPSHOT"

scalaVersion    := "2.11.11"

val akkaV       = "2.5.2"

val akkaHttpV   = "10.0.9"

resolvers ++= Seq(
  "jitpack" at "https://jitpack.io",
  "osgeo" at "http://download.osgeo.org/webdav/geotools/",
  "opengeo" at "http://repo.opengeo.org",
  "onebusaway" at "http://nexus.onebusaway.org/content/repositories/releases/",
  "onebusawaysnaps" at "http://nexus.onebusaway.org/content/repositories/snapshots/",
  "conveyal" at "https://maven.conveyal.com/",
  "graphql" at "http://dl.bintray.com/andimarek/graphql-java"
)

libraryDependencies ++= Seq(
  "com.github.colinsheppard"          %  "OpenTripPlanner"        % "otp-1.0.0-beam-SNAPSHOT",
  "com.typesafe.akka"                 %% "akka-actor"             % akkaV,
  "com.typesafe.akka"                 %% "akka-http"              % akkaHttpV,
  "com.typesafe.akka"                 %% "akka-http-spray-json"   % akkaHttpV,
  "com.typesafe.akka"                 %% "akka-testkit"           % akkaV           % "test",
  "com.typesafe.akka"                 %% "akka-http-testkit"      % akkaHttpV       % "test",
  "org.scalatest"                     %% "scalatest"              % "3.0.1"         % "test"
)
