create table health_report
(
    id                bigint auto_increment comment '主键ID'
        primary key,
    user_id           bigint      not null comment '用户id',
    report_date       date        null comment '体检日期',
    blood_sugar       double      null comment '血糖（单位：mmol/L）',
    glucose           double      null comment '葡萄糖（单位：mmol/L）',
    blood_pressure    varchar(20) null comment '血压',
    cholesterol       double      null comment '总胆固醇（单位：mmol/L）',
    triglycerides     double      null comment '甘油三酯（单位：mmol/L）',
    white_blood_cells varchar(20) null comment '白细胞',
    hemoglobin        varchar(20) null comment '红细胞',
    create_time       datetime    not null comment '上传时间'
);


create table log
(
    id                bigint auto_increment comment '主键ID'
        primary key,
    user_id           bigint      not null comment '用户ID',
    user_type         tinyint     not null comment '用户类型:0超级管理员,1管理员,2用户,3vip用户',
    log_type          varchar(50) not null comment '日志类型(如登录日志,系统日志,操作日志等)',
    operation_content varchar(50) not null comment '操作内容',
    operation_time    datetime    not null comment '操作时间',
    details           varchar(50) null comment '日志详细信息'
)
    comment '日志表';


create table user
(
    id              bigint auto_increment comment '用户唯一ID'
        primary key,
    password        varchar(255)         not null comment '密码,不为空',
    phone           varchar(11)          not null comment '手机号,不为空',
    status          tinyint(1) default 1 null comment '账号状态,默认为1启用,0禁用',
    role            tinyint(1) default 2 not null comment '用户类型:0超级管理员,1管理员,2用户,3vip用户',
    last_login_time datetime             null comment '最后登录时间',
    created_time    datetime             null comment '创建时间',
    updated_time    datetime             null comment '修改时间',
    is_deleted      tinyint(1) default 0 null comment '0未删除1已删除'
)
    comment '用户表';


create table user_info
(
    id           bigint auto_increment comment 'ID'
        primary key,
    user_id      bigint               not null comment '用户id',
    username     varchar(50)          not null comment '用户名,不为空',
    avatar       varchar(255)         null comment '头像',
    address      varchar(100)         null comment '地址',
    province     varchar(50)          null comment '省',
    city         varchar(50)          null comment '城市',
    gender       tinyint(1)           null comment '1男性,0女性',
    age          int                  null comment '年龄',
    create_time  datetime             null comment '创建时间',
    updated_time datetime             null comment '修改时间',
    is_deleted   tinyint(1) default 0 null comment '0未删除,1已删除'
)
    comment '用户信息表';