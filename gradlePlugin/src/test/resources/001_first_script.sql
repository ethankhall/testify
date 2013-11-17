create table some_table (
  id int auto_increment primary key,
  some_value varchar(255)
);

insert into some_table (some_value) values ('hello');
insert into some_table (some_value) values ('goodbye');