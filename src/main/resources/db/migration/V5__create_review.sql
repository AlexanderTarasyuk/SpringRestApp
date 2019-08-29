create table review (
   id serial primary key,
   doctor_id integer,
   medical_record_id integer,
   service_stars integer,
   equipment_stars integer,
   qualification_stars integer,
   treatment_results_stars integer,
   general_Stars integer,
   comment varchar(255),
   creation_time timestamp,
   version integer
);

