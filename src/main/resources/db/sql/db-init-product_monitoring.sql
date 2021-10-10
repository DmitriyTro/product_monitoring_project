-- table: user
create table "user"
(
    id serial
        constraint user_pk
            primary key,
    first_name varchar(25),
    last_name  varchar(25),
    password   varchar(255) not null,
    username   varchar(20) not null,
    email      varchar(30) not null,
    unique(username),
    unique(email)
);


-- table: role
create table role
(
    id serial
        constraint role_pk
            primary key,
    role_type varchar(20) not null
);


-- table for mapping user and role
create table user_role
(
    user_id integer not null
        constraint user_role_user_id_fk
            references "user" on update cascade,
    role_id integer not null
        constraint user_role_role_id_fk
            references "role" on update cascade,
    unique (user_id, role_id)
);


-- table: product
create table product
(
    id serial
        constraint product_pk
            primary key,
    product_name varchar(30) not null
);


-- table: category
create table category
(
    id serial
        constraint category_pk
            primary key,
    category_name varchar(30) not null,
    description varchar(100),
    unique (category_name)
);


-- table for mapping product and category
create table category_product
(
    category_id integer not null
        constraint category_product_category_id_fk
            references category on update cascade on delete cascade,
    product_id integer not null
        constraint category_product_product_id_fk
            references product on update cascade
);


-- table: store
create table store
(
    id serial
        constraint store_pk
            primary key,
    store_name varchar(100) not null
);


-- table: price
create table price
(
    id serial
        constraint price_pk
            primary key,
    unit_price integer not null,
    date timestamp not null,
    product_id integer not null
        constraint price_product_id_fk
            references product on update cascade,
    store_id integer not null
        constraint price_store_id_fk
            references store on update cascade
);