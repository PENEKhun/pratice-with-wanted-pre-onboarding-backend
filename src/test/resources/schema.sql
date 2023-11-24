create table user
(
    idx                      bigint auto_increment
        primary key,
    username                 varchar(50)                    not null,
    password                 varchar(100)                   not null,
    role                     varchar(15) default 'personal' not null,
    is_ban                   tinyint(1)  default 0          not null,
    personal_name            varchar(30)                    null comment '실명',
    personal_brith           date                           null comment '개인 - 생년월일',
    enterprise_name          varchar(20)                    null,
    enterprise_nation_code   varchar(5)                     null,
    enterprise_province_code varchar(10)                    null,
    created_at               datetime                       not null,
    updated_at               datetime                       not null,
    deleted_at               datetime                       null
);

create table job_posting
(
    idx              bigint auto_increment
        primary key,
    company_id       bigint        null,
    recruit_reward   int default 0 not null comment '채용 보상금',
    recruit_position varchar(30)   not null comment '채용 포지션',
    description      text          not null comment '채용 내용',
    required_skill   varchar(20)   not null,
    created_at       datetime      not null comment '삭',
    updated_at       datetime      not null,
    deleted_at       datetime      null,
    constraint job_posting_user_idx_fk
        foreign key (company_id) references user (idx)
)
    comment '채용 공고';

create index job_posting_idx_index
    on job_posting (idx);

create index user_idx_index
    on user (idx);

