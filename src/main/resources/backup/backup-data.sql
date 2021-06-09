DROP TABLE if EXISTS paises;
DROP TABLE if EXISTS personas;
DROP TABLE if EXISTS empleados;
DROP TABLE if EXISTS sectores;
DROP TABLE if EXISTS clientes;
DROP TABLE if EXISTS duenios;
DROP TABLE if EXISTS subastadores;
DROP TABLE if EXISTS subastas;
DROP TABLE if EXISTS productos;
DROP TABLE if EXISTS fotos;
DROP TABLE if EXISTS catalogos;
DROP TABLE if EXISTS itemsCatalogo;
DROP TABLE if EXISTS asistentes;
DROP TABLE if EXISTS pujos;
DROP TABLE if EXISTS registroDeSubasta;

CREATE TABLE paises(
	numero INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR (250) NOT NULL,
	nombreCorto VARCHAR (250) NULL,
	capital VARCHAR (250) NOT NULL ,
	nacionalidad VARCHAR (250) NOT NULL,
	idiomas VARCHAR (150) NOT NULL
);

CREATE TABLE personas (
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	documento VARCHAR(20) NOT NULL,
	nombre VARCHAR(150) NOT NULL,
	apellido VARCHAR(150) NOT NULL,
	direccion VARCHAR(250),
	telefono VARCHAR(250),
	contraseña VARCHAR(250),
	codigo INT,
	estado VARCHAR(15) CONSTRAINT chkEstado CHECK (estado in ('activo', 'inactivo')),
	foto VARBINARY(max)
);

CREATE TABLE empleados(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	cargo VARCHAR (100),
	sector INT NULL
);

CREATE TABLE sectores(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	nombreSector VARCHAR (150) NOT NULL,
	codigoSector VARCHAR (10) NULL,
	responsableSector INT NULL,
	CONSTRAINT fk_sectores_empleados FOREIGN KEY (responsableSector) REFERENCES empleados
);

CREATE TABLE clientes(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	numeroPais INT,
	admitido VARCHAR (2) CONSTRAINT chkAdmitido CHECK (admitido IN ('si','no')),
	categoria VARCHAR (10) CONSTRAINT chkCategoria CHECK (categoria IN ('comun', 'especial', 'plata', 'oro', 'platino')),
	verificador INT NOT NULL,
	CONSTRAINT fk_clientes_personas FOREIGN KEY (identificador) REFERENCES personas,
	CONSTRAINT fk_clientes_empleados FOREIGN KEY (verificador) REFERENCES empleados (identificador),
	CONSTRAINT fk_clientes_paises FOREIGN KEY (numeroPais) REFERENCES paises (numero)
);

CREATE TABLE duenios(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	numeroPais INT,
	verificaciónFinanciera VARCHAR (2) CONSTRAINT chkVF CHECK(verificaciónFinanciera IN ('si','no')),
	verificaciónJudicial VARCHAR (2) CONSTRAINT chkVJ CHECK (verificaciónJudicial IN ('si','no')),
	calificacionRiesgo INT CONSTRAINT chkCR CHECK(calificacionRiesgo IN (1,2,3,4,5,6)),
	verificador INT NOT NULL,
	CONSTRAINT fk_duenios_personas FOREIGN KEY (identificador) REFERENCES personas,
	CONSTRAINT fk_duenios_empleados FOREIGN KEY (verificador) REFERENCES empleados (identificador)
);

CREATE TABLE subastadores(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	matricula VARCHAR(15),
	region VARCHAR(50),
	CONSTRAINT fk_subastadores_personas FOREIGN KEY (identificador) REFERENCES personas
);

CREATE TABLE subastas(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	--las subastas tiene al menos 10 dias de anticipación al momento de crearlas.
	fecha DATE CONSTRAINT chkFecha CHECK (fecha > TIMESTAMPADD(DAY, 10, SYSDATE())),
	hora TIME NOT NULL,
	estado VARCHAR(10) CONSTRAINT chkES CHECK (estado IN ('abierta','cerrada')),
	subastador INT NULL,
	--direccion de don de se desarrolla el evento.
	ubicacion VARCHAR(350) NULL,
	capacidadAsistentes INT NULL,
	--caracteristica del lugar donde se hacen las subastas
	tieneDeposito VARCHAR(2) CONSTRAINT chkTD CHECK(tieneDeposito IN ('si','no')),
	--caracteristica del lugar donde se hacen las subastas
	seguridadPropia VARCHAR(2) CONSTRAINT chkSP CHECK(seguridadPropia IN ('si','no')),
	categoria VARCHAR(10) CONSTRAINT chkCS CHECK (categoria IN ('comun', 'especial', 'plata', 'oro', 'platino')),
	CONSTRAINT fk_subastas_subastadores FOREIGN KEY (subastador) REFERENCES subastadores(identificador)
);

CREATE TABLE productos(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	fecha DATE,
	disponible VARCHAR(2) CONSTRAINT chkD CHECK (disponible IN ('si','no')),
	--se obtiene despues que un empleado realiza la revision.
	descripcionCatalogo VARCHAR(500) NULL DEFAULT 'No Posee',
	--url que apunta a un documento PDF firmado que contiene la descripción del producto.
	descripcionCompleta VARCHAR(300) NOT NULL,
	revisor INT NOT NULL,
	duenio INT NOT NULL,
	CONSTRAINT fk_productos_empleados FOREIGN KEY (revisor) REFERENCES empleados(identificador),
	CONSTRAINT fk_productos_duenios FOREIGN KEY (duenio) REFERENCES duenios(identificador)
);

CREATE TABLE fotos(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	producto INT NOT NULL,
	foto varbinary (MAX) NOT NULL,
	CONSTRAINT fk_fotos_productos FOREIGN KEY (producto) REFERENCES productos(identificador)
);

create table catalogos(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	descripcion VARCHAR (250) NOT NULL,
	subasta INT NULL,
	responsable INT NOT NULL,
	CONSTRAINT fk_catalogos_empleados FOREIGN KEY (responsable) REFERENCES empleados(identificador),
	CONSTRAINT fk_catalogos_subastas FOREIGN KEY (subasta) REFERENCES subastas(identificador)
);

CREATE TABLE itemsCatalogo(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	catalogo INT NOT NULL,
	producto INT NOT NULL,
	precioBase DECIMAL(18,2) NOT NULL CONSTRAINT chkPB CHECK (precioBase > 0.01),
	comision DECIMAL(18,2) NOT NULL CONSTRAINT chkC CHECK (comision > 0.01),
	subastado VARCHAR(2) CONSTRAINT chkS CHECK (subastado IN ('si','no')),
	CONSTRAINT fk_itemsCatalogo_catalogos FOREIGN KEY (catalogo) REFERENCES catalogos,
	CONSTRAINT fk_itemsCatalogo_productos FOREIGN KEY (producto) REFERENCES productos
);

CREATE TABLE asistentes(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	numeroPostor INT NOT NULL,
	cliente INT NOT NULL,
	subasta INT NOT NULL,
	CONSTRAINT fk_asistentes_clientes FOREIGN KEY (cliente) REFERENCES clientes,
	CONSTRAINT fk_asistentes_subasta FOREIGN KEY (subasta) REFERENCES subastas
);

CREATE TABLE pujos(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	asistente INT NOT NULL,
	item INT NOT NULL,
	importe DECIMAL(18,2) NOT NULL CONSTRAINT chkI CHECK (importe > 0.01),
	ganador VARCHAR(2) DEFAULT 'no' CONSTRAINT chkG CHECK (ganador IN ('si','no')),
	CONSTRAINT fk_pujos_asistentes FOREIGN KEY (asistente) REFERENCES asistentes,
	CONSTRAINT fk_pujos_itemsCatalogo FOREIGN KEY (item) REFERENCES itemsCatalogo
);

CREATE TABLE registroDeSubasta(
	identificador INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	subasta INT NOT NULL,
	duenio INT NOT NULL,
	producto INT NOT NULL,
	cliente INT NOT NULL,
	importe DECIMAL(18,2) NOT NULL CONSTRAINT chkImportePagado CHECK (importe > 0.01),
	comision decimal(18,2) NOT NULL CONSTRAINT chkComisionPagada CHECK (comision > 0.01),
	CONSTRAINT fk_registroDeSubasta_subastas FOREIGN KEY (subasta) REFERENCES subastas,
	CONSTRAINT fk_registroDeSubasta_duenios FOREIGN KEY (duenio) REFERENCES duenios,
	CONSTRAINT fk_registroDeSubasta_producto FOREIGN KEY (producto) REFERENCES productos,
	CONSTRAINT fk_registroDeSubasta_cliente FOREIGN KEY (cliente) REFERENCES clientes
);
