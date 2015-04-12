package api.security

import api.account.Account
import api.client.Client
import spray.http.HttpRequest

class Guard {

  /**
   * Authenticate an HTTP request using HTTP basic auth.
   * @param request request to authenticate
   * @return The authenticated client making the request
   */
  def authenticate(request: HttpRequest): Option[Client] = {
    // todo: check authorization header and return calling Client
    if (! request.headers.contains("Authorization")) {
      return None
    }

    val fakeAccount = new Account(1, "brady", "bradyaolsen@gmail.com")

    Some(new Client(1, "ConsumerApp", fakeAccount))
  }

}
