create table users
(
    id                   varchar(45) primary key,
    full_name            varchar(100) not null,
    avatar_url           varchar(255) not null,
    oauth2_provider_name varchar(50),
    oauth2_provider_id   varchar(50),
    email                varchar(255) unique,
    password             varchar(255),
    created_at           datetime,
    updated_at           datetime
);
create table user_tokens
(
    id                   varchar(45) primary key,
    user_id              varchar(45),
    access_token         varchar(100) unique not null,
    access_token_expiry  datetime,
    refresh_token        varchar(100) unique not null,
    refresh_token_expiry datetime,
    created_at           datetime,
    updated_at           datetime
);
create table urls
(
    id             varchar(45) primary key,
    user_id        varchar(45),
    original_url   text               not null,
    short_url_code varchar(20) unique not null,
    created_at     datetime,
    updated_at     datetime
);