package org.wantsome.backend
package logic

import org.scalatest._

import cats._
import cats.implicits._
import cats.data._
import org.wantsome.backend.store.UserStore
import org.wantsome.common.Models._
import org.wantsome.backend.util.RandomProvider

class UserLogicSpec extends FlatSpec with Matchers {
  behavior of "UserLogic"

  trait Context {
    implicit val store : UserStore[Test]      = new TestUserStore {}
    implicit val random: RandomProvider[Test] = new RandomProvider[Test] {
      override def uuid = Test("test")
    }

    implicit val t = new (Test ~> Test) {
      override def apply[A](fa: Test[A]): Test[A] = fa
    }

    def logic = UserLogic[Test]

    val defUser = User(UserId("id"), "email@example.com", "abcdABCD12")

  }

  it should "create user when data is correct" in new Context {
    override implicit val store = new TestUserStore {
      override def createUser(user: User) = Test(user)

      override def findUserByEmail(email: String) = Test(None)
    }

    val res = logic.createUser(defUser).toIO.unsafeRunSync()
    res shouldBe a[CreateUserOk]
  }

  it should "return EmailAlreadyExists when email exists" in new Context {
    override implicit val store: UserStore[Test] = new TestUserStore {
      override def findUserByEmail(email: String) = Test(Some(defUser))
    }

    val res = logic.createUser(defUser).toIO.attempt.unsafeRunSync()
    res.right.get shouldBe EmailAlreadyExists
  }

  it should "return InvalidData when data won't validate" in new Context {
    val res = logic.createUser(defUser.copy(email = "blah")).toIO.attempt.unsafeRunSync()
    res.right.get shouldBe InvalidData("Email is not valid")
  }

  it should "aggregate all errors when data is not valid" in new Context {
    val res = logic.createUser(defUser.copy(email = "blah", password = "poor")).toIO.attempt.unsafeRunSync()
    res.right.get shouldBe InvalidData("Email is not valid\nPassword does not meet validation criteria")
  }


}
