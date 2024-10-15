create table account
(
    id                 uuid primary key         not null,
    display_name       text                     not null,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    is_disabled        boolean                  not null
);

create table admin_account
(
    id       uuid references account (id) primary key not null,
    username text                                     not null,
    password text                                     not null,
    role_ids jsonb                                    not null
);

create table wechat_binding
(
    id          uuid primary key not null,
    open_id     text             not null,
    session_key text             not null,
    union_id    text,
    unique (open_id)
);

create table alipay_binding
(
    id           uuid primary key not null,
    open_id      text             not null,
    access_token text             not null,
    unique (open_id)
);

create table user_account
(
    id                uuid references account (id) primary key not null,
    phone_number      text,
    wechat_binding_id uuid references wechat_binding (id),
    alipay_binding_id uuid references alipay_binding (id),
    avatar            text,
    unique (phone_number)
);

create table role
(
    id                 uuid primary key         not null,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    display_name       text                     not null,
    authority          jsonb                    not null
);

create table token
(
    access_token       text primary key         not null,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    user_id            uuid                     not null,
    refresh_token      text,
    unique (refresh_token)

);

