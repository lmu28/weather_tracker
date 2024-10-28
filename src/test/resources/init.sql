drop table if exists sessions;
drop table if exists users_locations;
drop table if exists  users;
drop table if exists  locations;

create table users
(
    id       bigserial primary key,
    name     varchar(32),
    password varchar(255)
);
create table locations
(
    id bigserial primary key ,
    name varchar(128),
    latitude decimal,
    longitude decimal
);
create table sessions
(
    id varchar(256) primary key,
    user_id bigserial references users(id) on update cascade on delete cascade ,
    expires_at timestamp
);
create table users_locations
(
    user_id bigserial references users(id) on update cascade  on delete cascade ,
    location_id bigserial references locations(id) on update cascade on delete cascade ,
    primary key(user_id, location_id)
);





INSERT INTO users (name, password)
VALUES ('Alice', 'ef92b7796a6d7f7d4243cba81b196525d0c39e6b8f69a5c8ccda6e525e967a2d'),
       ('Bob100', 'c19c48b68a0d7b7087e17a79b8c528d2ef9263a1e7335a0bb70c7d94de4d447b'),
       ('Charlie', '8c6976e5b5410415bde908bd4dee15dfb16f0b1f5e0c24e6c3da81bdc59262a9'),
       ('Diana', '5f4dcc3b5aa765d61d8327deb882cf99'),
       ('Eve100', 'cd67c7f929fb848f7a474f38cd7f2d8d');


INSERT INTO locations (name, latitude, longitude)
VALUES ('New York', 40.712776, -74.005974),
       ('Los Angeles', 34.052235, -118.243683),
       ('Chicago', 41.878113, -87.629799),
       ('Houston', 29.760427, -95.369804),
       ('Phoenix', 33.448376, -112.074036),
       ('Philadelphia', 39.952583, -75.165222),
       ('San Antonio', 29.424122, -98.493629),
       ('San Diego', 32.715736, -117.161087);


INSERT INTO sessions (id, user_id, expires_at)
VALUES ('session_1', 1, CURRENT_TIMESTAMP + (24 * 60 * 60 * 1000)),  -- Сессия для Alice
       ('session_2', 2, CURRENT_TIMESTAMP + (24 * 60 * 60 * 1000)),  -- Сессия для Bob
       ('session_3', 3, CURRENT_TIMESTAMP + (24 * 60 * 60 * 1000)),  -- Сессия для Charlie
       ('session_4', 4, CURRENT_TIMESTAMP + (24 * 60 * 60 * 1000));



INSERT INTO users_locations (user_id, location_id)
VALUES (1, 1), -- Alice - New York
       (1, 2), -- Alice - Los Angeles
       (2, 2), -- Bob - Los Angeles
       (2, 3), -- Bob - Chicago
       (3, 4), -- Charlie - Houston
       (3, 5), -- Charlie - Phoenix
       (4, 6); -- Diana - Philadelphia
