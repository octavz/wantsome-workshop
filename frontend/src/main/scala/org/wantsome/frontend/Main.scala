package org.wantsome.frontend

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportTopLevel, JSImport}
import scala.scalajs.LinkingInfo

import slinky.core._
import slinky.web.ReactDOM
import slinky.hot
import cats.effect._

import org.scalajs.dom

@JSImport("resources/index.css", JSImport.Default)
@js.native
object IndexCSS extends js.Object

object Main extends IOApp{
  val Instance = ConcurrentEffect[IO]
  val css = IndexCSS

  override def run(args: List[String]): IO[ExitCode] = {
    main()
    IO.pure(ExitCode.Success)
  }

  @JSExportTopLevel("main")
  def main(): Unit = {
    if (LinkingInfo.developmentMode) {
      hot.initialize()
    }

    val container = Option(dom.document.getElementById("root")).getOrElse {
      val elem = dom.document.createElement("div")
      elem.id = "root"
      dom.document.body.appendChild(elem)
      elem
    }

    ReactDOM.render(ReactApp(), container)
  }
}
