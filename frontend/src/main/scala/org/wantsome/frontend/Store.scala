package org.wantsome.frontend

import cats.effect._
import com.olegpy.shironeko._

object Store extends StoreBase[IO](Main.Instance) with ImpureIntegration[IO] with SlinkyIntegration[IO] {
  val counter = Cell(0)

}
