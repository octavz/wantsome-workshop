package org.wantsome.backend.util


sealed trait Expr
case class IntExpr(i: Int) extends Expr
case class BoolExpr(b: Boolean) extends Expr

object ExprOps {
  def eval(e: Expr) = e match {
    case IntExpr(i) => i
    case BoolExpr(b) => b
  }
}

/*
sealed trait Expr[T]
case class IntExpr(i: Int) extends Expr[Int]
case class BoolExpr(b: Boolean) extends Expr[Boolean]

object ExprOps {
  def eval[T](e: Expr[T]): T = e match {
    case IntExpr(i) => i
    case BoolExpr(b) => b
  }
}*/
