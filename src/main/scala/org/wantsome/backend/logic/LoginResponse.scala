package org.wantsome.backend.logic

import org.wantsome.backend.Models.SessionId

sealed trait LoginResponse extends Throwable

case object UserNotFound extends LoginResponse

case class Ok(sessionId: SessionId) extends LoginResponse

