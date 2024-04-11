# API-Backend

CREATE TABLE IF NOT EXISTS Atleta (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(25),
    apellido VARCHAR(25),
    direccion VARCHAR(25),
    telefono VARCHAR(25),
    competencia VARCHAR(25)
);


CREATE TABLE IF NOT EXISTS Perfil (
    id SERIAL PRIMARY KEY,
    atleta_id INT REFERENCES Atleta(id),
    correo VARCHAR(25),
    contraseña VARCHAR(25)
);

CREATE TABLE IF NOT EXISTS Rutina (
    id SERIAL PRIMARY KEY,
    atleta_id INT REFERENCES Atleta(id),
    fecha DATE,
    estado VARCHAR(25)
);

CREATE TABLE IF NOT EXISTS Ejercicio (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(20),
    descripcion VARCHAR(100),
    tipo VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS Rutina_Ejercicio (
    id SERIAL PRIMARY KEY,
    rutina_id INT REFERENCES Rutina(id),
    ejercicio_id INT REFERENCES Ejercicio(id),
    cantidad_series INT
);



CREATE TABLE IF NOT EXISTS Serie (
    id SERIAL PRIMARY KEY,
    rutina_ejercicio_id INT REFERENCES Rutina_Ejercicio(id),
    numero_serie INT,
    FOREIGN KEY (rutina_ejercicio_id) REFERENCES Rutina_Ejercicio(id),
    UNIQUE (rutina_ejercicio_id)
);

CREATE TABLE IF NOT EXISTS Rendimiento (
    id SERIAL PRIMARY KEY,
    serie_id INT UNIQUE REFERENCES Serie(id),
    velocidad_promedio DECIMAL,
    tiempo DECIMAL,
    pasos INT
);

CREATE TABLE IF NOT EXISTS Biometria (
    id SERIAL PRIMARY KEY,
    serie_id INT UNIQUE REFERENCES Serie(id),
    fre_cardiaca_promedio DECIMAL,
    presion_promedio DECIMAL
);

CREATE TABLE IF NOT EXISTS Ambiente (
    id SERIAL PRIMARY KEY,
    serie_id INT UNIQUE REFERENCES Serie(id),
    temperatura_promedio DECIMAL,
    humedad_promedio DECIMAL
);



-- Inserts para la tabla Atleta
INSERT INTO Atleta (nombre, apellido, direccion, telefono, competencia) 
VALUES ('Juan', 'Perez', 'Calle 123', '555-1234', '100m');

-- Inserts para la tabla Perfil
INSERT INTO Perfil (atleta_id, correo, contraseña)
VALUES (1, 'juan@example.com', 'password123');

-- Inserts para la tabla Ejercicio
INSERT INTO Ejercicio (nombre, descripcion, tipo)
VALUES ('Flexiones', 'Ejercicio de fuerza para el torso', 'Fuerza');

-- Inserts para la tabla Rutina
INSERT INTO Rutina (atleta_id, fecha, estado)
VALUES (1, '2024-04-10', 'Activa');

-- Inserts para la tabla Rutina_Ejercicio
INSERT INTO Rutina_Ejercicio (rutina_id, ejercicio_id, cantidad_series)
VALUES (1, 1, 3);

-- Inserts para la tabla Serie
INSERT INTO Serie (rutina_ejercicio_id, numero_serie)
VALUES (1, 1);

-- Inserts para la tabla Rendimiento
INSERT INTO Rendimiento (serie_id, velocidad_promedio, tiempo, pasos)
VALUES (1, 5.0, 10.0, 20);

-- Inserts para la tabla Biometria
INSERT INTO Biometria (serie_id, fre_cardiaca_promedio, presion_promedio)
VALUES (1, 80.0, 120.0);

-- Inserts para la tabla Ambiente
INSERT INTO Ambiente (serie_id, temperatura_promedio, humedad_promedio)
VALUES (1, 25.0, 50.0);
