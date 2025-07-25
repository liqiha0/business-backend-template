create table sms_code
(
    id                 uuid primary key,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    phone              text                     not null,
    code               varchar(6)               not null,
    type               text                     not null,
    expired_at         timestamp with time zone not null
);
