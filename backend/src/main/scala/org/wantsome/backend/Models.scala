package org.wantsome.backend

import doobie.util._
import io.estatico.newtype.macros.newtype
import io.estatico.newtype.ops._
import org.scalacheck.Arbitrary
import org.wantsome.common.Models._

object Models {


  object UserId {

    implicit val get: Get[UserId]       = deriving
    implicit val put: Put[UserId]       = deriving
  }

  object SessionId {

    implicit val get: Get[SessionId]       = deriving
    implicit val put: Put[SessionId]       = deriving
  }

  object ItemId {

    implicit val get: Get[ItemId]       = deriving
    implicit val put: Put[ItemId]       = deriving
  }

  object ListId {

    implicit val get: Get[ListId]       = deriving
    implicit val put: Put[ListId]       = deriving

  }

  object ListItemId {

  }
}
