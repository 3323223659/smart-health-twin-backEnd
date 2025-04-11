CREATE DATABASE smart_health_twin;

use smart_health_twin;

create table health_advice
(
    id            bigint auto_increment comment '主键ID'
        primary key,
    user_id       bigint   not null comment '关联用户ID',
    body_advice   text     null comment '身体锻炼建议',
    sleep_advice  text     null comment '作息睡眠建议',
    diet_advice   text     null comment '饮食营养建议',
    mental_advice text     null comment '心理健康建议',
    create_time   datetime null comment '创建时间'
)
    comment '健康建议表';

create table health_report
(
    id                bigint auto_increment comment '主键ID'
        primary key,
    user_id           bigint        not null comment '用户id',
    report_date       date          null comment '体检日期',
    blood_sugar       double        null comment '血糖（单位：mmol/L）',
    glucose           double        null comment '葡萄糖（单位：mmol/L）',
    blood_pressure    varchar(20)   null comment '血压',
    cholesterol       double        null comment '总胆固醇（单位：mmol/L）',
    triglycerides     double        null comment '甘油三酯（单位：mmol/L）',
    white_blood_cells varchar(20)   null comment '白细胞',
    hemoglobin        varchar(20)   null comment '红细胞',
    create_time       datetime      not null comment '上传时间',
    photo_path        varchar(255)  null comment '体检报告照片路径',
    full_report       varchar(2048) null comment '体检报告全部信息(JSON或文本格式)'
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
    id            bigint auto_increment comment 'ID'
        primary key,
    user_id       bigint               not null comment '用户id',
    username      varchar(50)          not null comment '用户名,不为空',
    avatar        varchar(255)         null comment '头像',
    address       varchar(100)         null comment '地址',
    province      varchar(50)          null comment '省',
    city          varchar(50)          null comment '城市',
    gender        tinyint(1)           null comment '1男性,0女性',
    age           int                  null comment '年龄',
    create_time   datetime             null comment '创建时间',
    updated_time  datetime             null comment '修改时间',
    is_deleted    tinyint(1) default 0 null comment '0未删除,1已删除',
    health_status tinyint    default 1 null comment '健康状态'
)
    comment '用户信息表';

insert into smart_health_twin.health_advice (id, user_id, body_advice, sleep_advice, diet_advice, mental_advice, create_time)
values  (1, 2, '**关键信息：**
- 性别：男，年龄未明确。
- 身高168cm，体重70kg，BMI约为24.6（超重范围）。
- 血压正常（110/70mmHg）。
- 血常规、尿常规、肝功能、血糖、血脂、心电图均正常。

**异常项：**
- 血红蛋白略高（160g/L，正常范围约130-175g/L），需注意是否存在脱水或高原反应等情况。
- BMI 24.6，接近超重临界值，需控制体重。

**建议：**
保持健康生活方式，均衡饮食，适量运动以控制体重。定期复查血红蛋白水平，排除潜在问题。', null, null, null, '2025-04-09 23:37:52');

insert into smart_health_twin.health_report (id, user_id, report_date, blood_sugar, glucose, blood_pressure, cholesterol, triglycerides, white_blood_cells, hemoglobin, create_time, photo_path, full_report)
values  (1, 5, '2025-04-06', 5.44, 5.2, '110/70 mmHg', 3.5, 1.1, '9.2/10-9/L', '5.3/10-12/L', '2025-04-06 11:21:33', null, null),
        (2, 2, '2025-04-08', 5.44, 5.2, '110/70 mmHg', 3.5, 1.1, '9.2/10-9/L', '5.3/10-12/L', '2025-04-08 09:12:51', 'https://tlias399.oss-cn-hangzhou.aliyuncs.com/view.jpg', '姓 名 年龄 性别 男 籍贯 确诊慢病 无 体检日期 1、身高168cm 体重70KG 2、血压： 110/70 mmHg， 3、血常规：正常 白细胞(9.2/10-9/L) 红细胞(5.3/10-12/L) 血红蛋白(160g/10-12/L) 血小板总数(300/10-9/L) 3、尿常规： 正常 葡萄糖(5.2mmol/L) 蛋白质(阴性) 体 隐血(阴性) 胆红素(阴性) 酮体(阴性) 白细胞(阴性) 4、肝功： 正常 谷丙转氨酶( 13U/L) 总胆红素(12umol/L) 白蛋白 (40mmol/L) 直胆红素(1.4umol/L) 间接胆红素(9umol/L) 5、血糖： 血糖 (5.44mmol/L) 6、血脂： 总胆固醇 (3.5mmol/L) 甘油三脂(1.1mmol/L) 检 7、心电图：正常 窦性心率 正常心电图 体检印象： 1、血压正常 2、血糖正常 3、所检查的项目未见明显异常 结 果 '),
        (3, 1, '2025-04-08', 5.44, 5.2, '110/70 mmHg', 3.5, 1.1, '9.2/10-9/L', '5.3/10-12/L', '2025-04-08 09:12:51', 'https://tlias399.oss-cn-hangzhou.aliyuncs.com/view.jpg', '姓 名 年龄 性别 男 籍贯 确诊慢病 无 体检日期 1、身高168cm 体重70KG 2、血压： 110/70 mmHg， 3、血常规：正常 白细胞(9.2/10-9/L) 红细胞(5.3/10-12/L) 血红蛋白(160g/10-12/L) 血小板总数(300/10-9/L) 3、尿常规： 正常 葡萄糖(5.2mmol/L) 蛋白质(阴性) 体 隐血(阴性) 胆红素(阴性) 酮体(阴性) 白细胞(阴性) 4、肝功： 正常 谷丙转氨酶( 13U/L) 总胆红素(12umol/L) 白蛋白 (40mmol/L) 直胆红素(1.4umol/L) 间接胆红素(9umol/L) 5、血糖： 血糖 (5.44mmol/L) 6、血脂： 总胆固醇 (3.5mmol/L) 甘油三脂(1.1mmol/L) 检 7、心电图：正常 窦性心率 正常心电图 体检印象： 1、血压正常 2、血糖正常 3、所检查的项目未见明显异常 结 果 ');


insert into smart_health_twin.user (id, password, phone, status, role, last_login_time, created_time, updated_time, is_deleted)
values  (1, '62924ec26ce203086bf2f2da612cc37e', '13553636103', 1, 2, '2025-04-10 19:05:00', '2025-03-25 11:34:55', '2025-03-25 11:34:55', 0),
        (2, '4c92cbd086373b42df998ba993712dfe', '18565950974', 1, 2, '2025-04-10 19:04:00', '2025-03-21 11:34:55', '2025-03-21 11:34:55', 0),
        (3, '4c92cbd086373b42df998ba993712dfe', '13727788481', 1, 2, null, '2025-03-21 23:59:55', '2025-03-21 23:59:55', 0),
        (4, '4c92cbd086373b42df998ba993712dfe', '12345678906', 1, 2, '2025-03-22 00:14:22', '2025-03-22 00:14:01', '2025-03-22 00:14:01', 0),
        (5, '4c92cbd086373b42df998ba993712dfe', '12345678908', 1, 2, '2025-04-06 11:18:04', '2025-03-22 13:12:42', '2025-03-22 13:12:42', 0);


insert into smart_health_twin.user_info (id, user_id, username, avatar, address, province, city, gender, age, create_time, updated_time, is_deleted, health_status)
values  (1, 1, '张三', 'http://ymusic-image.oss-cn-guangzhou.aliyuncs.com/e5886128-3361-40de-b8fe-bf481978a62a.jpg', 'gdupt', '广东省', '茂名市', 1, 18, '2025-03-22 18:14:54', '2025-03-22 18:14:57', 0, 1),
        (2, 2, 'yangxiao', '2.png', 'gdupt', '广东省', '茂名市', 1, 18, '2025-03-21 18:14:54', '2025-03-21 18:14:57', 0, 1),
        (3, 3, 'yx', '3.png', 'gdupt', '广东省', '广州市', 0, 19, '2025-03-22 00:01:04', '2025-03-22 00:01:05', 0, 1),
        (1903119729762402306, 4, 'yangx', '222.png', 'gdupt', '甘肃', '兰州', 1, 80, '2025-03-22 00:21:16', '2025-03-22 00:21:16', 0, 1),
        (1903119729762402307, 5, 'wwwwwww', '222.png', 'gdupt', '甘肃', '兰州', 1, 40, '2025-03-22 13:13:50', '2025-03-22 13:13:50', 0, 1);
