create table account
(
    id                 uuid primary key         not null,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    role_ids           jsonb                    not null,
    disabled           boolean                  not null
);

create table role
(
    id                 uuid primary key         not null,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    display_name       text                     not null,
    authority          jsonb                    not null,
    is_builtin         boolean                  not null
);

create table token
(
    access_token       text primary key         not null,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    account_id         uuid                     not null,
    refresh_token      text,
    unique (refresh_token)
);

CREATE TABLE credential
(
    id         UUID primary key NOT NULL,
    account_id uuid references account (id)
);

CREATE TABLE phone_credential
(
    id    UUID primary key references credential (id) NOT NULL,
    phone text                                        not null
);

CREATE TABLE username_password_credential
(
    id            UUID primary key references credential (id) NOT NULL,
    username      text                                        not null,
    password_hash text                                        not null
);

CREATE TABLE wechat_credential
(
    id          UUID primary key references credential (id) NOT NULL,
    open_id     text                                        not null,
    session_key text                                        not null,
    union_id    text
);