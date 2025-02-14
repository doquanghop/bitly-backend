create table account_levels
(
    id               varchar(45) primary key,
    name             varchar(100)   not null,
    max_urls         int            not null,
    max_storage_days int            not null,
    monthly_price    decimal(10, 2) not null,
    yearly_price     decimal(10, 2) not null,
    created_at       timestamp,
    updated_at       timestamp
);
create table users
(
    id         varchar(45) primary key,
    full_name  varchar(100) not null,
    avatar_url varchar(255) not null,
    role       varchar(50)  not null,
    email      varchar(255) unique,
    password   varchar(255),
    created_at timestamp,
    updated_at timestamp
);
create index idx_user_email ON accounts (email);
create table user_subscriptions
(
    id               varchar(45) primary key,
    user_id          varchar(45) not null,
    account_level_id varchar(45) not null,
    start_date       datetime    not null,
    end_date         datetime    not null,
    status           varchar(20),
    created_at       timestamp,
    updated_at       timestamp
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
    partition by list columns (country)
        subpartition by hash (city) subpartitions 5 (
        partition p_us values in ('us'),
        partition p_uk values in ('uk'),
        partition p_vn values in ('vn'),
        partition p_other values in (default)
        );