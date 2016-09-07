package api.todo

import api.account.ClientRequest
import commando.web.Router
import spray.http.HttpResponse

class TodoModuleHandler {

  def handle(request: ClientRequest): HttpResponse = {
    val router = new Router(Seq[

      ])
  }


}

class AccountModule(
                     val accountStore: AccountStore,
                     val configModule: ConfigModule,
                     val consumerModule: ConsumerModule
                     ) {

  lazy val accountPostValidator = new AccountPostValidator

  lazy val listHandler = new ListAccountsHandler(accountStore)
  lazy val postAccountHandler = new PostAccountHandler(accountStore, accountPostValidator)
  lazy val getAccountHandler = new GetAccountHandler(accountStore)
  lazy val configHandler = configModule
  lazy val consumerHandler = consumerModule

  lazy val router = new AccountRouter()

  def handle(request: ClientRequest): HttpResponse = router.handle(request)
}
