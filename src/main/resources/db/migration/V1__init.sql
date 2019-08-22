create table medical_card (
    id serial primary key,
    created_date timestamp,
    pet_id integer,
    special_note_id integer
);

create table medical_card_records (
   records_id integer primary key,
   medical_card_id integer
);

create table medical_record (
   id serial primary key,
   date timestamp,
   end_date timestamp,
   medicine_name varchar(255),
   start_date timestamp,
   times_per_day integer
);

create table medical_record_complaints (
    complaints text,
    medical_record_id integer

);

create table pet (
   id serial primary key,
   age integer,
   breed varchar(255),
   name varchar(255),
   owner varchar(255)
);

create table special_note (
   id serial primary key,
   allergic boolean,
   note text
);