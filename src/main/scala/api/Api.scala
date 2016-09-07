package api

import api.web.SuccessResponse
import commando._
import commando.web._

class Api extends Application {

  override def handle(request: Request) = {
    SuccessResponse("Hello from commando")
  }

}
