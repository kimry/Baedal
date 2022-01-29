CREATE TABLE USER(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email varchar2(255) unique not null,
    password varchar2(255) not null,
    user_Type varchar2(20) not null,
    created_At varchar2(30) not null,
    updated_At varchar2(30) not null
);

create table REQUEST(
        id int not null auto_increment primary key,
        address varchar2(255) not null,
        driver_Id int,
        customer_Id int not null,
        status varchar2(30) not null,
        accepted_At varchar2(30),
        completed_At varchar2(30),
        created_At varchar2(30) not null,
        updated_At varchar2(30) not null
);
