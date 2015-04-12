package api

import api.web.SuccessResponse
import commando._
import commando.web._
import scalikejdbc.config.DBs

class Api extends Application {
//  DBs.setupAll()

  override def handle(request: Request) = {
    SuccessResponse("Hello from commando")
  }

}
