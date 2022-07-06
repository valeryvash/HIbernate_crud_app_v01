drop table if exists posts cascade;
drop table if exists posts_tags cascade;
drop table if exists tags cascade;
drop table if exists writers cascade;
drop table if exists writers_posts cascade;

create table posts (id bigserial not null, content varchar(255) not null, post_status varchar(255) not null, writer_id bigint not null, primary key (id));
create table posts_tags (relatedPosts_id bigint not null, tags_id bigint not null);
create table tags (id bigserial not null, name varchar(255) not null, primary key (id));
create table writers (id bigserial not null, name varchar(255) not null, primary key (id));

alter table if exists posts add constraint first_one foreign key (writer_id) references writers;
alter table if exists posts_tags add constraint second_one foreign key (tags_id) references tags;
alter table if exists posts_tags add constraint third_one foreign key (relatedPosts_id) references posts;