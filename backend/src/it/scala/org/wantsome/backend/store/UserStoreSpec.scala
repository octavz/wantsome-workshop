package org.wantsome.backend.store

import scala.concurrent.ExecutionContext

import cats._
import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import org.scalacheck.Arbitrary
import org.scalatest._
import org.wantsome.common.Models.User
import io.estatico.newtype.ops._
import io.estatico.newtype.macros.newtype
import cats._
import cats.data.ContT
import org.wantsome.common.Models._
import org.wantsome.backend.util.RandomProvider


class UserStoreSpec extends FlatSpec with Matchers {
  behavior of "UserOps"

  trait Context {

    implicit val ec                             = ExecutionContext.Implicits.global
    implicit val contextShift: ContextShift[IO] = IO.contextShift(ec)

    val rand = RandomProvider[ConnectionIO]

    def randomUser =
      (rand.uuid.map(UserId.apply), rand.uuid, rand.uuid).mapN(User.apply)

    def dbOps[A](dbName: String = "test") = ContT {
      (cb: Transactor[IO] => IO[A]) =>
        TransactorProvider[IO].transactor(dbName).use { implicit xa =>
          val run = DDL[ConnectionIO].database
          run.transact(xa) flatMap (_ => cb(xa))
        }
    }

    val store = UserStore[ConnectionIO]
  }

  it should "correctly create an user" in new Context {
    val (expected, Some(actual)) = dbOps().run { xa =>
      val run = for {
        ru <- randomUser
        u <- store.createUser(ru)
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
        u <- store.createUser(ru)
        r <- store.getUserById(u.id)
      } yield (u, r)
      run.transact(xa)
    }.unsafeRunSync()

    actual shouldBe expected
  }

  it should "correctly return None when no user found when searching by id" in new Context {
    val actual = dbOps().run { xa =>
      store.getUserById(UserId("test")).transact(xa)
    }.unsafeRunSync()
    actual shouldBe 'empty
  }

  it should "correctly return None when no user found by when searching by email" in new Context {
    val actual = dbOps().run { xa =>
      store.findUserByEmail("test").transact(xa)
    }.unsafeRunSync()
    actual shouldBe 'empty
  }

  it should "correctly retrieve an user by email" in new Context {
    val (expected, Some(actual)) = dbOps().run { xa =>
      val run = for {
        ru <- randomUser
        u <- store.createUser(ru)
        r <- store.findUserByEmail(u.email)
      } yield (u, r)
      run.transact(xa)
    }.unsafeRunSync()

    actual shouldBe expected
  }

  it should "correctly create a session" in new Context {
    val (session, Some(id)) = dbOps().run { xa =>
      val run = for {
        ru <- randomUser
        _ <- store.createUser(ru)
        s <- store.createSession(ru)
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
        s <- store.createUser(ru)
        s <- store.createSession(ru)
        r <- store.getSessionById(s.id)
      } yield (s, r)
      run.transact(xa)
    }.unsafeRunSync()

    actual shouldBe expected
  }

  it should "correctly return None when no session foun when searching by id" in new Context {
    val actual = dbOps().run { xa =>
      store.getSessionById(SessionId("test")).transact(xa)
    }.unsafeRunSync()

    actual shouldBe 'empty
  }
}

