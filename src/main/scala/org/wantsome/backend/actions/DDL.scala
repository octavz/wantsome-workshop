package org.wantsome.backend.actions

import cats.effect._
import doobie._
import doobie.implicits._
import simulacrum.typeclass

@typeclass trait DDL[F[_]] {
  def database: F[Unit]
}

object DDL {
  implicit def instanceDDL[F]: DDL[ConnectionIO] =
    new DDL[ConnectionIO] {
      override def database: ConnectionIO[Unit] =
        for {
          _ <- user
          _ <- item
          _ <- list
          _ <- listItem
        } yield ()

      private def user =
        sql"""
          create table if not exists user (
            id bigint identity,
            email varchar(200) not null unique,
            password varchar(200) not null)"""
          .update.run

      private def item =
        sql"""
          create table if not exists item (
            id bigint identity,
            name varchar(1000) not null unique,
            description text)"""
          .update.run

      private def list =
        sql"""
          create table if not exists list (
            id bigint identity,
            description text)"""
          .update.run

      private def listItem =
        sql"""
          create table if not exists list_item (
            list_id bigint,
            item_id bigint,
            description text)"""
          .update.run


    }
}
