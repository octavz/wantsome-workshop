package org.wantsome.backend.logic

import cats._
import cats.implicits._
import org.wantsome.backend.Models.SessionId
import org.wantsome.backend.store.UserOps
import simulacrum.typeclass

@typeclass trait UserLogic[F[_]] {

  def login(email: String, password: String): F[LoginResponse]

}

object UserLogic {
  def instanceF[F[_]: Monad : UserOps] = new UserLogic[F] {
    val store = UserOps[F]
    override def login(email: String, password: String) = for{
      user <- store.findUserByEmail(email)
      _ <- if(user.isDefined)

    } yield Ok("")

  }
}
