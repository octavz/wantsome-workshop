package org.wantsome.frontend

import cats.effect._

object Actions {
  val increment: IO[Unit] = Store.counter.update(_ + 1)
  val decrement: IO[Unit] = Store.counter.update(_ - 1)
}
