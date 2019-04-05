package org.wantsome.backend

import doobie.util.Get
import doobie.util.Put
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.estatico.newtype.macros.newtype
import org.scalacheck.Arbitrary

object Models {

  @newtype case class UserId(id: String)

  object UserId {
    implicit val arb: Arbitrary[UserId] = deriving
    implicit val get: Get[UserId]       = deriving
    implicit val put: Put[UserId]       = deriving
  }

  @newtype case class SessionId(id: String)

  object SessionId {
    implicit val arb: Arbitrary[SessionId] = deriving
    implicit val get: Get[SessionId]       = deriving
    implicit val put: Put[SessionId]       = deriving
  }


  @newtype case class ItemId(id: String)

  object ItemId {
    implicit val arb: Arbitrary[ItemId] = deriving
    implicit val get: Get[ItemId]       = deriving
    implicit val put: Put[ItemId]       = deriving
  }

  @newtype case class ListId(id: String)

  @newtype case class ListItemId(id: String)


  implicit val getSessionId                 = Get[String].map(SessionId.apply)
  implicit val putSessionId: Put[SessionId] = Put[String].contramap(_.id)


  case class User(id: UserId, email: String, password: String)

  case class Session(id: SessionId, userId: UserId)

  case class Item(id: ItemId, name: String, description: String)

  case class ListDef(id: ListId, description: String, userId: UserId)

  case class ListItem(listId: ListId, itemId: ItemId, description: String)

}
