create table post(
id bigint auto_increment primary key,
title varchar(100) not null,
content TEXT not null,
author_id bigint,
created_at DATETIME default current_timestamp,
view_count int default 0,
foreign key (author_id) references user(id) on delete set null
);
-- on delete set null 은 외래키가 user id를 가리키고 있다.
-- 사용자가 삭제되면 author_id 필드는 NULL 설정
-- 게시물이 남아있다는 장점이 있다.