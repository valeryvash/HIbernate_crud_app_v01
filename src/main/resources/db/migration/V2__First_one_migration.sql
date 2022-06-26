drop table if exists posts cascade;
drop table if exists posts_tags cascade;
drop table if exists tags cascade;
drop table if exists writers cascade;
drop table if exists writers_posts cascade;

drop sequence if exists post_seq;
drop sequence if exists tag_seq;
drop sequence if exists writer_seq;

create sequence post_seq start with 1 increment by 1;
create sequence tag_seq start with 1 increment by 1;
create sequence writer_seq start with 1 increment by 1;

create table posts (id bigint not null, content varchar(255) not null, post_status smallint not null, primary key (id));
create table posts_tags (Post_id bigint not null, tags_id bigint not null);
create table tags (id bigint not null, name varchar(255) not null, primary key (id));
create table writers (id bigint not null, name varchar(255) not null, primary key (id));
create table writers_posts (Writer_id bigint not null, posts_id bigint not null);

alter table if exists writers_posts add constraint first_one unique (posts_id);
alter table if exists posts_tags add constraint second_one foreign key (tags_id) references tags;
alter table if exists posts_tags add constraint third_one foreign key (Post_id) references posts;
alter table if exists writers_posts add constraint fourth_one foreign key (posts_id) references posts;
alter table if exists writers_posts add constraint fifth_one foreign key (Writer_id) references writers;