-- Create the database.
create database if not exists csx370_mb_platform;

-- Use the created database.
use csx370_mb_platform;

-- Create the user table.
create table if not exists user (
    userId int auto_increment,
    username varchar(255) not null,
    password varchar(255) not null,
    firstName varchar(255) not null,
    lastName varchar(255) not null,
    primary key (userId),
    unique (username),
    constraint userName_min_length check (char_length(trim(userName)) >= 2),
    constraint firstName_min_length check (char_length(trim(firstName)) >= 2),
    constraint lastName_min_length check (char_length(trim(lastName)) >= 2)
);

-- Posts made by users
CREATE TABLE if not exists posts (
    postId int AUTO_INCREMENT PRIMARY KEY,
    userId int NOT NULL REFERENCES user(userId) ON DELETE CASCADE,
    content TEXT NOT NULL,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Follow relationships
CREATE TABLE if not exists follows (
    followerId int NOT NULL REFERENCES user(userId) ON DELETE CASCADE,
    followingId int NOT NULL REFERENCES user(userId) ON DELETE CASCADE,
    PRIMARY KEY (followerId, followingId),
    CHECK (followerId <> followingId)  -- can't follow yourself
);

-- Likes on posts
CREATE TABLE if not exists likes (
    userId int NOT NULL REFERENCES user(userId) ON DELETE CASCADE,
    postId int NOT NULL REFERENCES posts(postId) ON DELETE CASCADE,
    PRIMARY KEY (userId, postId)
);

-- Bookmarks
CREATE TABLE if not exists bookmarks (
    userId int NOT NULL REFERENCES user(userId) ON DELETE CASCADE,
    postId int NOT NULL REFERENCES posts(postId) ON DELETE CASCADE,
    PRIMARY KEY (userId, postId)
);

-- Comments on posts
CREATE TABLE if not exists comments (
    commentId int AUTO_INCREMENT PRIMARY KEY,
    postId int NOT NULL REFERENCES posts(postId) ON DELETE CASCADE,
    userId int NOT NULL REFERENCES user(userId) ON DELETE CASCADE,
    content TEXT NOT NULL,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);