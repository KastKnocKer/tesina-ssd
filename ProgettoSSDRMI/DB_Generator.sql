CREATE DATABASE IF NOT EXISTS SIP_DB;

USE SIP_DB;

CREATE TABLE IF NOT EXISTS USER(
	idUser int(11) not null auto_increment,
	nome varchar(50) not null,
	cognome varchar(50) not null,
	email varchar(250) not null,
	nickname varchar(50) not null,
	password varchar(50) not null,
	
	primary key (idUser),
	unique (email)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS USERSTATUS(
	idUser int(11) not null,
	publicIP varchar(50) not null,
	localIP varchar(50) not null,
	rmiregistryPort int(11) not null,
	lastConnection timestamp not null default NOW(),
	
	primary key (idUser),
	KEY `FK1` (`idUser`),
	CONSTRAINT `FK1` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS FRIENDSHIP(
	idUserA int(11) not null,
	idUserB int(11) not null,
	linkType enum('LIBERO','OCCUPATO', 'PRENOTATO', 'PULIRE') not null default 'LIBERO',
	
	primary key (idUserA,idUserB)
)ENGINE=InnoDB;


INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Fabio', 'Pierazzi', 'biofrost88@gmail.com', 'LaMagicaBia', 'bio');
INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Andrea', 'Castelli', 'kastknocker@gmail.com', 'KastKnocKer', 'asd');
INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Matteo', 'Renzi', 'mattexxx@gmail.com', 'IlManico', 'asd');
INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Marco', 'Guerri', 'marco@gmail.com', 'Marco', 'asd');