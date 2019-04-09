package org.wantsome.backend
package logic

import cats._
import cats.data._
import cats.implicits._

sealed trait UserValidation extends Throwable {
  def message: String
}

case object EmailIsEmpty extends UserValidation {
  override def message = "Email is empty"
}

case object EmailIsTooLong extends UserValidation {
  override def message = "Email is too long"
}

case object EmailNotValid extends UserValidation {
  override def message = "Email is not valid"
}

case object PasswordNotMeetingCriteria extends UserValidation {
  override def message = "Password does not meet validation criteria"
}

object UserValidation {
  type Valid[A] = ValidatedNec[UserValidation, A]

  implicit val showInst = new Show[UserValidation] {
    override def show(t: UserValidation) = t.message
  }

  def validatePassword(password: String): Valid[String] = {
    val passRegex = "(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$"
    if (password.matches(passRegex)) password.validNec
    else PasswordNotMeetingCriteria.invalidNec
  }


  def validateEmail(email: String): Valid[String] = {
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    email match {
      case e if e.matches(emailRegex) => e.validNec
      case e if e.trim.isEmpty        => EmailIsEmpty.invalidNec
      case e if e.length > 200        => EmailIsTooLong.invalidNec
      case _                          => EmailNotValid.invalidNec
    }

  }
}

