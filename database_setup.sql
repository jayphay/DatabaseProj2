create database if not exists csx370_mb_platform;
use csx370_mb_platform;

create table if not exists user(
    userId int auto_increment primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    firstName varchar(255) not null,
    lastName varchar(255) not null
);

create table if not exists posts(
    postId int auto_increment primary key,
    userId int not null,
    content text not null,
    createdDate timestamp default current_timestamp,
    foreign key (userId) references user(userId) on delete cascade
);

create table if not exists follows (
    followerId int not null,
    followingId int not null,
    primary key (followerId, followingId),
    foreign key (followerId) references user(userId) on delete cascade,
    foreign key (followingId) references user(userId) on delete cascade
);

create table if not exists likes (
    userId int not null,
    postId int not null,
    primary key (userId, postId),
    foreign key (userId) references user(userId) on delete cascade,
    foreign key (postId) references posts(postId) on delete cascade
);

create table if not exists bookmarks (
    userId int not null,
    postId int not null,
    primary key (userId, postId),
    foreign key (userId) references user(userId) on delete cascade,
    foreign key (postId) references posts(postId) on delete cascade
);

create table if not exists comments (
    commentId int auto_increment primary key,
    postId int not null,
    userId int not null,
    content text not null,
    createdDate timestamp default current_timestamp,
    foreign key (postId) references posts(postId) on delete cascade,
    foreign key (userId) references user(userId) on delete cascade
);

-- Users
INSERT IGNORE INTO user (username, password, firstName, lastName) VALUES
('alice', '$2a$10$abcdefghijklmnopqrstuv', 'Alice', 'Anderson'),
('bob', '$2a$10$abcdefghijklmnopqrstuv', 'Bob', 'Brown'),
('carol', '$2a$10$abcdefghijklmnopqrstuv', 'Carol', 'Clark'),
('dave', '$2a$10$abcdefghijklmnopqrstuv', 'Dave', 'Davis'),
('eve', '$2a$10$abcdefghijklmnopqrstuv', 'Eve', 'Evans');

-- Posts
INSERT IGNORE INTO posts (userId, content) VALUES
(1, 'Hello world! This is Alice. #intro'),
(2, 'Bob here, enjoying the platform.'),
(3, 'Carol''s first post!'),
(4, 'Dave checking in. #intro'),
(5, 'Eve loves databases! #databases'),
(1, 'Alice again, with another post.'),
(2, 'Bob shares a fun fact.'),
(3, 'Carol posts a question. #question'),
(4, 'Dave''s second post.'),
(5, 'Eve''s thoughts on SQL. #databases');

-- Follows
INSERT IGNORE INTO follows (followerId, followingId) VALUES
(1,2),(1,3),(2,1),(2,3),(3,1),(3,4),(4,5),(5,1),(5,2);

-- Likes
INSERT IGNORE INTO likes (userId, postId) VALUES
(1,2),(1,3),(2,1),(2,3),(3,1),(3,2),(4,5),(5,1),(5,2),(5,3);

-- Bookmarks
INSERT IGNORE INTO bookmarks (userId, postId) VALUES
(1,2),(2,1),(3,4),(4,5),(5,1);

-- Comments
INSERT IGNORE INTO comments (postId, userId, content) VALUES
(1,2,'Nice post, Alice!'),
(1,3,'Welcome Alice!'),
(2,1,'Thanks Bob!'),
(3,4,'Interesting question, Carol.'),
(4,5,'Good to see you, Dave!'),
(5,1,'Great thoughts, Eve.'),
(6,2,'Another good one, Alice.'),
(7,3,'Fun fact, Bob!'),
(8,4,'I have an answer, Carol.'),
(9,5,'Keep posting, Dave!');