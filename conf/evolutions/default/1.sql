# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table city (
  name                          varchar(255) not null,
  lng                           float,
  lat                           float,
  photo                         varchar(255),
  constraint pk_city primary key (name)
);

create table groups (
  id                            serial not null,
  name                          varchar(255),
  identifiant                   varchar(255),
  authentification              boolean,
  school_id                     integer,
  constraint pk_groups primary key (id)
);

create table school (
  id                            serial not null,
  name                          varchar(255),
  city_name                     varchar(255),
  url                           varchar(255),
  constraint pk_school primary key (id)
);

create table users (
  login                         varchar(255) not null,
  nom                           varchar(255),
  prenom                        varchar(255),
  password                      varchar(255),
  email                         varchar(255),
  constraint pk_users primary key (login)
);

alter table groups add constraint fk_groups_school_id foreign key (school_id) references school (id) on delete restrict on update restrict;
create index ix_groups_school_id on groups (school_id);

alter table school add constraint fk_school_city_name foreign key (city_name) references city (name) on delete restrict on update restrict;
create index ix_school_city_name on school (city_name);


# --- !Downs

alter table if exists groups drop constraint if exists fk_groups_school_id;
drop index if exists ix_groups_school_id;

alter table if exists school drop constraint if exists fk_school_city_name;
drop index if exists ix_school_city_name;

drop table if exists city cascade;

drop table if exists groups cascade;

drop table if exists school cascade;

drop table if exists users cascade;

