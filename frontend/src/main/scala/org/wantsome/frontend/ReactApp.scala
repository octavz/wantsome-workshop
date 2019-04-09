package org.wantsome.frontend

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSImport, ScalaJSDefined}

@JSImport("resources/App.css", JSImport.Default)
@js.native
object AppCSS extends js.Object

@JSImport("resources/logo.svg", JSImport.Default)
@js.native
object ReactLogo extends js.Object

object ReactApp extends Store.Container[Int](Store.counter.listen) {
  type Props = Unit

  private val css = AppCSS

  override def render(a: Int) = {
    div(className := "App")(
      header(className := "App-header")(
        img(src := ReactLogo.asInstanceOf[String], className := "App-logo", alt := "logo"),
        h1(className := "App-title")("Welcome to React (with Scala.js!)"),
      ),
      button(onClick := { () => Store.exec(Actions.increment) })("Increment"),
      button(onClick := { () => Store.exec(Actions.decrement) })("Decrement"),
      p(className := "App-intro")(
        s"current value: $a"
      )
    )
  }
}
