import com.github.retronym.OneJarProject
import sbt._

class build(info: ProjectInfo) extends DefaultProject(info) with OneJarProject {

  override def mainClass = Some("BuildTimerServer")

  val casbah = "com.mongodb.casbah" %% "casbah" % "2.0.1"

}