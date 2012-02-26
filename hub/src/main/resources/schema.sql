create table message(
	id identity not null primary key,
	digipostAddress varchar(255),
	personalIdentificationNumber varchar(11),
	name varchar(255),
	addressline1 varchar(80),
	addressline2 varchar(80),
	zipcode varchar(10),
	city varchar(50),
	country varchar(50),
	content blob,
	status varchar(20)
);