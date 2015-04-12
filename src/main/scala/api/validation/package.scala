package api.validation

case class ValidationError(name: String, message: String)

class EmptyStringValidator {
  def isEmpty(x: String) = x.length < 1
}

class EmailValidator {
  def isEmail(x: String): Boolean = x.contains("@")
}