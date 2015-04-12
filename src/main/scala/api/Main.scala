package api

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

object Main extends App {

  implicit val system = ActorSystem()

  val handler = system.actorOf(Props[Api], name = "server")

  IO(Http) ! Http.Bind(handler, interface = "0.0.0.0", port = 9090)

}