CREATE TABLE system_configuration
(
    key_group          text                     NOT NULL,
    key_name           text                     NOT NULL,
    created_by         UUID,
    created_date       TIMESTAMP WITH TIME ZONE not null,
    last_modified_by   UUID,
    last_modified_date TIMESTAMP WITH TIME ZONE NOT NULL,
    value              text                     not null,
    PRIMARY KEY (key_group, key_name)
);