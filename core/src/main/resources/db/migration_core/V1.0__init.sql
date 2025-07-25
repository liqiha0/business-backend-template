create table principal
(
    id                 uuid primary key         not null,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    role_ids           jsonb                    not null,
    disabled           boolean                  not null
);

create table identity
(
    id           uuid primary key not null,
    principal_id uuid references principal (id)
);

create table username_identity
(
    id       uuid primary key references identity (id) not null,
    username text unique                               not null
);

create table phone_identity
(
    id    uuid primary key references identity (id) not null,
    phone text unique                               not null
);

create table wechat_identity
(
    id       uuid primary key references identity (id) not null,
    open_id  text unique                               not null,
    union_id text unique
);

create table credential
(
    id           uuid primary key not null,
    principal_id uuid references principal (id)
);

create table password_credential
(
    id            uuid primary key references credential (id) not null,
    password_hash text                                        not null
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
    principal_id       uuid                     not null,
    refresh_token      text,
    unique (refresh_token)
);

create index idx_token_principal_id on token (principal_id);

create table system_configuration
(
    key_group          text                     not null,
    key_name           text                     not null,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    value              text                     not null,
    primary key (key_group, key_name)
);