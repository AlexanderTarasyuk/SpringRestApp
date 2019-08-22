alter table pet add column version integer;
update pet set version = 1;