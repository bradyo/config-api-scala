organization  := "config"

version       := "0.1"

scalaVersion  := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaVersion = "2.3.6"
  val sprayVersion = "1.3.2"
  Seq(
    "io.spray"            %%  "spray-can"             % sprayVersion,
    "io.spray"            %%  "spray-routing"         % sprayVersion,
    "io.spray"            %%  "spray-json"            % "1.3.1",
    "io.spray"            %%  "spray-testkit"         % sprayVersion    % "test",
    "com.typesafe.akka"   %%  "akka-actor"            % akkaVersion,
    "com.typesafe.akka"   %%  "akka-testkit"          % akkaVersion     % "test",
    "com.h2database" % "h2" % "1.4.186",
    "org.scalikejdbc" %% "scalikejdbc" % "2.2.5",
    "org.scalikejdbc" %% "scalikejdbc-config"  % "2.2.5",
    "org.scalikejdbc" %% "scalikejdbc-test" % "2.2.5" % "test"
  )
}


