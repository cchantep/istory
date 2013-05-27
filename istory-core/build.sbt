name := "istory-core"

organization := "istory"

version := "1.1-SNAPSHOT"

//scalaVersion := "2.10.0"

scalacOptions += "-feature"

autoScalaLibrary := false

//autoScalaLibrary in Test := true

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

//testOptions in Test += Tests.Argument("junitxml", "console")

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.1"

libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test->default"

libraryDependencies += "junit" % "junit" % "4.11" % "test"

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

//pomPostProcess := { (pom: scala.xml.Node) =>
//  scala.xml.Node.unapplySeq(pom) match {
//    case Some((label, attrs, child)) =>
//      scala.xml.Elem(null, label, attrs, scala.xml.TopScope, 
//        child.map(n => n.label match {
//          case "groupId" => <groupId>fr.applicius.kyub</groupId>
//          case "artifactId" => <artifactId>java-client</artifactId>
//          case _ => n
//        }):_*)
//    case _ => sys.error("Invalid POM")
//  }
//}
