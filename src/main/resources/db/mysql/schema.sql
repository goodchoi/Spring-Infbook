drop table if exists order_items cascade;

drop table if exists pay cascade;

drop table if exists orders cascade;

drop table if exists delivery cascade;

drop table if exists shopping_item cascade;

drop table if exists shopping_cart cascade;

drop table if exists member cascade;

drop table if exists subcategory_item cascade;

drop table if exists item cascade;

drop table if exists sub_category cascade;

drop table if exists category cascade;


create table category
(
    category_id bigint auto_increment primary key,
    eng_name    varchar(255) null,
    name        varchar(255) null
);

create table delivery
(
    delivery_id      bigint auto_increment primary key,
    created_at       datetime(6)  null,
    created_by       varchar(255) null,
    updated_at       datetime(6)  null,
    city             varchar(100) null,
    detailed_address varchar(100) null,
    street           varchar(100)  not null,
    zipcode          varchar(6)   not null,
    delivery_status  varchar(255) not null
);

create table item
(
    item_id          bigint auto_increment primary key,
    created_at       datetime(6)  null,
    created_by       varchar(255) null,
    updated_at       datetime(6)  null,
    author           varchar(20)  not null,
    file_name        varchar(100) not null,
    indexes          text         not null,
    isbn             varchar(13)  not null,
    name             varchar(60)  not null,
    page_number      int          not null,
    price            int          not null,
    publication_date datetime     not null,
    publisher        varchar(60)  not null,
    stock_quantity   int          not null,
    sub_title        varchar(255) null,
    category_id      bigint       null,
    constraint fk_item_category_id foreign key (category_id) references category (category_id)
);

create index idx_item_price
    on item (price);

create index idx_item_pub_date
    on item (publication_date);

create index idx_item_stock
    on item (stock_quantity);

create table member
(
    member_id        bigint auto_increment primary key,
    created_at       datetime(6)  null,
    created_by       varchar(255) null,
    updated_at       datetime(6)  null,
    account_id       varchar(60)  not null,
    city             varchar(100) null,
    detailed_address varchar(100) null,
    street           varchar(100)  not null,
    zipcode          varchar(6)   not null,
    birth_date       datetime     not null,
    email            varchar(50)  not null,
    name             varchar(60)  not null,
    password         varchar(100) not null,
    telephone        varchar(15)  not null,
    user_level       varchar(255) null
);

create table orders
(
    order_id     bigint auto_increment primary key,
    created_at   datetime(6)                        null,
    created_by   varchar(255)                       null,
    updated_at   datetime(6)                        null,
    order_date   datetime default CURRENT_TIMESTAMP not null,
    order_status varchar(255)                       not null,
    total_price  int                                not null,
    delivery_id  bigint                             null,
    member_id    bigint                             null,
    constraint fk_order_member_id foreign key (member_id) references member (member_id),
    constraint fk_order_delivery_id foreign key (delivery_id) references delivery (delivery_id)
);

create table order_items
(
    order_item_id bigint auto_increment primary key,
    created_at    datetime(6)  null,
    created_by    varchar(255) null,
    updated_at    datetime(6)  null,
    quantity      int          not null,
    item_id       bigint       null,
    order_id      bigint       null,
    constraint fk_orderItem_order_id foreign key (order_id) references orders (order_id),
    constraint fk_orderItem_item_id foreign key (item_id) references item (item_id)
);

create table pay
(
    id             varchar(255) not null primary key,
    paid_time      datetime(6)  not null,
    requested_time datetime(6)  not null,
    total_amount   int          not null,
    order_id       bigint       null,
    constraint fk_pay_order_id foreign key (order_id) references orders (order_id)
);

create table shopping_cart
(
    shopping_cart_id bigint auto_increment primary key,
    created_at       datetime(6)  null,
    created_by       varchar(255) null,
    updated_at       datetime(6)  null,
    member_id        bigint       null,
    constraint fk_shopping_cart_member_id foreign key (member_id) references member (member_id)
);

create table shopping_item
(
    shopping_item_id binary(16) not null
        primary key,
    quantity         int        not null,
    item_id          bigint     null,
    shopping_cart_id bigint     null,
    constraint fk_shoppingItem_item_id foreign key (item_id) references item (item_id),
    constraint fk_shoppingItem_cart_id foreign key (shopping_cart_id) references shopping_cart (shopping_cart_id)
);

create table sub_category
(
    subcategory_id bigint auto_increment primary key,
    eng_name       varchar(255) null,
    name           varchar(255) null,
    category_id    bigint       null,
    constraint fk_subcategory_category_id foreign key (category_id) references category (category_id)
);

create table subcategory_item
(
    item_id        bigint not null,
    subcategory_id bigint not null,
    primary key (item_id, subcategory_id),
    constraint fk_subcategroy_item_subcategory_id foreign key (subcategory_id) references sub_category (subcategory_id),
    constraint fk_subcategroy_item_item_id foreign key (item_id) references item (item_id)
);


