create table roles
(
    id   integer not null primary key auto_increment,
    role varchar(255)
);
create table users
(
    id           integer primary key not null auto_increment,
    email        varchar(255),
    active       integer,
    is_activated bit,
    last_name    varchar(255)        not null,
    name         varchar(255)        not null,
    password     varchar(255)        not null,
    image_url    varchar(255)

);
create table users_roles
(
    user_id integer not null,
    role_id integer not null,
    constraint role_user_fk foreign key (role_id) references roles (id),
    constraint user_role_fk foreign key (user_id) references users (id)
);
create table confirmation_token
(
    token_id           integer not null primary key auto_increment,
    confirmation_token varchar(255),
    user_id            integer not null,
    constraint user_token_fk foreign key (user_id) references users (id)
);

