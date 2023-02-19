--liquibase formatted sql
--changeset igor:1

create table role
(
    code  character varying(255) not null,
    label character varying(255) not null,
    constraint role_pk primary key (code)
);

create index role_label_idx on role (label text_pattern_ops);

create table user_role
(
    id         bigserial                not null,
    user_name  character varying(255)   not null,
    role_code  character varying(255)   not null,
    created_at timestamp with time zone not null default NOW(),
    constraint user_role_pk primary key (id)
);

create index user_role_user_name_idx on user_role (user_name);

alter table user_role add constraint user_role_role_code_fk foreign key (role_code) references role (code);

--rollback DROP TABLE user_role;
--rollback DROP TABLE role;