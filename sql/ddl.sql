create table member(
                       id varchar2(30) primary key ,
                       pwd varchar2(70) not null ,
                       name varchar2(30) not null
);

create table item(
                     id number primary key ,
                     itemname varchar2(30),
                     price number(10),
                     quantity number(3)
);

select * from member;