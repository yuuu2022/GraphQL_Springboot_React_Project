drop table if exist event;

create table event(
    id serial,
    title varchar(100),
    description varchar(255) not null,
    price float not null,
    date timestamp not null,
    primary key(id)
)
