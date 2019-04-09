package org.wantsome.backend
package logic

import org.wantsome.common.Models.User

sealed trait CreateUserResponse

case class CreateUserOk(user: User) extends CreateUserResponse

case object EmailAlreadyExists extends CreateUserResponse

case class InvalidData(message: String) extends CreateUserResponse


sealed trait GetUserResponse

case class GetUserOk(user: User) extends GetUserResponse

case object GetUserNotFound extends GetUserResponse

