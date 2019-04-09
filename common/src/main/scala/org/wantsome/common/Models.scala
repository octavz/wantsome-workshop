package org.wantsome.common

import io.estatico.newtype.ops._
import io.estatico.newtype.macros.newtype
import org.scalacheck.Arbitrary

object Models {

  @newtype class UserId(val toStr: String)

  object UserId {
    def apply(id: String): UserId = id.coerce[UserId]

    implicit val arb: Arbitrary[UserId] = deriving
  }

  @newtype class SessionId(val toStr: String)

  object SessionId {
    def apply(id: String): SessionId = id.coerce[SessionId]

    implicit val arb: Arbitrary[SessionId] = deriving
  }


  @newtype class ItemId(private val id: String)

  object ItemId {
    def apply(id: String): ItemId = id.coerce[ItemId]

    implicit val arb: Arbitrary[ItemId] = deriving
  }

  @newtype class ListId(private val id: String)

  object ListId {
    def apply(id: String): ListId = id.coerce[ListId]

    implicit val arb: Arbitrary[ListId] = deriving

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
