alter table post_comments
    add constraint post_comments_parent_id_fk foreign key (parent_id) references post_comments (id);
alter table post_comments
    add constraint post_comments_post_id_fk foreign key (post_id) references posts (id);
alter table post_comments
    add constraint post_comments_user_id_fk foreign key (user_id) references users (id);
alter table post_votes
    add constraint post_votes_user_id_fk foreign key (user_id) references users (id);
alter table post_votes
    add constraint post_votes_post_id_fk foreign key (post_id) references posts (id);
alter table posts
    add constraint posts_moderator_id_fk foreign key (moderator_id) references users (id);
alter table posts
    add constraint posts_user_id_fk foreign key (user_id) references users (id);
alter table tag2post
    add constraint tag2post_post_id_fk foreign key (post_id) references posts (id);
alter table tag2post
    add constraint tag2post_tag_id_fk foreign key (tag_id) references tags (id);