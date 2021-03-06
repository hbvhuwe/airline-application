create table routes (
    route_id int not null primary key auto_increment,
    departure_airport varchar(100) not null,
    destination_airport varchar(100) not null,
    price double not null,
    duration time not null
);

create table flights (
    flight_id int not null primary key auto_increment,
    departure_time time not null,
    arrival_time time not null,
    cancellation_status boolean default false,
    route_id int not null,
    pilot_id int not null,
    airplane_id int not null
);

create table tickets (
    ticket_id int not null primary key auto_increment,
    flight_id int not null,
    passenger_id int not null,
    seat varchar(5) not null
);

create table airplanes (
    airplane_id int not null primary key auto_increment,
    airplane_number int not null,
    model varchar(50) default '',
    manufacture_date date,
    lifetime int default 5,
    seat_number int not null
);

create table passengers (
    passenger_id int not null primary key auto_increment,
    first_name varchar(50),
    last_name varchar(50),
    address varchar(50),
    phone_number varchar(25),
    user_id int not null
);

create table users (
    user_id int not null primary key auto_increment,
    email varchar(50) unique,
    password char(64),
    role varchar(10)
);

create table sessions (
    session_id int not null primary key auto_increment,
    user_id int not null,
    session_key char(64) not null unique
);

create table pilots (
    pilot_id int not null primary key auto_increment,
    first_name varchar(50),
    last_name varchar(50),
    address varchar(50),
    phone_number varchar(50),
    experience int
);
