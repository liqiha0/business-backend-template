create domain non_blank_text as text
    check (length(btrim(value)) > 0);

create domain positive_int as integer
    check (value > 0);

create domain non_negative_int as integer
    check (value >= 0);

create table principal
(
    id                 uuid primary key         not null,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    role_ids           jsonb                    not null,
    realm              non_blank_text,
    disabled           boolean                  not null
);

create table identity
(
    id           uuid primary key not null,
    principal_id uuid references principal (id),
    realm        non_blank_text
);

create table username_identity
(
    id       uuid primary key references identity (id) not null,
    realm    non_blank_text,
    username non_blank_text                            not null,
    unique nulls not distinct (realm, username)
);

create table phone_identity
(
    id    uuid primary key references identity (id) not null,
    realm non_blank_text,
    phone non_blank_text                            not null,
    unique nulls not distinct (realm, phone)
);

create table wechat_identity
(
    id       uuid primary key references identity (id) not null,
    realm    non_blank_text,
    open_id  non_blank_text                            not null,
    union_id non_blank_text,
    app_id   non_blank_text,
    unique nulls not distinct (realm, open_id)
);

create table credential
(
    id           uuid primary key not null,
    principal_id uuid references principal (id)
);

create table password_credential
(
    id            uuid primary key references credential (id) not null,
    password_hash non_blank_text                              not null
);


create table role
(
    id                 uuid primary key         not null,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    display_name       non_blank_text           not null,
    authority          jsonb                    not null,
    is_builtin         boolean                  not null
);

create table token
(
    access_token       non_blank_text primary key not null,
    created_by         uuid,
    created_date       timestamp with time zone   not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone   not null,
    principal_id       uuid                       not null
);

create index idx_token_principal_id on token (principal_id);

create table system_configuration
(
    key_group          non_blank_text           not null,
    key_name           non_blank_text           not null,
    created_by         uuid,
    created_date       timestamp with time zone not null,
    last_modified_by   uuid,
    last_modified_date timestamp with time zone not null,
    value              jsonb                    not null,
    primary key (key_group, key_name)
);
