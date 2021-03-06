﻿-- DROP TABLE Breed;
-- DROP TABLE Show;
-- DROP TABLE Kennel;
-- DROP TABLE Dog;
-- DROP TABLE Owner;
-- DROP TABLE Attendance;


CREATE TABLE Breed (
	breedname VARCHAR(64) PRIMARY KEY
);

CREATE TABLE Show (
	showname  VARCHAR(64) NOT NULL,
	opendate  VARCHAR(12) NOT NULL,
	closedate VARCHAR(12),
	PRIMARY KEY(showname, opendate)
);

CREATE TABLE Kennel (
	kennelname VARCHAR(64) PRIMARY KEY,
	address    VARCHAR(64),
	phone      VARCHAR(16)
);

CREATE TABLE Owner (
	ownerid INT            PRIMARY KEY,
	name    VARCHAR(32)    NOT NULL,
	phone   VARCHAR(16)
);

CREATE TABLE Dog (
	dogid      INT	       PRIMARY KEY,
	name       VARCHAR(32),
	ownerid    INT         REFERENCES Owner(ownerid),
	kennelname VARCHAR(64),
	breedname  VARCHAR(64),
	mothername VARCHAR(64),
	fathername VARCHAR(64)
);

CREATE TABLE Attendance ( -- RELATIONSHIP BETWEEN Dog AND Show
	dogid    INT           REFERENCES Dog(dogid),
	showname VARCHAR(64),
	opendate VARCHAR(12),
	place    INT,
	FOREIGN KEY(showname, opendate) REFERENCES Show(showname, opendate),
	PRIMARY KEY(showname, opendate, place)
);
