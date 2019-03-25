package org.wantsome.backend

import scala.util.Properties.envOrNone
import cats.implicits._
import cats.effect._
import doobie._
import doobie.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import actions._
import doobie.h2._
import org.h2.tools.Server

object Main extends IOApp {

  val ip: String = "0.0.0.0"
  val dbName = "lists"

  private def startConsole(dbName: String) = IO {
    val server = Server.createTcpServer("-tcpPort", "30000").start()
    println("URL: jdbc:h2:" + server.getURL() + "/mem:" + dbName)
  }

  override def run(args: List[String]): IO[ExitCode] =
    TransactorProvider[IO].transactor[H2Transactor[IO]](dbName).use { implicit xa =>
      for {
        _ <- startConsole(dbName)
        _ <- DDL[ConnectionIO].database.transact(xa)
        port <- IO(envOrNone("HTTP_PORT").map(_.toInt).getOrElse(8080))
        exitCode <- BlazeServerBuilder[IO]
          .bindHttp(port, "localhost")
          .withHttpApp(Routes.service)
          .serve
          .compile
          .drain
          .as(ExitCode.Success)
      } yield exitCode
    }

}
