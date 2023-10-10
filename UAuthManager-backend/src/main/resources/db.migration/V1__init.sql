insert into role (name)
values ('ROLE_USER'), ('ROLE_ADMIN');

insert into users (user_name, password, email)
values
('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'user@gmail.com'),
('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'admin@gmail.com');

insert into user_role (user_id, role_id)
values
    (1, 1),
    (2, 2);