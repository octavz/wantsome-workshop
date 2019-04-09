package org.wantsome.backend.util

import cats.effect.Sync
import doobie._
import doobie.implicits._
import simulacrum.typeclass

@typeclass trait RandomProvider[F[_]] {
  def uuid: F[String]
}

object RandomProvider {
  implicit def randomInstanceEffect[F[_] : Sync] = new RandomProvider[F] {
    override def uuid = Sync[F].delay {
      java.util.UUID.randomUUID().toString
    }

  }
}

