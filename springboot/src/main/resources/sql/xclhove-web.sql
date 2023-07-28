create table if not exists SystemConfig
(
    id    int auto_increment
        primary key,
    name  varchar(255) not null,
    value varchar(255) null
);

create table if not exists SystemStatus
(
    id    int auto_increment
        primary key,
    name  varchar(255)         not null,
    value tinyint(1) default 0 not null,
    constraint name_unique
        unique (name)
)
    collate = utf8mb4_unicode_ci;

create table if not exists keyword
(
    id      int         null,
    linkId  int         null,
    keyWord varchar(20) null
);

create table if not exists link
(
    id   int          null,
    name varchar(20)  null,
    url  varchar(500) null
);


