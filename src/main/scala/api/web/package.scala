package api.web

import java.util.UUID
import commando.web._
import api.validation.ValidationError
import spray.http._
import spray.json._

case class Version(major: Int, minor: Int, patch: Int) {
  override def toString: String = major + "." + minor + "." + patch
}

abstract class DataResponse extends ContentResponse {
  override def content = data.prettyPrint
  def data: JsObject
}

case class RequestIdHeader(requestId: String) extends HttpHeader with ToStringRenderable {
  override def name: String = "RequestId"
  override def value: String = requestId.toString
  override def lowercaseName: String = name.toLowerCase
  override def render[R <: Rendering](r: R): r.type = r ~~ requestId
}

case class AppResponse(response: DataResponse, version: Version, requestId: UUID) extends DataResponse {
  override def status = response.status
  override def data = response.data
  override def headers =
    HttpHeaders.Server(Seq(ProductVersion("Config-API", version.toString))) ::
      RequestIdHeader(requestId.toString) ::
      response.headers
}

case class SuccessResponse(message: String) extends DataResponse {
  def status = 200
  def data = JsObject("message" -> JsString(message))
}

abstract case class CreatedResponse(location: String) extends DataResponse {
  def status = 201
  override def headers =
    HttpHeaders.Location(Uri(location)) ::
    super.headers
}

case class ErrorResponse(message: String) extends DataResponse {
  def status = 500
  def data = JsObject("message" -> JsString(message))
}

case class ValidationErrorResponse(message: String, errors: List[ValidationError]) extends DataResponse {
  def status = 400
  def data = JsObject(
    "message" -> JsString(message),
    "errors" -> JsArray(
      errors.map(e => JsObject(
        "field" -> JsString(e.name),
        "message" -> JsString(e.message)
      )).toVector
    )
  )
}


