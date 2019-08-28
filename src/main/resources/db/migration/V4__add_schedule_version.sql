alter table schedule add column version integer;
update schedule set version = 1;