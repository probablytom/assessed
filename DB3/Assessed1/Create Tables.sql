-- William Thomas Wallis, University of Glasgow, Matric. No. 2025138, 2015, for course DB3
-- Still to be commented...

CREATE TABLE Band (
	bid integer PRIMARY KEY,
	name character varying(50) NOT NULL,
	country character varying(20),
	webpage character varying(120)
)

CREATE TABLE Member (
	mid integer PRIMARY KEY,
	name character varying(50),
	stillalive character
)

CREATE TABLE Release (
	rid integer PRIMARY KEY,
	bid integer REFERENCES Band,
	title character varying(120),
	year integer,
	type character varying(10),
	rating integer
)

CREATE TABLE Song (
	rid integer REFERENCES Release,
	title character varying(120),
	cdbonus character
)

-- Should Memberof have a primary key? Can't see what it should be; the years have assumptions sayingt hey can be null, and no combination of the other three fields is guarenteed to be unique...
CREATE TABLE Memberof (
	mid integer REFERENCES Member,
	bid integer REFERENCES Band,
	startyear integer,
	endyear integer,
	instrument character varying(15)
)