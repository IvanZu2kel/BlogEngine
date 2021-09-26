drop table if exists captcha_codes;
drop table if exists global_settings;
drop table if exists posts;
drop table if exists post_comments;
drop table if exists post_votes;
drop table if exists tags;
drop table if exists tag2post;
drop table if exists users;
create table captcha_codes
(
    id          INT COMMENT 'id каптча' not null auto_increment,
    code        TINYTEXT COMMENT 'код, отображаемый на картинкке капчи' not null,
    secret_code TINYTEXT COMMENT 'код, передаваемый в параметре' not null,
    time        datetime COMMENT 'дата и время генерации кода капчи' not null,
    primary key (id)
);
create table global_settings
(
    id    INT COMMENT 'id настройки' not null auto_increment,
    code  VARCHAR(255) COMMENT 'системное имя настройки' not null,
    name  VARCHAR(255) COMMENT 'название настройки' not null,
    value VARCHAR(255) COMMENT 'значение настройки' not null,
    primary key (id)
);
create table post_comments
(
    id        INT COMMENT 'id комментария' not null auto_increment,
    text      text COMMENT 'текст комментария' not null,
    time      datetime COMMENT 'дата и время комментария' not null,
    parent_id INT COMMENT 'комментарий, на который оставлен этот комментарий (может быть NULL, если комментарий оставлен просто к посту)',
    post_id   INT COMMENT 'пост, к которому написан комментарий' not null,
    user_id   INT COMMENT 'автор комментария' not null,
    primary key (id)
);
create table post_votes
(
    id      INT COMMENT 'id лайка/дизлайка' not null auto_increment,
    time    datetime COMMENT 'дата и время лайка / дизлайка' not null,
    value   TINYINT COMMENT 'лайк или дизлайк: 1 или -1' not null,
    user_id INT NOT NULL COMMENT 'тот, кто поставил лайк / дизлайк',
    post_id INT NOT NULL COMMENT 'пост, которому поставлен лайк / дизлайк',
    primary key (id)
);
create table posts
(
    id                INT COMMENT 'id поста' not null auto_increment,
    is_active         TINYINT COMMENT 'скрыта или активна публикация: 0 или 1' not null,
    moderation_status enum('NEW','ACCEPTED', 'DECLINED') COMMENT 'статус модерации, по умолчанию значение "NEW"' not null,
    text              text COMMENT 'текст поста' not null,
    time              datetime COMMENT 'дата и время публикации поста' not null,
    title             VARCHAR(255) COMMENT 'заголовок поста' not null,
    view_count        INT COMMENT 'количество просмотров поста' not null,
    moderator_id      INT COMMENT 'ID пользователя-модератора, принявшего решение, или NULL',
    user_id           INT NOT NULL COMMENT 'автор поста',
    primary key (id)
);
create table tag2post
(
    id      INT COMMENT 'id связи' not null auto_increment,
    post_id INT COMMENT 'id поста' not null,
    tag_id  INT COMMENT 'id тэга' not null,
    primary key (id)
);
create table tags
(
    id   INT COMMENT 'id тэга' not null auto_increment,
    name VARCHAR(255) COMMENT 'текст тэга' not null,
    primary key (id)
);
create table users
(
    id           INT COMMENT 'id пользователя' not null auto_increment,
    code         VARCHAR(255) COMMENT 'код для восстановления пароля, может быть NULL',
    email        VARCHAR(255) COMMENT 'e-mail пользователя' not null,
    is_moderator TINYINT COMMENT 'является ли пользователь модератором (может ли править глобальные настройки сайта и модерировать посты)' not null,
    name         VARCHAR(255) COMMENT 'имя пользователя' not null,
    password     VARCHAR(255) COMMENT 'хэш пароля пользователя' not null,
    photo        text COMMENT 'фотография (ссылка на файл), может быть NULL',
    reg_time     datetime COMMENT 'дата и время регистрации пользователя' not null,
    primary key (id)
);
