package commando

import akka.actor.Actor
import akka.actor._
import spray.can.Http
import spray.http._
import commando.web._
import commando.web.Method

abstract class Application extends Actor with RequestHandler {

  def receive = {
    case _: Http.Connected =>
      sender ! Http.Register(self)

    case request: HttpRequest =>
      sender ! handle(request)

    case Timedout =>
      sender ! HttpResponse(500, "Request has timed out...")
  }

  private def handle(httpRequest: HttpRequest): HttpResponse = {
    val request = adaptRequest(httpRequest)
    val response = handle(request)
    adaptResponse(response)
  }

  private def adaptRequest(httpRequest: HttpRequest): Request = {
    val method = httpRequest.method match {
      case HttpMethods.GET => Method.Get
      case HttpMethods.POST => Method.Post
      case HttpMethods.PUT => Method.Put
      case HttpMethods.DELETE => Method.Delete
      case _ => Method.Any
    }
    if (httpRequest.entity.isEmpty) {
      new EmptyRequest(method, httpRequest.uri, httpRequest.headers)
    } else {
      val content = httpRequest.entity.asString
      new ContentRequest(method, httpRequest.uri, httpRequest.headers, content)
    }
  }

  private def adaptResponse(response: Response): HttpResponse = {
    val status = StatusCode.int2StatusCode(response.status)
    val entity = response match {
      case x: EmptyResponse => HttpEntity.Empty
      case x: ContentResponse => HttpEntity(x.content)
    }
    HttpResponse(status, entity, response.headers, HttpProtocols.`HTTP/1.1`)
  }

  def handle(request: Request): Response

}
