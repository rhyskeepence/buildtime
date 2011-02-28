import actors.Actor
import io.Source
import java.io.IOException
import java.net.{Socket, ServerSocket}
import com.mongodb.casbah.Imports._
import java.util.Date
object BuildTimerServer extends Application {
  val port = 10999

  try {
    val listener = new ServerSocket(port)
    val persistence = new BuildTimePersistence("localhost")

    println("BuildTimerServer listening on port " + port)

    while (true) {
      new ConnectionHandler(listener.accept(), persistence).start()
    }

    listener.close()

  } catch {
    case e: IOException =>
      System.err.println("Could not listen on port " + port)
  }
}

class ConnectionHandler(socket: Socket, persistence: BuildTimePersistence) extends Actor {
  val buildLogPattern = """user:(\w+) host:(\w+) directory:([\w\/]+) command:'(.*)' time:(\d+) processor:([\d\.]+)% memory:(\d+) input:(\d+) output:(\d+) received:(\d+) sent:(\d+) waits:(\d+) os:(\w+) cpu:([\w\d]+) ncpu:(\d+)""".r

  def act {
    try {
      println("Client connected from " + socket.getInetAddress())
      val input = Source.fromInputStream(socket.getInputStream).getLines.next.trim

      input match {
        case buildLogPattern(user, host, directory, command, time, processor, memory, input, output, received, sent, waits, os, cpu, ncpu) =>
          persistence.writeBuildTime(user, host, directory, command, time.toInt)
        case _ =>
          println("got some funny request: " + input)
      }

    } catch {
      case e =>
        e.printStackTrace

    } finally {
      socket.close()
    }
  }
}

class BuildTimePersistence(host: String) {

  val mongo = MongoConnection(host)
  val builds = mongo("buildtime")("builds")

  def writeBuildTime(user: String, host: String, directory: String, command: String, elapsedTime: Int) {

    val builder = MongoDBObject.newBuilder
    builder += "user" -> user
    builder += "host" -> host
    builder += "directory" -> directory
    builder += "command" -> command
    builder += "elapsedTime" -> elapsedTime
    builder += "timestamp" -> new Date

    builds += builder.result.asDBObject

  }

}
