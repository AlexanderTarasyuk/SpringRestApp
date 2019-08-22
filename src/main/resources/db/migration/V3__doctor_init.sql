create table doctor (
   id serial primary key,
   name varchar(255)
);

create table doctor_schedule_to_date (
   doctor_id integer,
   schedule_to_date_key date ,
   schedule_to_date_id integer
);

create table doctor_specialization(
   doctor_id integer,
   specialization varchar(255)
);

create table appointment(
   id serial primary key
);

create table appointment_hour_to_pet_id(
   hour_to_pet_id_key integer,
   schedule_id integer,
   hour_to_pet_id integer
);