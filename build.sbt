import sbt._
import util.matching.Regex._

resolvers += Resolver.sonatypeRepo("public")
resolvers += Resolver.sonatypeRepo("releases")
resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"

name := "VeriTRAN"

version := "0.3"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.3"

libraryDependencies += "org.scalaz.stream" %% "scalaz-stream" % "0.8"

libraryDependencies += "org.scala-stm" %% "scala-stm" % "0.7"

val monocleLibraryVersion = "1.2.0-M1"

libraryDependencies ++= Seq(
  "com.github.julien-truffaut"  %%  "monocle-core"    % monocleLibraryVersion,
  "com.github.julien-truffaut"  %%  "monocle-generic" % monocleLibraryVersion,
  "com.github.julien-truffaut"  %%  "monocle-macro"   % monocleLibraryVersion,
  "com.github.julien-truffaut"  %%  "monocle-state"   % monocleLibraryVersion,
  "com.github.julien-truffaut"  %%  "monocle-law"     % monocleLibraryVersion % "test"
)

libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.4"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"

libraryDependencies += "me.lessis" %% "lapse" % "0.1.0"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")

triggeredMessage := Watched.clearWhenTriggered

maxErrors := 10