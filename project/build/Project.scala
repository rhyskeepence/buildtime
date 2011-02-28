import sbt._
class build(info: ProjectInfo) extends DefaultProject(info) {

  val casbah = "com.mongodb.casbah" %% "casbah" % "2.0.1"

}