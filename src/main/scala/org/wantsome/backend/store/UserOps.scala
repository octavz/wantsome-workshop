package org.wantsome.backend.store

import doobie._
import doobie.implicits._
import simulacrum.typeclass
import org.wantsome.backend.Models._
import org.wantsome.backend.util.RandomProvider

@typeclass trait UserOps[F[_]] {

  def createSession(user: User): F[Session]

  def getSessionById(id: SessionId): F[Option[Session]]

  def createUser(user: User): F[User]

  def getUserById(user: UserId): F[Option[User]]

  def findUserByEmail(email: String): F[Option[User]]

}

object UserOps {
  implicit val userOpsDoobieInst = new UserOps[ConnectionIO] {
    val R = RandomProvider[ConnectionIO]

    override def createSession(user: User) =
      for {
        id <- R.uuid
        _ <- sql"insert into session(id, user_id) values($id, ${user.id})".update.run
      } yield Session(SessionId(id), user.id)

    override def createUser(user: User) =
      for {
        id <- R.uuid
        _ <- sql"insert into user(id, email, password) values(${user.id}, ${user.email}, ${user.password})".update.run
      } yield user

    override def getSessionById(id: SessionId) =
      sql"select id, user_id from session where id=$id".query[Session].option

    override def getUserById(id: UserId) =
      sql"select id, email, password from user where id=$id".query[User].option

    override def findUserByEmail(email: String) =
      sql"select id, email, password from user where email=$email".query[User].option
  }
}
