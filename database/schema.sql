create table urls
(
    id           varchar(45) primary key,
    original_url text               not null,
    short_url    varchar(20) unique not null,
    created_at   datetime,
    updated_at   datetime
);