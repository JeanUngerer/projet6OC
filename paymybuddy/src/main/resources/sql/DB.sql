-- we don't know how to generate root <with-no-name> (class Root) :(
create table contact_seq
(
    next_val bigint null
)
    collate = utf8mb4_0900_ai_ci;

create table transaction_seq
(
    next_val bigint null
)
    collate = utf8mb4_0900_ai_ci;

create table users
(
    user_id      bigint       not null
        primary key,
    balance      double       not null,
    email        varchar(50)  not null,
    firstname    varchar(50)  null,
    lastname     varchar(50)  null,
    displayes_login varchar(50)  not null,
    password     varchar(255) not null,
    phone_number varchar(15)  null,
    provider     varchar(255) null,
    role         varchar(255) not null,
    username     varchar(50)  not null,
    constraint UK_6dotkott2kjsp8vw4d0m25fb7
        unique (email),
    constraint UK_r43af9ap4edm43mmtq01oddj6
        unique (username)
)
    collate = utf8mb4_0900_ai_ci;

create table contact
(
    contact_id bigint not null
        primary key,
    friend_id  bigint null,
    user_id    bigint null,
    constraint FK5cp46ojos2bcrke3ghqy22a1w
        foreign key (friend_id) references users (user_id),
    constraint FKbxl6anxo14q097g8cd2e51v55
        foreign key (user_id) references users (user_id)
)
    collate = utf8mb4_0900_ai_ci;

create table transaction
(
    transaction_id   bigint       not null
        primary key,
    amount           float        not null,
    transaction_date datetime(6)  null,
    description      varchar(255) null,
    fee              float        null,
    trader_id        bigint       null,
    user_id          bigint       null,
    constraint FKanjpo5tiapru7an6cw4cu37y4
        foreign key (user_id) references users (user_id),
    constraint FKb2o1bofu9k9jumeldw3b4e7ds
        foreign key (trader_id) references users (user_id)
)
    collate = utf8mb4_0900_ai_ci;

create table users_seq
(
    next_val bigint null
)
    collate = utf8mb4_0900_ai_ci;

