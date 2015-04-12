package api.account

import api.validation.ValidationError
import scalikejdbc._
import spray.http._
import scala.collection.mutable

class ConfigModule
class ConsumerModule


abstract class AccountStore {
  def save(post: NewAccount): Account
  def get(id: Long): Option[Account]
  def find: List[Account]
}
class DatabaseAccountStore(val session: DBSession = AutoSession) extends AccountStore {
  override def save(newAccount: NewAccount) = {
    DB localTx { implicit session => {
      val id = sql"""
          insert into accounts (name, email)
          values (${newAccount.name}, ${newAccount.email})
        """
        .updateAndReturnGeneratedKey()
        .apply()
      new Account(id, newAccount.name, newAccount.email)
    }}
  }
  override def get(id: Long): Option[Account] = {
    DB readOnly { implicit session => {
      sql"select id, name, email from accounts where id = ${id}"
        .map(resultSetMapper)
        .single()
        .apply()
    }}
  }
  override def find: List[Account] = {
    DB.readOnly { implicit session => {
      sql"select * from accounts"
        .map(resultSetMapper)
        .list()
        .apply()
    }}
  }
  private def resultSetMapper(rs: WrappedResultSet) = new Account(
    rs.long("id"),
    rs.string("name"),
    rs.string("email")
  )
}
class MemoryAccountStore extends AccountStore {
  val accounts = new mutable.LinkedHashMap[Long, Account]()
  override def save(newAccount: NewAccount) = {
    val id = accounts.size
    val account = new Account(id, newAccount.name, newAccount.email)
    accounts.put(id, account)
    account
  }
  override def get(id: Long): Option[Account] = accounts.get(id)
  override def find: List[Account] = accounts.values.toList
}

class ListAccountsHandler(val accountRepo: AccountStore)

class GetAccountHandler(val accountRepo: AccountStore)

class ClientRequest extends HttpRequest

class AccountRouter {
  def handle(clientRequest: ClientRequest) = new HttpResponse(200, "in account router")
}

class NewAccount(val name: String, val email: String)

class PostAccountRequest extends HttpRequest {
  def post = new NewAccount("brady", "bradyaolsen@gmail.com")
}

class AccountPostValidator {
  def validate(post: NewAccount): List[ValidationError] = List()
}

class AccountCreatedResponse(val account: Account, val request: HttpRequest) extends HttpResponse

class ValidationErrorResponse(
  val s: String,
  val errors: List[ValidationError],
  request: HttpRequest
  ) extends HttpResponse

class PostAccountHandler(
  val repo: AccountStore,
  val validator: AccountPostValidator
  ) {

  def handle(request: PostAccountRequest): HttpResponse = {
    val post = request.post
    val errors = validator.validate(post)
    if (errors.nonEmpty) {
      return new ValidationErrorResponse("Account post is invalid", errors, request)
    }

    val account = repo.save(post)

    new AccountCreatedResponse(account, request)
  }
}

case class Account(
  id: Long,
  name: String,
  emailAddress: String)

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