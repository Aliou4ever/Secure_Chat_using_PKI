Create database PKI2014;

create table PKI2014.Certificat (
    id int primary key auto_increment ,
    login varchar(20),
    certificat longblob,
    chatPort int,
    certPort int,
    certToRevok boolean NOT NULL DEFAULT FALSE 
);

create table PKI2014.User(
    id int primary key auto_increment ,
    login varchar(20),
    password longblob
);
