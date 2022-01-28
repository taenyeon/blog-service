create table member(
                       id varchar2(30) primary key ,
                       pwd varchar2(70) not null ,
                       name varchar2(30) not null,
                       "LEVEL" number default 0,
                       adress varchar2(200) default '없음',
                       birth date not null ,
                       tel varchar2(20) default '없음',
                       email varchar2(40) not null,
                       img varchar2(200) default '/profileDefault/default.jpg'
);
alter table member modify img varchar2(200) default '/profileDefault/default.jpg';
update member set img = '/profileDefault/default.jpg';
create table item(
                     id number primary key ,
                     item_name varchar2(30),
                     price number(10),
                     quantity number(3)
);

create table board(
                      id number primary key ,
                      title varchar2(100),
                      content varchar2(1000),
                      writer varchar2(50),
                      "date" date,
                      hit number
);

create table file_info
(
    file_path varchar2(200) primary key ,
    file_name varchar2(100) not null ,
    file_size number(15) not null,
    board_id number,
    file_date date default sysdate
);
create table reply(
                      reply_id number primary key ,
                      reply_content varchar2(500),
                      reply_writer varchar2(100),
                      reply_date date,
                      reply_modified_date date default sysdate,
                      reply_parent_id number default 0,
                      reply_depth number default 0,
                      reply_order number default 0,
                      board_id number
);

create sequence reply_id_up
    increment by 1
    start with 1
    minvalue 1
    maxvalue 10000
    nocycle;
create sequence board_id_up
    increment by 1
    start with 1
    minvalue 1
    maxvalue 100
    nocycle;
select board_id_up.currval from DUAL;
select * from member;
commit;
select * from item;
select * from board;
select * from FILE_INFO;
select * from reply;
drop table reply;
drop table member;
drop table item;
CREATE SEQUENCE item_id START WITH 1 INCREMENT BY 1;
select * from member;
commit;

SELECT *
FROM
    (
        SELECT
            ROW_NUMBER() over (order by id desc ) AS RNUM, b.*
        FROM
            board b
        WHERE
                ROWNUM <= 10
    )
WHERE
        0 < RNUM;

select count(*) as listCnt
from BOARD;
select NVL(MAX(REPLY_ORDER), 0)
from REPLY
where BOARD_ID = 69;