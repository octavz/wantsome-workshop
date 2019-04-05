package org.wantsome.backend

import doobie.util.Get
import doobie.util.Put
import io.estatico.newtype.macros.newtype
import io.estatico.newtype.ops._
import org.scalacheck.Arbitrary

object Models {

  @newtype class UserId(val toStr: String)

  object UserId {
    def apply(id: String): UserId = id.coerce[UserId]

    implicit val arb: Arbitrary[UserId] = deriving
    implicit val get: Get[UserId]       = deriving
    implicit val put: Put[UserId]       = deriving
  }

  @newtype class SessionId(val toStr: String)

  object SessionId {
    def apply(id: String): SessionId = id.coerce[SessionId]

    implicit val arb: Arbitrary[SessionId] = deriving
    implicit val get: Get[SessionId]       = deriving
    implicit val put: Put[SessionId]       = deriving
  }


  @newtype class ItemId(private val id: String)

  object ItemId {
    def apply(id: String): ItemId = id.coerce[ItemId]

    implicit val arb: Arbitrary[ItemId] = deriving
    implicit val get: Get[ItemId]       = deriving
    implicit val put: Put[ItemId]       = deriving
  }

  @newtype class ListId(private val id: String)

  object ListId {
    type MId = ListId

    def apply(id: String): MId = id.coerce[MId]

    implicit val arb: Arbitrary[MId] = deriving
    implicit val get: Get[MId]       = deriving
    implicit val put: Put[MId]       = deriving

  }

  @newtype case class ListItemId(private val id: String)

  object ListItemId {

  }


  case class User(id: UserId, email: String, password: String)

  case class Session(id: SessionId, userId: UserId)

  case class Item(id: ItemId, name: String, description: String)

  case class ListDef(id: ListId, description: String, userId: UserId)

  case class ListItem(listId: ListId, itemId: ItemId, description: String)

}
