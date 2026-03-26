create database if not exists csx370_mb_platform;
use csx370_mb_platform;

create table user (
    userId int auto_increment primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    firstName varchar(255) not null,
    lastName varchar(255) not null
);

create table posts (
    postId int auto_increment primary key,
    userId int not null,
    content text not null,
    createdDate timestamp default current_timestamp,
    foreign key (userId) references user(userId) on delete cascade
);

create table follows (
    followerId int not null,
    followingId int not null,
    primary key (followerId, followingId),
    foreign key (followerId) references user(userId) on delete cascade,
    foreign key (followingId) references user(userId) on delete cascade
);

create table likes (
    userId int not null,
    postId int not null,
    primary key (userId, postId),
    foreign key (userId) references user(userId) on delete cascade,
    foreign key (postId) references posts(postId) on delete cascade
);

create table bookmarks (
    userId int not null,
    postId int not null,
    primary key (userId, postId),
    foreign key (userId) references user(userId) on delete cascade,
    foreign key (postId) references posts(postId) on delete cascade
);

create table comments (
    commentId int auto_increment primary key,
    postId int not null,
    userId int not null,
    content text not null,
    createdDate timestamp default current_timestamp,
    foreign key (postId) references posts(postId) on delete cascade,
    foreign key (userId) references user(userId) on delete cascade
);