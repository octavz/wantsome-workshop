package org.wantsome.backend

import io.estatico.newtype.ops._
import io.estatico.newtype.macros.newtype
import cats._
import cats.effect._
import io.estatico.newtype.Coercible

import org.wantsome.backend.store._
import org.wantsome.backend.Models._

package object logic {

  @newtype class Test[T](val toIO: IO[T])

  object Test {
    def apply[A](io: IO[A]): Test[A] = io.coerce[Test[A]]

    def apply[A](a: A): Test[A] = apply(IO.pure(a))
  }

  implicit def coercibleME[R[_], N[_]](implicit ev: Coercible[MonadError[R, Throwable], MonadError[N, Throwable]], R: MonadError[R, Throwable]): MonadError[N, Throwable] = ev(R)

  implicit def coercibleSync[R[_], N[_]](implicit ev: Coercible[Sync[R], Sync[N]], R: Sync[R]): Sync[N] = ev(R)

  implicit def coercibleContext[R[_], N[_]](implicit ev: Coercible[ContextShift[R], ContextShift[N]], R: ContextShift[R]): ContextShift[N] = ev(R)

  trait TestUserStore extends UserStore[Test] {
    override def createSession(user: Models.User): Test[Session] = ???

    override def getSessionById(id: Models.SessionId): Test[Option[Session]] = ???

    override def getUserById(user: Models.UserId): Test[Option[User]] = ???

    override def createUser(user: Models.User): Test[User] = ???

    override def findUserByEmail(email: String): Test[Option[User]] = ???
  }

}
