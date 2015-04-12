package api.client

import api.account.Account

class Client(val id: Long, val name: String, val account: Account) {

  def canAccessAccount(otherAccountId: Long) = account.id == otherAccountId

  def apiCallsRemaining = 5000

  def isOverRateLimit = apiCallsRemaining < 1

  def requiresAuthToken = false

  def authToken = "12345"

  def isValidAuthToken(token: String) = requiresAuthToken && authToken == token

}
