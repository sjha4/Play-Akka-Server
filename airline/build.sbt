name := """Airline"""
organization := "airline"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)

scalaVersion := "2.12.2"

libraryDependencies += javaJdbc
libraryDependencies += guice
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.20.0"