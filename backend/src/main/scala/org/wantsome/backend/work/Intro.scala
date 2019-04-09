package org.wantsome.backend.work

object Intro extends App {

  val x: Int = 1

  def f(x: Int): Int = x + 1

  val f: Int => Int = x => x + 1

  val f1: Int => (Int => Int) = x => (y => x + y)

  val f2: Int => Int => Int = x => y => x + y
}

//https://scastie.scala-lang.org/5aGzHhlYQ5adtXg3aQ3jbQ
object Lib {

  import scala.language.implicitConversions
  import cats._
  import cats.effect._
  import cats.implicits._
  import simulacrum.typeclass

  sealed trait Response

  case object Bad extends Response

  case class Good(data: Data) extends Response

  case class Data(value: String)

  case object Error extends Throwable {
    override def getMessage = "Error happened"
  }

  @typeclass trait Database[F[_]] {
    def update(d: Data): F[Response]
  }

  object Database {
    implicit def inst[F[_] : Sync]: Database[F] = new Database[F] {
      override def update(d: Data): F[Response] =
        Sync[F].pure(if (d.value.isEmpty) Bad else Good(d))
    }
  }

  def update(d: Data): Unit = println(s"Updating: $d")

  def dump[F[_] : Database : Sync](d: Data): F[Unit] =
    for {
      _ <- {
        //println(s"Debug: $d"); Sync[F].delay(update(d)) //original code
        Sync[F].delay(println(s"Debug: $d")).map(_ => update(d))
      }
      _ <- Database[F].update(d).map(res => if (res == Bad) throw Error else ())
    } yield ()

}

import Lib._

object Cats {

  import cats.effect._

  def run(d: Data) =
    dump[IO](d).attempt.map {
      case Left(e) =>
        println(s"Cats: ${e.getMessage}")
      case Right(_) =>
        println("Things are fine in Cats")
    }.unsafeRunSync()
}

object Monix {

  import monix.eval.Task
  import scala.concurrent.duration._
  import monix.execution.Scheduler.Implicits.global

  def run(d: Data) =
    dump[Task](d).attempt.map {
      case Left(e) =>
        println(s"Monix: ${e.getMessage}")
      case Right(_) =>
        println("Things are fine in Monix")
    }.runSyncUnsafe(10.seconds)
}

object Main extends App {
  Cats.run(Data(""))
  Cats.run(Data("something"))
  Monix.run(Data(""))
  Monix.run(Data("something"))
}
