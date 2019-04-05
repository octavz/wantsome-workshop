package org.wantsome.backend

import cats.effect._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._
import org.wantsome.backend.logic.UserLogic
import cats.implicits._
import org.http4s.circe.CirceEntityDecoder._
import org.wantsome.backend.logic._

object Routes {

  import Models._
  import io.circe.generic.auto._, io.circe.syntax._
  import SerDe._

  def service[F[_] : UserLogic](implicit F: Effect[F], cs: ContextShift[F]) = {
    val logic = UserLogic[F]

    def getResource(pathInfo: String) = F.delay(getClass.getResource(pathInfo))

    object dsl extends Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case req@POST -> Root / "user" =>
        for {
          user <- req.as[User]
          resp <- logic.createUser(user)
          r <- resp match {
            case CreateUserOk(u)       => Ok(u)
            case EmailAlreadyExists => Conflict()
            case InvalidData(m)     => BadRequest(m)
            case _ => InternalServerError()
          }
        } yield r

      case GET -> Root / "user" / id =>
        for {
          resp <- logic.getUserById(UserId(id))
          r <- resp match {
            case GetUserOk(user) => Ok(user)
            case GetUserNotFound => NotFound()
            case _ => InternalServerError()
          }
        } yield r

      case req@POST -> Root / "session" =>
        for {
          user <- req.as[User]
          resp <- logic.createSession(user)
          r <- resp match {
            case UserNotFound => NotFound()
            case LoginFailed  => Forbidden()
            case LoginOk(id)  => Ok(id.toStr)
            case _ => InternalServerError()
          }
        } yield r

    }.orNotFound
  }

}
