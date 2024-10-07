create table "order"
(
    id                 non_blank_text primary key not null,
    created_by         uuid,
    created_date       timestamp with time zone   not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone   not null,
    principal_id       uuid                       not null,
    amount             numeric(19, 2)             not null,
    payment_status     non_blank_text             not null,
    payment_channel    non_blank_text
);