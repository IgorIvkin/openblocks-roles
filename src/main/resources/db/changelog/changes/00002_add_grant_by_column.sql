--liquibase formatted sql
--changeset igor:2

alter table user_role add column grant_by character varying(255) not null;

create index user_role_grant_by on user_role (grant_by);

--rollback alter table user_role drop column grant_by;