
CREATE TABLE public.atleta (
	id serial4 NOT NULL,
	nombre varchar(100) NULL,
	apellido varchar(100) NULL,
	direccion varchar(100) NULL,
	telefono varchar(25) NULL,
	competencia varchar(25) NULL,
	CONSTRAINT atleta_pkey PRIMARY KEY (id)
);


CREATE TABLE public.ejercicio (
	id serial4 NOT NULL,
	nombre varchar(200) NULL,
	descripcion varchar(200) NULL,
	tipo varchar(200) NULL,
	CONSTRAINT ejercicio_pkey PRIMARY KEY (id)
);


CREATE TABLE public.perfil (
	id serial4 NOT NULL,
	atleta_id int4 NULL,
	correo varchar(200) NULL,
	contraseña varchar(200) NULL,
	pin int4 NULL,
	CONSTRAINT perfil_pkey PRIMARY KEY (id),
	CONSTRAINT perfil_atleta_id_fkey FOREIGN KEY (atleta_id) REFERENCES public.atleta(id)
);



CREATE TABLE public.rutina (
	id serial4 NOT NULL,
	atleta_id int4 NULL,
	fecha date NULL,
	estado varchar(25) NULL,
	temperatura_promedio numeric DEFAULT 0 NULL,
	humedad_promedio numeric DEFAULT 0 NULL,
	presion_promedio numeric DEFAULT 0 NULL,
	fecha_completada date NULL,
	CONSTRAINT rutina_pkey PRIMARY KEY (id),
	CONSTRAINT rutina_atleta_id_fkey FOREIGN KEY (atleta_id) REFERENCES public.atleta(id)
);


CREATE TABLE public.rutina_ejercicio (
	id serial4 NOT NULL,
	rutina_id int4 NULL,
	ejercicio_id int4 NULL,
	cantidad_series int4 NULL,
	intensidad varchar(100) NULL,
	descanso varchar(100) NULL,
	CONSTRAINT rutina_ejercicio_pkey PRIMARY KEY (id),
	CONSTRAINT rutina_ejercicio_ejercicio_id_fkey FOREIGN KEY (ejercicio_id) REFERENCES public.ejercicio(id),
	CONSTRAINT rutina_ejercicio_rutina_id_fkey FOREIGN KEY (rutina_id) REFERENCES public.rutina(id)
);


CREATE TABLE public.serie (
	id serial4 NOT NULL,
	rutina_ejercicio_id int4 NULL,
	numero_serie int4 NULL,
	tiempo numeric NULL,
	velocidad numeric NULL,
	fre_cardiaca numeric NULL,
	CONSTRAINT serie_pkey PRIMARY KEY (id),
	CONSTRAINT serie_rutina_ejercicio_id_fkey FOREIGN KEY (rutina_ejercicio_id) REFERENCES public.rutina_ejercicio(id),
	CONSTRAINT serie_rutina_ejercicio_id_fkey1 FOREIGN KEY (rutina_ejercicio_id) REFERENCES public.rutina_ejercicio(id)
);
