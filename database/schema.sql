create table users
(
    id         varchar(45) primary key,
    full_name  varchar(100) not null,
    avatar_url varchar(255) not null,
    role       varchar(50)  not null,
    email      varchar(255) unique,
    password   varchar(255),
    created_at datetime,
    updated_at datetime
);
create index idx_user_email ON users (email);

create table user_tokens
(
    id                   varchar(45) primary key,
    user_id              varchar(45),
    access_token         varchar(100) unique not null,
    access_token_expiry  datetime,
    refresh_token        varchar(100) unique not null,
    refresh_token_expiry datetime,
    created_at           timestamp,
    updated_at           timestamp
);
create table urls
(
    id             varchar(45) primary key,
    user_id        varchar(45),
    original_url   text               not null,
    short_url_code varchar(20) unique not null,
    expires_at     timestamp,
    created_at     timestamp,
    updated_at     timestamp
);
create table url_analytics
(
    id          varchar(45) primary key,
    url_id      varchar(45) not null,
    ip_address  varchar(45),
    country     varchar(100),
    city        varchar(100),
    user_agent  text,
    device_type varchar(50),
    referrer    text,
    accessed_at timestamp,
    created_at  timestamp,
    updated_at  timestamp
);

create index idx_url_id on url_analytics (url_id);
create index idx_device_type on url_analytics (device_type);
create index idx_referrer on url_analytics (referrer);
create index idx_accessed_at on url_analytics (accessed_at);

alter table url_analytics
    partition by list columns(country)
    subpartition by hash (city) subpartitions 5 (
    partition p_us values in ('us'),
    partition p_uk values in ('uk'),
    partition p_vn values in ('vn'),
    partition p_other values in (default)
    );