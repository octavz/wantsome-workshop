package org.wantsome.backend
package actions

import simulacrum.typeclass
import io.estatico.newtype.macros.newtype
import io.estatico.newtype.ops._

case class Person(id: String)

@typeclass trait Eq[A] {
  def areEqual(x: A, y: A): Boolean
}

object Eq {

  implicit val intInst: Eq[Int] = new Eq[Int] {
    override def areEqual(x: Int, y: Int): Boolean = x == y
  }

  implicit val stringInst: Eq[String] = new Eq[String] {
    override def areEqual(x: String, y: String): Boolean = x.equals(y)
  }

  implicit val personInst: Eq[Person] = new Eq[Person] {
    override def areEqual(x: Person, y: Person): Boolean =
      Eq[String].areEqual(x.id, y.id)
  }

}


object IgnoreCaseEq {

  @newtype case class IgnoreCaseString(value: String)

  implicit val stringInst: Eq[IgnoreCaseString] = new Eq[IgnoreCaseString] {
    override def areEqual(x: IgnoreCaseString, y: IgnoreCaseString): Boolean =
      x.value.toLowerCase().equals(y.value.toLowerCase())
  }
}

object EqOps {
  def areEqual[A: Eq](x: A, y: A): Boolean = Eq[A].areEqual(x, y)
}

