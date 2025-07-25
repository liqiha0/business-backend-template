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