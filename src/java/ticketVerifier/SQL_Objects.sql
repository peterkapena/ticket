/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  217016375
 * Created: 12 Mar 2019
 */

create table Users (
    UserName varchar(20)not null,
    Password varchar(20) not null,
    Email varchar(20) not null,
    PhoneNumber varchar(10) not null,
    Constraint pk primary key(UserName)
)

insert into Users values('Peter','Peter','Peter@gmail.com','0812174767')
insert into Users values('P','P','P@gmail.com','0812174767')

create table Tickets(
    TicketNum int not null,
    LastEntrance varchar(15) not null,
    EntranceTimes int not null,
    Constraint pkTickets primary key(TicketNum)
)

insert into Tickets values(236,'Test',1)


CREATE TABLE passwordreset 
(resetNumber int NOT null GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
token1 VARCHAR(100) NOT NULL,  
token2 VARCHAR(100) NOT NULL, 
token3 VARCHAR(100) NOT NULL, 
username varchar(20) not null,
email varchar(50) ,
foreign key(username) references Users(UserName),
PRIMARY KEY (resetNumber));

ALTER TABLE
PETER.passwordreset
ADD
emailDate date NOT NULL DEFAULT '17/03/1997';

ALTER TABLE
PETER.passwordreset
ADD column
emailTime time NOT NULL DEFAULT '00:00:00';