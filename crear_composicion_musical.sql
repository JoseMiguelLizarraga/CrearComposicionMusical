

drop database if exists crear_composicion_musical;
create database crear_composicion_musical default character set utf8 default collate utf8_general_ci;
use crear_composicion_musical;   

-- ===================================================================================================================>>>>>>

create table parametros_login(
	id int primary key not null auto_increment,
	duracion_token_milisegundos int,
	mensaje_error_autenticacion varchar(100),
	mensaje_token_expirado varchar(100),
	mensaje_usuario_deshabilitado varchar(100),
	mensaje_error_interno varchar(100)
);

-- 3.600.000  equivale a una hora. Ej: multiplicar 3.600.000 * 24(horas) = 86400000 

INSERT INTO parametros_login(id, duracion_token_milisegundos, mensaje_error_autenticacion, mensaje_token_expirado, mensaje_usuario_deshabilitado, mensaje_error_interno) 
VALUES (
	1, 
	86400000, 
	'Error de autenticación. Username o password incorrecto',
	'Se termino el tiempo limite de uso del usuario',
	'Usuario deshabilitado',
	'Error interno'
);

-- ===================================================================================================================>>>>>>

create table usuario(
	id int primary key not null auto_increment,
	username varchar(40),
	password varchar(120),
	largo_password int,
	nombre varchar(40),
	apellido_paterno varchar(40),
	apellido_materno varchar(40),
	rut varchar(40),
	telefono varchar(40),
	activo boolean,
	visible boolean  
);

create table cargo(
	id int primary key not null auto_increment,
	authority varchar(40),
	descripcion varchar(40),
	activo boolean,
	visible boolean
);

create table usuario_cargo(
	id int primary key not null auto_increment,
	usuario_id int,
	cargo_id int,
	foreign key(usuario_id) references usuario(id),
	foreign key(cargo_id) references cargo(id)    
);

-- ===================================================================================================================>>>>>>

create table cabecera_composicion_musical(
	id int primary key not null auto_increment,
	titulo varchar(100),
	autor varchar(100),
	visible boolean,
	usuario_id int,
	foreign key(usuario_id) references usuario(id)
);

create table detalle_composicion_musical(
	id int primary key not null auto_increment,
	orden int,
	cadenaListaSubDetalles varchar(3000),
	cabecera_composicion_musical_id int,
	foreign key(cabecera_composicion_musical_id) references cabecera_composicion_musical(id)
);

-- ===================================================================================================================>>>>>>

-- Inserto los cargos

insert into cargo(id, authority, descripcion, activo, visible) values(1, 'ROLE_SUPER_ADMIN', 'Super Administrador IT', 1, 0);

-- ===================================================================================================================>>>>>>
-- Creo un usuario Administrador y su(s) cargo(s)
-- admin 
-- 123456789

INSERT INTO usuario(id, username, password, largo_password, nombre, apellido_paterno, apellido_materno, rut, telefono, activo, visible) 
VALUES (1, 'admin', '$2a$11$oTiVF3rePm8YqI//.8N4Bup6P3B3509KbN6imVrfvTKHUgy2VZgJG', 4, 'admin', null, null, '11.111.111-1', null, 1, 0);


INSERT INTO usuario_cargo(id, usuario_id, cargo_id) VALUES 
(1, (select id from usuario where username = 'admin'), (select id from cargo where descripcion = 'Super Administrador IT'));

-- ===================================================================================================================>>>>>>
-- Creo otro usuario 

INSERT INTO usuario(id, username, password, largo_password, nombre, apellido_paterno, apellido_materno, rut, telefono, activo, visible) 
VALUES (2, 'pedro', '$2a$11$oTiVF3rePm8YqI//.8N4Bup6P3B3509KbN6imVrfvTKHUgy2VZgJG', 4, 'pedro', null, null, '12.222.222-2', null, 1, 0);


INSERT INTO usuario_cargo(id, usuario_id, cargo_id) VALUES 
(2, (select id from usuario where username = 'pedro'), (select id from cargo where descripcion = 'Super Administrador IT'));

-- ===================================================================================================================>>>>>>

/*
create table sub_detalle_composicion_musical(
	id int primary key not null auto_increment,
	nota varchar(5),
	orden int,
	duracion int,
	detalle_composicion_musical_id int,
	foreign key(detalle_composicion_musical_id) references detalle_composicion_musical(id)
);
*/

