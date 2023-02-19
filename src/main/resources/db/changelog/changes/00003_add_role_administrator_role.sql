--liquibase formatted sql
--changeset igor:3

insert into role(code, label) values('ROLES_ADMINISTRATOR', 'Администратор ролей');

insert into user_role(user_name, role_code, created_at, grant_by)
values ('service-account-infra-backend', 'ROLES_ADMINISTRATOR', NOW(), 'service-account-infra-backend')

--rollback alter table user_role drop column grant_by;