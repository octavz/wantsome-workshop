package org.wantsome.backend.store

import doobie._
import simulacrum.typeclass

@typeclass trait DDL[F[_]] {

  def database: F[Unit]
}

object DDL {

  import Sql._

  implicit def instanceDDL: DDL[ConnectionIO] = new DDL[ConnectionIO] {

    def database: ConnectionIO[Unit] = for {
      _ <- createUserSql.update.run
      _ <- createSessionSql.update.run
      _ <- createItemSql.update.run
      _ <- createListSql.update.run
      _ <- createListItemSql.update.run
    } yield ()

  }
}
