create table member
(
    member_id             varchar2(30) primary key,
    member_pwd            varchar2(70) not null,
    member_name           varchar2(30) not null,
    member_birth          date          default sysdate,
    member_tel            varchar2(20) not null,
    member_email          varchar2(40) not null,
    member_img            varchar2(200) default 'default.jpg',
    member_status_message varchar2(300) default '없음',
    member_is_del         varchar2(1)   default 'N',
    member_created        date          default sysdate
);
create table blog(
                     blog_adress varchar2(255) primary key,
                     blog_writer varchar2(255) not null,
                     blog_name varchar2(255) not null,
                     blog_explanation varchar2(255) not null,
                     blog_is_team varchar2(1) default 'N',
                     blog_is_del varchar2(1) default 'N',
                     blog_is_private varchar2(1) default 'N',
                     blog_background_img varchar2(255) null,
                     blog_main_img varchar2(255) null
);
alter table blog add constraint fk_blog_writer foreign key (blog_writer) references member(member_id);
create table board
(
    board_id          number primary key,
    category_id       number        not null,
    board_title       varchar2(255) not null,
    board_content     clob          not null,
    board_writer      varchar2(50)  not null,
    board_write_date  date          default sysdate,
    board_modify_date date          null,
    board_hit         number        default 0,
    board_is_del      varchar2(1)   default 'N',
    board_main_img    varchar2(255) default 'default.jsp',
    board_is_private  varchar2(1)   default 'N',
    board_teg         varchar2(100) null
);
alter table board add constraint fk_writer foreign key(board_writer) references member(member_id);
alter table board add constraint fk_category foreign key (category_id) references category(category_id);

create table category(
                         category_id number primary key,
                         category_name varchar2(255) not null,
                         category_is_del varchar2(1) default 'N',
                         blog_adress varchar2(255) not null
);
alter table category add constraint fk_category_blog_id foreign key (blog_adress) references blog(blog_adress);
create table like_info(
                          like_id number primary key,
                          board_id number not null,
                          member_id varchar2(255) not null
);
alter table like_info add constraint fk_board_id foreign key (board_id) references board(board_id);
alter table like_info add constraint fk_member_id foreign key (member_id) references member(member_id);
create table push(
                     push_id number primary key,
                     member_id number not null,
                     push_content varchar2(500) not null,
                     push_is_seen varchar2(1) default 'N',
                     push_date date default sysdate
);
alter table push add constraint fk_push_member_id foreign key (member_id) references member(member_id);

create table file_info
(
    file_path varchar2(200) primary key,
    file_name varchar2(100) not null,
    file_size number(15)    not null,
    board_id  number,
    file_date date default sysdate
);
alter table file_info add constraint fk_file_board_id foreign key (board_id) references board(board_id);

create table reply
(
    reply_id          number primary key,
    board_id          number        not null,
    reply_content varchar2 (1000) not null,
    reply_writer      varchar2(100) not null,
    reply_write_date  date        default sysdate,
    reply_modify_date date          null,
    reply_parent_id   number      default 0,
    reply_depth       number      default 0,
    reply_order       number      default 0,
    reply_like        number      default 0,
    reply_is_del      varchar2(1) default 'N'
);
alter table reply add constraint  fk_reply_board_id foreign key  (board_id) references board(board_id);
alter table reply add constraint  fk_reply_writer foreign key  (reply_writer) references member(member_id);