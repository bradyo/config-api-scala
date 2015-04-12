package commando.web

import spray.http._


object Method extends Enumeration {
  type Method = Value
  val Any, Get, Post, Put, Delete = Value
}
import Method._

abstract class Request(
  val method: Method = Method.Get,
  val uri: Uri = Uri./,
  val headers: List[HttpHeader] = Nil)

class EmptyRequest(
  override val method: Method = Method.Get,
  override val uri: Uri = Uri./,
  override val headers: List[HttpHeader] = Nil
  ) extends Request

class ContentRequest(
  override val method: Method,
  override val uri: Uri,
  override val headers: List[HttpHeader],
  val content: String
  ) extends EmptyRequest

class MatchedRequest[T](matchedRoute: MatchedRoute[T]) extends Request


abstract class Response {
  def status: Int
  def headers: List[HttpHeader] = List.empty
}

abstract class EmptyResponse extends Response

abstract class ContentResponse extends Response {
  def content: String
}

class TextResponse(val statusIn: Integer, val contentIn: String) extends ContentResponse {
  override def status = statusIn
  override def headers = HttpHeaders.`Content-Type`(ContentTypes.`text/plain`) :: super.headers
  override def content = contentIn
}

trait RequestHandler {
  def handle(request: Request): Response
}

class DefaultRequestHandler extends RequestHandler {
  def handle(request: Request) = new TextResponse(200, "Commando Application")
}

abstract class WebExceptionHandler {
  def handle(request: Request, exception: Exception): Response
}

class DefaultWebExceptionHandler extends WebExceptionHandler {
  def handle(request: Request, exception: Exception) = {
    val content = "Application error: " + exception.getMessage +
      "\n\n" + exception.getStackTrace.toString
    new TextResponse(500, content)
  }
}

class Route[E](
  val method: Method,
  val path: String,
  val value: E
  )

class PathRoute[E](
  path: String,
  value: E
  ) extends Route(
  Method.Any,
  path,
  value
)

class MatchedRoute[E](
  override val method: Method,
  override val path: String,
  override val value: E,
  val params: Map[String, Object]
  ) extends Route(
  method,
  path,
  value
)

class Router[E](val routes: List[Route[E]]) {
  def getMatch(request: Request): MatchedRoute[E] = null // todo
}
