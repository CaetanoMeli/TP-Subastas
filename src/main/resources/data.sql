drop table if exists personas;

CREATE TABLE personas (
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	documento VARCHAR(20) NOT NULL,
	nombre VARCHAR(150) NOT NULL,
	apellido VARCHAR(150) NOT NULL,
	direccion VARCHAR(250),
	estado VARCHAR(15) CONSTRAINT chkEstado CHECK (estado in ('activo', 'inactivo')),
	foto VARBINARY(max)
)