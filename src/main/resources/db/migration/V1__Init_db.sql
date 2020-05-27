alter table place drop foreign key FKlw8w8ebld7eye2gvoxi0utiai
alter table place_type drop foreign key FK7blrkile2e4apuo810oyuql9r
alter table place_type drop foreign key FK1uqsjoh58d4qor6rqq2o2jmcr
alter table trip drop foreign key FKd8pbh44g1ci1797yixosxacb9

drop table if exists place
drop table if exists place_type
drop table if exists trip
drop table if exists type
drop table if exists users

create table place (
id bigint not null auto_increment,
formatted_address varchar(255) not null,
google_place_id varchar(255) not null,
location_lat decimal(15,15) not null,
location_lng decimal(15,15) not null,
name varchar(255) not null,
parent_location varchar(255),
price_level integer not null,
rating decimal(19,2) not null,
trip_id bigint not null,
primary key (id))


create table place_type (
type_id bigint not null,
place_id bigint not null)

create table trip (
id bigint not null auto_increment,
created_at datetime(6) not null,
name varchar(255) not null,
updated_at datetime(6) not null,
user_id bigint not null,
primary key (id))

create table type (
id bigint not null auto_increment,
name varchar(255) not null,
primary key (id))

create table user (
id bigint not null auto_increment,
first_name varchar(255) not null,
last_name varchar(255) not null,
password varchar(255) not null,
role varchar(255) not null,
username varchar(255) not null,
primary key (id))


alter table type
add constraint UK_3tg65hx29l2ser69ddfwvhy4h unique (name)

alter table user
add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username)

alter table place
add constraint FKlw8w8ebld7eye2gvoxi0utiai
foreign key (trip_id) references trip (id)

alter table place_type
add constraint FK7blrkile2e4apuo810oyuql9r
foreign key (place_id) references place (id)

alter table place_type
add constraint FK1uqsjoh58d4qor6rqq2o2jmcr
foreign key (type_id) references type (id)

alter table trip
add constraint FKd8pbh44g1ci1797yixosxacb9
foreign key (user_id) references user (id)