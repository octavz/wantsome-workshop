package org.wantsome.backend

import cats.effect._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._

object Routes {
  import Models._
  import  io.circe.generic.auto._, io.circe.syntax._

  def service[F[_]](implicit F: Effect[F], cs: ContextShift[F]) = {
    def getResource(pathInfo: String) = F.delay(getClass.getResource(pathInfo))

    object dsl extends Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "json" / name =>
        Ok(MyData(name).asJson)
    }.orNotFound
  }

}
