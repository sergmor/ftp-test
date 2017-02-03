name := """activator-ftp"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  // Uncomment to use Akka
  //"com.typesafe.akka" % "akka-actor_2.11" % "2.3.9",
  "commons-net" % "commons-net" % "3.5",
  "org.apache.commons" % "commons-vfs2" % "2.1",
  "com.jcraft" % "jsch" % "0.1.54",
  "commons-logging" % "commons-logging" % "1.2",
  "junit"             % "junit"           % "4.12"  % "test",
  "com.novocode"      % "junit-interface" % "0.11"  % "test"
)
