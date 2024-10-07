create table one_time_password
(
    id                 uuid primary key,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    type               non_blank_text           not null,
    scene              non_blank_text           not null,
    recipient          non_blank_text           not null,
    code               varchar(6)               not null,
    expired_at         timestamp with time zone not null
);