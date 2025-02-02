insert into request_types (id, name, description) values ('U1', 'U1', 'descr');
insert into request_types (id, name, description) values ('U2', 'U2', 'descr');

insert into requests (id, type_id, template_id) values ('00000000-0000-0000-0000-a00000000001', 'U1', 'Request 1');
insert into requests (id, type_id, template_id) values ('00000000-0000-0000-0000-a00000000002', 'U1', 'Request 2');
insert into requests (id, type_id, template_id) values ('00000000-0000-0000-0000-a00000000003', 'U1', 'Request 3');
insert into requests (id, type_id, template_id) values ('00000000-0000-0000-0000-a00000000004', 'U2', 'Request 4');
insert into requests (id, type_id, template_id) values ('00000000-0000-0000-0000-a00000000005', 'U2', 'Request 5');
insert into requests (id, type_id, template_id) values ('00000000-0000-0000-0000-a00000000006', 'U2', 'Request 6');

insert into partners (id, name, description, request_uuid) values ('00000000-0000-0000-0000-f00000000001', 'P1', 'descr', '00000000-0000-0000-0000-a00000000001');
insert into partners (id, name, description, request_uuid) values ('00000000-0000-0000-0000-f00000000002', 'P2', 'descr', '00000000-0000-0000-0000-a00000000001');

insert into partners (id, name, description, request_uuid) values ('00000000-0000-0000-0000-f00000000003', 'P3', 'descr', '00000000-0000-0000-0000-a00000000002');
insert into partners (id, name, description, request_uuid) values ('00000000-0000-0000-0000-f00000000004', 'P4', 'descr', '00000000-0000-0000-0000-a00000000002');
