package org.wantsome.backend.actions

import cats.effect._
import simulacrum.typeclass
import doobie._
import doobie.h2._

@typeclass trait TransactorProvider[F[_]] {
  def transactor[T <: Transactor[F]](dbName: String): Resource[F, T]
}

object TransactorProvider {
  implicit def instanceTransactorProvider[F[_] : ContextShift : Async]: TransactorProvider[F] =
    new TransactorProvider[F] {
      override def transactor[T <: doobie.Transactor[F]](dbName: String): Resource[F, T] = for {
        ce <- ExecutionContexts.fixedThreadPool[F](32) // our connect EC
        te <- ExecutionContexts.cachedThreadPool[F] // our transaction EC
        xa <- H2Transactor.newH2Transactor[F](
          s"jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1",
          "sa",
          "",
          ce, // await connection here
          te // execute JDBC operations here
        )
      } yield xa

    }
}
