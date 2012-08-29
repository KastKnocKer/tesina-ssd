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
	publicIP varchar(20) not null,
	localIP varchar(20) not null,
	rmiregistryPort int(11) not null default 1099,
	clientPort int(11) not null,
	lastConnection timestamp not null default NOW(),
	status enum('ONLINE','BUSY','AWAY','OFFLINE') not null default 'OFFLINE',
	
	primary key (idUser),
	KEY `FK1` (`idUser`),
	CONSTRAINT `FK1` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS FRIENDSHIP(
	idUserA int(11) not null,
	idUserB int(11) not null,
	linkType enum('RICHIESTA_AB','RICHIESTA_BA', 'ATTIVA') not null default 'RICHIESTA_AB',
	
	primary key (idUserA,idUserB)
)ENGINE=InnoDB;


INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Fabio', 'Pierazzi', 'biofrost88@gmail.com', 'LaMagicaBia', 'bio');
INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Andrea', 'Castelli', 'kastknocker@gmail.com', 'KastKnocKer', 'asd');
INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Matteo', 'Renzi', 'mattexxx@gmail.com', 'IlManico', 'asd');
INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Marco', 'Guerri', 'marco@gmail.com', 'Marco', 'asd');
INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Marco', 'Guerri', 'A@gmail.com', 'A', 'asd');
INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Marco', 'Guerri', 'B@gmail.com', 'B', 'asd');
INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Marco', 'Guerri', 'C@gmail.com', 'C', 'asd');
INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Marco', 'Guerri', 'D@gmail.com', 'D', 'asd');
INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Marco', 'Guerri', 'E@gmail.com', 'E', 'asd');
INSERT INTO `sip_db`.`user` (`idUser`, `nome`, `cognome`, `email`, `nickname`, `password`) VALUES (NULL, 'Marco', 'Guerri', 'F@gmail.com', 'F', 'asd');
