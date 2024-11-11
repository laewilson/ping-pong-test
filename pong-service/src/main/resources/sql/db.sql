drop table if exists "operation_log";
create table operation_log (
  id varchar(64) not null,
  access_time timestamp not null default now(),
  operation varchar(64),
  ip varchar(64),
  state varchar(64),
  primary key (id)
);