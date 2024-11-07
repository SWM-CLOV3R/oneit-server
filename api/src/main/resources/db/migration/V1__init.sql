create table category
(
    idx                  bigint auto_increment primary key,
    name                 varchar(255) null,
    display_name         varchar(255) null,
    level                int          null,
    parent_category_idx  bigint       null,
    parent_category_name varchar(255) null
);

create table collection
(
    idx           bigint auto_increment primary key,
    name          varchar(255) null,
    description   varchar(255) null,
    created_at    datetime     null,
    updated_at    datetime     null,
    deleted_at    datetime     null,
    thumbnail_url text         null,
    status        varchar(20)  null
);

create table collection_product
(
    idx            bigint auto_increment primary key,
    collection_idx bigint      null,
    product_idx    bigint      null,
    status         varchar(20) null,
    created_at     datetime    null,
    updated_at     datetime    null,
    deleted_at     datetime    null,
    detail_img     text        null
);

create table comment
(
    idx                 bigint auto_increment primary key,
    giftbox_product_idx bigint        not null,
    writer_idx          bigint        not null,
    content             varchar(1000) not null,
    created_at          datetime      null,
    updated_at          datetime      null,
    deleted_at          datetime      null,
    column_name         int           null,
    status              varchar(20)   null
);

create table device
(
    idx               bigint auto_increment primary key,
    user_idx          bigint       not null,
    device_token      varchar(255) null,
    last_logged_in_at datetime     null,
    device_type       varchar(20)  null
);

create table emoji
(
    idx     bigint auto_increment primary key,
    name    varchar(255) null,
    content varchar(255) null
);

create table friend_req
(
    idx               bigint auto_increment primary key,
    from_idx          bigint      null,
    to_idx            bigint      null,
    friend_req_status varchar(20) null,
    created_at        datetime    null,
    updated_at        datetime    null,
    deleted_at        datetime    null
);

create table friendship
(
    idx        bigint auto_increment primary key,
    user_idx   bigint      null,
    friend_idx bigint      null,
    created_at datetime    null,
    updated_at datetime    null,
    deleted_at datetime    null,
    status     varchar(20) null
);

create table giftbox
(
    idx              bigint auto_increment primary key,
    name             varchar(255) null,
    description      text         null,
    deadline         date         null,
    image_url        text         null,
    created_user_idx bigint       null,
    access_status    varchar(255) null,
    created_at       datetime     null,
    updated_at       datetime     null,
    deleted_at       datetime     null,
    status           varchar(20)  null,
    inquiry_status   varchar(20)  null,
    purchase_status  varchar(20)  null
);

create table giftbox_product
(
    idx             bigint auto_increment primary key,
    giftbox_idx     bigint                   null,
    product_idx     bigint                   null,
    status          varchar(255)             null,
    created_at      datetime default (now()) null,
    updated_at      datetime                 null,
    deleted_at      datetime                 null,
    purchase_status varchar(20)              null,
    like_count      int                      null,
    dislike_count   int      default 0       null,
    emoji_name      varchar(20)              null
);

create table giftbox_product_vote
(
    giftbox_idx  bigint       not null,
    product_idx  bigint       not null,
    user_idx     bigint       not null,
    browser_uuid varchar(20)  not null,
    vote         varchar(255) not null,
    primary key (product_idx, browser_uuid, giftbox_idx, user_idx)
)
    comment '유저의 선물바구니의 상품에 좋아요/싫어요 표시';

create table giftbox_user
(
    idx               bigint auto_increment primary key,
    giftbox_idx       bigint       null,
    user_idx          bigint       null,
    user_role         varchar(255) null,
    created_at        datetime     null,
    updated_at        datetime     null,
    deleted_at        datetime     null,
    invitation_status varchar(255) null,
    sender_idx        bigint       null
);

create table inquiry
(
    idx            bigint auto_increment primary key,
    giftbox_idx    bigint       null,
    target         varchar(255) null,
    created_at     datetime     null,
    updated_at     datetime     null,
    deleted_at     datetime     null,
    user_idx       bigint       null,
    inquiry_status varchar(20)  null
);

create table inquiry_product
(
    idx         bigint auto_increment primary key,
    emoji_name  varchar(20) null,
    inquiry_idx bigint      null,
    product_idx bigint      null,
    emoji_idx   bigint      null,
    giftbox_idx bigint      null,
    created_at  datetime    null,
    updated_at  datetime    null,
    deleted_at  datetime    null
);

create table keyword
(
    idx                 bigint auto_increment primary key,
    name                varchar(255) null,
    field               varchar(255) null,
    keyword_description varchar(255) null,
    created_at          datetime     null,
    updated_at          datetime     null
);

create table notification
(
    idx           bigint auto_increment primary key,
    receiver_idx  bigint       not null,
    sender_idx    bigint       null,
    title         varchar(100) null,
    body          varchar(255) null,
    created_at    datetime     null,
    read_at       datetime     null,
    sent_at       datetime     null,
    noti_status   varchar(10)  null,
    action_type   varchar(20)  null,
    platform_type varchar(20)  null,
    device_idx    bigint       null,
    updated_at    datetime     null,
    deleted_at    datetime     null
);

create table product
(
    idx                   bigint auto_increment primary key,
    name                  varchar(255) null,
    brand_name            varchar(255) null,
    original_price        int          null,
    current_price         int          null,
    discount_rate         int          null,
    mall_name             varchar(255) null,
    product_url           varchar(255) null,
    thumbnail_url         varchar(255) null,
    gender                varchar(255) null,
    category_idx          bigint       null,
    category_display_name varchar(255) null,
    created_at            datetime     null,
    updated_at            datetime     null,
    deleted_at            datetime     null,
    status                varchar(255) null,
    description           varchar(255) null,
    like_count            int          null,
    detail_urls           text         null
);

create table product_keyword
(
    product_idx bigint not null,
    keyword_idx bigint not null,
    intensity   int    null,
    primary key (product_idx, keyword_idx)
);

create table product_like
(
    idx         bigint auto_increment primary key,
    user_idx    bigint       not null,
    product_idx bigint       null,
    created_at  datetime     null,
    updated_at  datetime     null,
    deleted_at  datetime     null,
    like_status varchar(100) not null
);

create table user
(
    idx                bigint auto_increment primary key,
    email              varchar(255) null,
    name               varchar(255) null,
    nickname           varchar(255) null,
    phone_number       varchar(255) null,
    profile_img        text         null,
    gender             varchar(20)  null,
    birth_date         date         null,
    is_agree_marketing tinyint(1)   null,
    refresh_token      varchar(255) null,
    status             varchar(20)  null,
    created_at         datetime     null,
    updated_at         datetime     null,
    deleted_at         datetime     null
);