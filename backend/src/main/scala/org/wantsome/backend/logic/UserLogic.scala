package org.wantsome.backend
package logic

import cats._
import cats.data._
import cats.effect.Sync
import cats.implicits._
import simulacrum.typeclass
import org.wantsome.backend.store.UserStore
import org.wantsome.backend.util.RandomProvider
import org.wantsome.common.Models._

@typeclass trait UserLogic[F[_]] {

  def createSession(user: User): F[LoginResponse]

  def createUser(user: User): F[CreateUserResponse]

  def getUserById(userId: UserId): F[GetUserResponse]

}

object UserLogic {

  implicit def instanceF[F[_] : Sync : RandomProvider, G[_] : UserStore : Monad](implicit E: MonadError[F, Throwable], toF: G ~> F): UserLogic[F] =
    new UserLogic[F] {
      val store  = UserStore[G]

      import UserValidation._

      private def validateUserData(user: User): Either[NonEmptyChain[UserValidation], User] =
        (user.id.validNec, validateEmail(user.email), validatePassword(user.password)).mapN(User).toEither

      override def createSession(user: User) =
        toF(store.findUserByEmail(user.email)) flatMap {
          case Some(u) if u.password == user.password =>
            toF(store.createSession(u).map(s => LoginOk(s.id)))
          case None                                   => Sync[F].pure(UserNotFound)
          case _                                      => Sync[F].pure(LoginFailed)
        }

      override def getUserById(userId: UserId) =
        toF(store.getUserById(userId)) map {
          case Some(u) => GetUserOk(u)
          case None    => GetUserNotFound
        }

      override def createUser(user: User): F[CreateUserResponse] =
        RandomProvider[F].uuid flatMap { id =>
          validateUserData(user.copy(id = UserId(id))) match {
            case Left(d)  =>
              Sync[F].pure(InvalidData(d.mkString_("", "\n", "")))
            case Right(u) =>
              toF(store.findUserByEmail(user.email)) flatMap {
                case Some(_) => Sync[F].pure(EmailAlreadyExists)
                case None    => toF(store.createUser(u).map(CreateUserOk))
              }
          }
        }
    }
}

