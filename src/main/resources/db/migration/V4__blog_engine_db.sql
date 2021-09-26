insert into users(code, email, is_moderator, `name`, password, photo, reg_time)
values ('123', 't1@mail.ru', 0, 'user1', '$2y$12$RrhSR3qnVbpk4yBuqAgXb.xtGerpj8Jo5RIqPM.OtbmT/R.YyrspK', null,
        now())
     , ('123', 't2@mail.ru', 0, 'user2', '$2y$12$PB9KG0cthVErpLN1jX46/O7z3VhrldGp/mDT6dksygrR4Of4eehoe',
        null, now())
     , ('123', 't3@mail.ru', 1, 'user3', '$2y$12$lNZlcHGLfhNB/G1JBAGPye3NFLge.x7bSyRfcEDYPSMEBLa7ipYwy',
        null, now());

insert into tags(name)
values ('tag0'),
       ('tag1');

insert into posts(is_active, moderation_status, moderator_id, user_id, `time`, title, text, view_count)
values (true, 'NEW', 1, 1, now(), 'title 1', 'post text 1', 222)
     , (true, 'NEW', 1, 1, '2021-09-01 12:00:00', 'title 2', 'some text', 323)
     , (true, 'NEW', 2, 2, '2021-09-01 12:00:00', 'title 3', 'some text', 231)
     , (true, 'ACCEPTED', 1, 2, '2020-08-01 00:00:00', 'title 4', 'some text', 123)
     , (true, 'NEW', 1, 3, now(), 'title 5', 'some text', 4123)
     , (true, 'NEW', 1, 3, now(), 'title 6', 'some text', 1233)
     , (true, 'DECLINED', 1, 3, now(), 'title 7', 'some text', 3331)
     , (true, 'ACCEPTED', 1, 1, now(), 'title 8', 'some text', 2311)
     , (true, 'DECLINED', 1, 1, now(), 'title 9', 'some text', 0)
     , (true, 'DECLINED', 1, 1, now(), 'title 10', 'some text', 1111)
     , (true, 'NEW', 1, 1, now(), 'title 11', 'some text', -1)
     , (true, 'ACCEPTED', 1, 1, now(), 'title 12', 'some text', 1110)
     , (true, 'ACCEPTED', 1, 1, now(), 'title 13', 'some text', 3213)
     , (false, 'DECLINED', 1, 1, now(), 'title 14', 'some text', 22)
;

insert into tag2post(post_id, tag_id)
values (1, 1)
     , (2, 1)
     , (3, 1)
     , (3, 2)
     , (4, 1)
     , (5, 2)
     , (6, 2);

insert into post_comments(text, time, parent_id, post_id, user_id)
values ('comment 1', now(), 1, 1, 1)
     , ('comment 2', now(), 1, 1, 2)
     , ('comment 3', now(), null, 2, 2);

insert into post_votes(`time`, `value`, user_id, post_id)
values (now(), 1, 1, 3)
     , (now(), -1, 2, 3)
     , (now(), 1, 3, 3);