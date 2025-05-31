alter table admin_account drop column role_ids;

alter table account
    add column role_ids jsonb not null default '[]';

alter table account
    alter column role_ids drop default;

alter table role
    add column is_builtin boolean not null;

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

create table "order"
(
    id                 text primary key         not null,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    user_id            uuid                     not null,
    amount             numeric(15, 2)           not null,
    payment_status     text                     not null,
    payment_channel    text
);