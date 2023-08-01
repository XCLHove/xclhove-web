create table if not exists OneDrive
(
    id            int auto_increment
        primary key,
    name          varchar(255) null,
    client_id     varchar(255) null,
    client_secret varchar(255) null,
    redirect_uri  varchar(255) null,
    access_token  text         null,
    refresh_token text         null,
    driveId       varchar(255) null
)
    collate = utf8mb4_unicode_ci;

create table if not exists OneDriveItem
(
    id          int auto_increment
        primary key,
    name        varchar(255)          null,
    itemId      char(34)   default '' not null,
    size        varchar(10)           null,
    isFile      tinyint(1) default 0  null,
    downloadUrl text                  null,
    parentId    int                   null,
    isRoot      tinyint(1) default 0  not null,
    constraint unique_itemId
        unique (itemId),
    constraint parentId
        foreign key (parentId) references OneDriveItem (id)
            on update cascade on delete cascade
)
    collate = utf8mb4_unicode_ci;

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


