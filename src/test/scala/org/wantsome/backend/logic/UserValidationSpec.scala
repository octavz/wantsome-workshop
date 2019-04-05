package org.wantsome.backend
package logic

import org.scalatest._

class UserValidationSpec extends FlatSpec with Matchers {
  behavior of "User Validation"


  it should "validate correct email address" in  {
    val email = "test@example.com"
    val res = UserValidation.validateEmail(email)
    res.toEither shouldBe 'right
  }

  it should "not allow garbage in email address" in  {
    val email = "test@e^&xample.com"
    val res = UserValidation.validateEmail(email)
    res.toEither shouldBe 'left
  }

  it should "not allow passwords less than 10 chars" in {
    val password = "abc"
    val res = UserValidation.validatePassword(password)
    res.toEither shouldBe 'left
  }

  it should "not allow passwords at least 10 chars without uppercase" in {
    val password = "abcabc1234"
    val res = UserValidation.validatePassword(password)
    res.toEither shouldBe 'left
  }

  it should "not allow passwords at least 10 chars without digits" in {
    val password = "abcABCabcd"
    val res = UserValidation.validatePassword(password)
    res.toEither shouldBe 'left
  }

  it should "allow passwords at least 10 chars and with uppercase and digits" in {
    val password = "abcABC1234"
    val res = UserValidation.validatePassword(password)
    res.toEither shouldBe 'right
  }


}
