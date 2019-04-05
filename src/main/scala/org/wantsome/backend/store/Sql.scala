package org.wantsome.backend.store

import doobie.implicits._

object Sql {
  val createUserSql =
    sql"""
          create table if not exists user (
            id varchar(36) primary key,
            email varchar(200) not null unique,
            password varchar(200) not null)"""

  val createSessionSql =
    sql"""
          create table if not exists session (
            id varchar(36) primary key,
            user_id varchar(200) unique references user)"""

  val createItemSql =
    sql"""
          create table if not exists item (
            id varchar(36) primary key,
            name varchar(1000) not null unique,
            description text)"""

  val createListSql =
    sql"""
          create table if not exists list (
            id varchar(36) primary key,
            user_id bigint not null references user,
            description text)"""

  val createListItemSql =
    sql"""
          create table if not exists list_item (
            list_id varchar(36) not null references list,
            item_id varchar(36) not null references item,
            description text,
            primary key(list_id, item_id) )"""
}
