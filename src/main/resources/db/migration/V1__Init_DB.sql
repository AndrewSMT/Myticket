create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );

create table city (
    id_city bigint not null,
    title varchar(255),
    primary key (id_city));

create table event (
    id bigint not null,
    date varchar(255),
    description varchar(2048),
    picture varchar(2500),
    title varchar(255),
    id_place bigint,
    primary key (id));


create table order_status (
    id_order bigint not null,
    order_status integer);

create table order_ticket (
    id_order bigint not null,
    id_ticket bigint not null,
    primary key (id_order, id_ticket));

create table orderr (
    id_order bigint not null,
    id_user bigint,
    primary key (id_order));

create table place (
    id_place bigint not null,
    title varchar(255),
    id_city bigint,
    primary key (id_place));

create table ticket (
    id_ticket bigint not null,
    number integer,
    order_number bigint,
    price integer,
    row_ticket integer,
    id_event bigint,
    primary key (id_ticket));

create table ticket_status (
    id_ticket bigint not null,
    ticket_status integer);

create table user (
    id_user bigint not null,
    activation_code varchar(255),
     email varchar(255),
     password varchar(255),
     picture varchar(255),
     username varchar(255),
     primary key (id_user));

create table user_role (
    id_user bigint not null,
    roles integer);

alter table event add constraint event_place_fk
 foreign key (id_place) references place (id_place);

alter table order_status add constraint order_status_order_fk
 foreign key (id_order) references orderr (id_order);

alter table order_ticket add constraint order_ticket_ticket_fk
 foreign key (id_ticket) references ticket (id_ticket);

alter table order_ticket add constraint order_ticket_order_fk
 foreign key (id_order) references orderr (id_order);

alter table orderr add constraint order_user_fk
 foreign key (id_user) references user (id_user);

alter table place add constraint place_city_fk
 foreign key (id_city) references city (id_city);

alter table ticket add constraint ticket_event_fk
 foreign key (id_event) references event (id);

alter table ticket_status add constraint ticket_status_ticket_fk
 foreign key (id_ticket) references ticket (id_ticket);

alter table user_role add constraint user_role_user_fk
 foreign key (id_user) references user (id_user);