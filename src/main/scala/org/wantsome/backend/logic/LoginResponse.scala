package org.wantsome.backend
package logic

import org.wantsome.backend.Models.SessionId

sealed trait LoginResponse

case object UserNotFound extends LoginResponse

case object LoginFailed extends LoginResponse

case class LoginOk(sessionId: SessionId) extends LoginResponse

