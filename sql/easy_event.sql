

DROP table if exists tb_user;
CREATE TABLE tb_user(
    id serial,
    email varchar(100) not null,
    password varchar(100) not null,
    primary key(id)
	);


drop table if exists tb_event;
create table tb_event(
    id serial,
    title varchar(100),
    description varchar(255) not null,
    price float not null,
    date timestamp not null,
    creator_id integer not null,
    primary key(id),
    constraint fk_created_id foreign key (creator_id) references tb_user(id)
);
