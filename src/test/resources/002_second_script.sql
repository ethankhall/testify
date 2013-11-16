create table second_table (
  id int auto_increment primary key,
  some_value varchar(255)
);

insert into second_table (some_value) values ('2');
insert into second_table (some_value) values ('3');