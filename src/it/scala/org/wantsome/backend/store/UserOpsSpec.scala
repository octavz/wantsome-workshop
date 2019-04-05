package org.wantsome.backend.store

import scala.concurrent.ExecutionContext

import cats._
import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import org.scalacheck.Arbitrary
import org.scalatest._
import org.wantsome.backend.Models.User
import io.estatico.newtype.macros.newtype
import io.estatico.newtype.ops._
import cats._
import cats.data.ContT
import org.wantsome.backend.Models._
import org.wantsome.backend.util.RandomProvider

/*object TestOps {

  @newtype class Test[T](val toIO: IO[T])

  object Test {
    def apply[A](io: IO[A]): Test[A] = io.coerce[Test[A]]

    implicit val functor: Functor[Test] = derivingK


  }

    implicit def deriveTest(conIO: ContextShift[IO]) = new ContextShift[Test] {
      override def shift: Test[Unit] = conIO.shift.coerce[Test[Unit]]


      override def evalOn[A](aec: ExecutionContext)(fa: Test[A]): Test[A] =
        conIO.evalOn(aec)(fa.toIO).coerce[Test[A]]

    }

}*/

class UserOpsSpec extends FlatSpec with Matchers {
  behavior of "UserOps"


  trait Context {

    import org.scalacheck.ScalacheckShapeless._

    implicit val ec                             = ExecutionContext.Implicits.global
    implicit val contextShift: ContextShift[IO] = IO.contextShift(ec)


    val rand = RandomProvider[ConnectionIO]

    def randomUser = (rand.uuid.map(_.coerce[UserId]), rand.uuid, rand.uuid).mapN(User.apply)

    def dbOps[A](dbName: String = "test") = ContT {
      (cb: Transactor[IO] => IO[A]) =>
        TransactorProvider[IO].transactor(dbName).use { implicit xa =>
          val run = DDL[ConnectionIO].database
          run.transact(xa) flatMap (_ => cb(xa))
        }
    }

    val userOps = UserOps[ConnectionIO]
  }

  it should "correctly create an user" in new Context {
    val (expected, Some(actual)) = dbOps().run { xa =>
      val run = for {
        ru <- randomUser
        u <- userOps.createUser(ru)
        r <- sql"select id,email,password from user where id=${u.id}".query[User].option
      } yield (u, r)
      run.transact(xa)
    }.unsafeRunSync()

    actual shouldBe expected
  }

  it should "correctly retrieve an user by id" in new Context {
    val (expected, Some(actual)) = dbOps().run { xa =>
      val run = for {
        ru <- randomUser
        u <- userOps.createUser(ru)
        r <- userOps.getUserById(u.id)
      } yield (u, r)
      run.transact(xa)
    }.unsafeRunSync()

    actual shouldBe expected
  }

  it should "correctly return None when no user found when searching by id" in new Context {
    val actual = dbOps().run { xa =>
      userOps.getUserById("test".coerce[UserId]).transact(xa)
    }.unsafeRunSync()
    actual shouldBe 'empty
  }

  it should "correctly return None when no user found by when searching by email" in new Context {
    val actual = dbOps().run { xa =>
      userOps.findUserByEmail("test").transact(xa)
    }.unsafeRunSync()
    actual shouldBe 'empty
  }

  it should "correctly retrieve an user by email" in new Context {
    val (expected, Some(actual)) = dbOps().run { xa =>
      val run = for {
        ru <- randomUser
        u <- userOps.createUser(ru)
        r <- userOps.findUserByEmail(u.email)
      } yield (u, r)
      run.transact(xa)
    }.unsafeRunSync()

    actual shouldBe expected
  }

  it should "correctly create a session" in new Context {
    val (session, Some(id)) = dbOps().run { xa =>
      val run = for {
        ru <- randomUser
        _ <- userOps.createUser(ru)
        s <- userOps.createSession(ru)
        r <- sql"select id from session where id=${s.id}".query[SessionId].option
      } yield (s, r)
      run.transact(xa)
    }.unsafeRunSync()

    session.id shouldBe id
  }

  it should "correctly retrieve a session by id" in new Context {
    val (expected, Some(actual)) = dbOps().run { xa =>
      val run = for {
        ru <- randomUser
        s <- userOps.createUser(ru)
        s <- userOps.createSession(ru)
        r <- userOps.getSessionById(s.id)
      } yield (s, r)
      run.transact(xa)
    }.unsafeRunSync()

    actual shouldBe expected
  }

  it should "correctly return None when no session foun when searching by id" in new Context {
    val actual = dbOps().run { xa =>
      userOps.getSessionById("test".coerce[SessionId]).transact(xa)
    }.unsafeRunSync()

    actual shouldBe 'empty
  }
}

