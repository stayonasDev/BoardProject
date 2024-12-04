create table comment(
id bigint auto_increment primary key,
post_id bigint,
author_id bigint,
content text not null,
parent_id bigint,
created_at datetime default current_timestamp,
foreign key (post_id) references post(id) on delete cascade,
foreign key (author_id) references user(id) on delete set null,
foreign key (parent_id) references comment(id) on delete set null
);