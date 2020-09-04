set timezone = '+04';

create type status as enum ('ACTIVE', 'SUSPENDED', 'DELETED');
create type connection_type as enum ('ADSL', 'Dial_up', 'ISDN', 'Cable', 'Fiber');

create table if not exists client
(
    id serial not null primary key,
    name varchar(50) not null,
    phone_number varchar(12) not null,
    email_address varchar(129) not null
);

create table if not exists internet
(
    id serial not null primary key,
    client_id integer not null references client(id),
    activation_date timestamp(0) not null,
    status status
);

create table if not exists phone
(
    id serial not null primary key,
    client_id integer not null references client(id),
    activation_date timestamp(0) not null,
    status status
);

create table if not exists television
(
    id serial not null primary key,
    client_id integer not null references client,
    activation_date timestamp(0) not null,
    status status
);

create table if not exists internet_state
(
    id serial not null primary key,
    internet_id integer not null references internet on update cascade on delete cascade,
    begin_date timestamp(0) not null,
    end_date timestamp(0),
    speed integer not null,
    antivirus boolean,
    connection_type connection_type not null
);

create table if not exists phone_state
(
    id serial not null primary key,
    phone_id integer not null references phone on update cascade on delete cascade,
    begin_date timestamp(0) not null,
    end_date timestamp(0),
    mins_count integer not null,
    sms_count integer not null
);

create table if not exists television_state
(
    id serial not null primary key,
    television_id integer not null references television on update cascade on delete cascade,
    begin_date timestamp(0) not null,
    end_date timestamp(0),
    channels_count integer not null
);





