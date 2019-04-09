package org.wantsome.backend

import cats.effect._
import org.wantsome.common.Models._
import cats.Applicative
import io.circe._
import io.circe.generic.semiauto._
import org.http4s.circe._

object SerDe {

  implicit val userJsonDecoder: Decoder[User] = new Decoder[User] {
    override def apply(c: HCursor) = for {
      email <- c.downField("email").as[String]
      password <- c.downField("password").as[String]
    } yield User(UserId(""), email, password)
  }
  implicit val userJsonEncoder: Encoder[User] = new Encoder[User] {
    override def apply(a: User) = Json.obj(
      ("id", Json.fromString(a.id.toStr)),
      ("email", Json.fromString(a.email)),
      ("password", Json.fromString(a.password))
    )
  }

  implicit def userEncoder[F[_] : Applicative] = jsonEncoderOf[F, User]

}
