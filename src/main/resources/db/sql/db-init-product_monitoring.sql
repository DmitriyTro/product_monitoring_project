-- table: user
create table "user"
(
    id serial
        constraint user_pk
            primary key,
    username varchar(255) not null,
    password varchar(255) not null,
    first_name varchar(65),
    last_name varchar(65),
    unique(username)
);


-- table: role
create table role
(
    id serial
        constraint role_pk
            primary key,
    role_name varchar(100) not null
);


-- table for mapping user and role
create table user_role
(
    user_id integer not null
        constraint user_role_user_id_fk
            references "user",
    role_id integer not null
        constraint user_role_role_id_fk
            references "role",
    unique (user_id, role_id)
);


-- table: product
create table product
(
    id serial
        constraint product_pk
            primary key,
    product_name varchar(65) not null
);


-- table: category
create table category
(
    id serial
        constraint category_pk
            primary key,
    category_name varchar(65) not null,
    description text
);


-- table for mapping product and category
create table category_product
(
    category_id integer not null
        constraint product_category_category_id_fk
            references category,
    product_id integer not null
        constraint product_category_product_id_fk
            references product
);


-- table: store
create table store
(
    id serial
        constraint store_pk
            primary key,
    store_name varchar(65) not null
);


-- table: price
create table price
(
    id serial
        constraint price_pk
            primary key,
    unit_price integer,
    date timestamp,
    product_id integer not null
        constraint price_product_id_fk
            references product,
    store_id integer not null
        constraint price_store_id_fk
            references store
);