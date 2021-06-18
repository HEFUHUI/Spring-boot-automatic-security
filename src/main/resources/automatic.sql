create database if not exists automatic;
use automatic;

create table if not exists t_class
(
    class_id    varchar(36)                            not null comment '班级ID'
        primary key,
    name        varchar(20)                            not null comment '班级名称',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_at   datetime                               not null on update CURRENT_TIMESTAMP default CURRENT_TIMESTAMP comment '最后更新',
    comment     varchar(100) default ''                not null comment '描述',
    is_delete   bit          default 0 comment '是否已被删除'
) comment '班级表';

create table if not exists t_image
(
    image_id    varchar(36)   not null primary key comment '图片ID',
    url         varchar(1024) not null comment '图片地址',
    alias       varchar(300)  null                                 default '' comment '别名',
    create_time datetime                                           default CURRENT_TIMESTAMP not null comment '创建时间',
    update_at   datetime      not null on update CURRENT_TIMESTAMP default CURRENT_TIMESTAMP comment '最后更新',
    comment     varchar(100)                                       default '' not null comment '描述',
    is_delete   bit                                                default 0 comment '是否已被删除',
    full        bit                                                default 0 comment '是否完全URL'
) comment '图片表';

create table if not exists t_course
(
    course_id   varchar(36)                            not null comment '课程ID'
        primary key,
    name        varchar(10)                            not null comment '课程名称',
    preview     varchar(36)                            null comment '封面图片',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_at   datetime                               not null on update CURRENT_TIMESTAMP default CURRENT_TIMESTAMP comment '最后更新',
    comment     varchar(100) default ''                not null comment '描述',
    is_delete   bit          default 0 comment '是否已被删除',
    foreign key (preview) references t_image (image_id)
) comment '课程';

create table if not exists t_user
(
    user_id     varchar(36)                            not null comment '用户ID'
        primary key,
    login_name  varchar(20)                            not null unique,
    password    varchar(1024)                          null comment '用户密码',
    nick_name   varchar(20)                            not null comment '用户名',
    user_type   int                                    not null comment '用户类型',
    avatar      varchar(36) comment '用户头像',
    login_ip    varchar(15)  default '0.0.0.0' comment '注册IP',
    online      bit          default 0 comment '是否在线',
    class_id    varchar(36)                            null comment '班级ID',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_at   datetime                               not null on update CURRENT_TIMESTAMP default CURRENT_TIMESTAMP comment '最后更新',
    comment     varchar(100) default ''                not null comment '描述',
    is_delete   bit          default 0 comment '是否已被删除',
    foreign key (avatar) references t_image (image_id) on delete set null on update set null,
    foreign key (class_id) references t_class (class_id) on delete set null on update set null
) comment '用户表';


create table if not exists t_role
(
    role_id     varchar(36)                            not null primary key,
    name        varchar(10)                            not null unique,
    comment     varchar(100) default ''                not null comment '描述',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_at   datetime                               not null on update CURRENT_TIMESTAMP default CURRENT_TIMESTAMP comment '最后更新'
) comment '角色';

create table if not exists t_user_role
(
    user_id     varchar(36)                            not null comment '用户ID',
    role_id     varchar(36)                            not null comment '角色ID',
    comment     varchar(100) default ''                not null comment '描述',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_at   datetime                               not null on update CURRENT_TIMESTAMP default CURRENT_TIMESTAMP comment '最后更新',
    primary key (user_id, role_id),
    foreign key (user_id) references t_user (user_id) on DELETE CASCADE,
    foreign key (role_id) references t_role (role_id) on DELETE CASCADE
) comment '用户所属角色';

create table if not exists t_permissions
(
    permissions_id varchar(36) not null primary key,
    title          varchar(10) not null,
    code           varchar(20) not null,
    create_time    datetime                                         default CURRENT_TIMESTAMP not null comment '创建时间',
    update_at      datetime    not null on update CURRENT_TIMESTAMP default CURRENT_TIMESTAMP comment '最后更新',
    comment        varchar(100)                                     default '' comment '描述'
) comment '权限';

create table if not exists t_role_permissions
(
    permissions_id varchar(36)                            not null,
    role_id        varchar(36)                            not null,
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_at      datetime                               not null on update CURRENT_TIMESTAMP default CURRENT_TIMESTAMP comment '最后更新',
    comment        varchar(100) default ''                not null comment '描述',
    primary key (permissions_id, role_id),
    foreign key (permissions_id) references t_permissions (permissions_id) on DELETE CASCADE,
    foreign key (role_id) references t_role (role_id) on DELETE CASCADE
) comment '角色拥有权限';



create table if not exists t_worker
(
    course_id   varchar(36)                            not null comment '课程ID',
    class_id    varchar(36)                            not null comment '班级ID',
    time        int          default 0                 not null comment '上课时间，每天12节课，20则表示第二天的第8节课',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_at   datetime                               not null on update CURRENT_TIMESTAMP default CURRENT_TIMESTAMP comment '最后更新',
    comment     varchar(100) default ''                not null comment '描述',
    is_delete   bit          default 0 comment '是否已被删除',
    primary key (time, course_id, class_id),
    check ( time between 1 and 50),
    foreign key (course_id) references t_course (course_id),
    foreign key (class_id) references t_class (class_id)
) comment '上课表';

create table if not exists t_notification
(
    pro_id      varchar(36)                        not null comment '消息ID'
        primary key,
    title       varchar(20)                        not null comment '消息标题',
    body        varchar(120)                       not null comment '消息主体',
    preview     varchar(36)                        null comment '预览图片',
    notify_type int      default 0                 null comment '消息类型 0：提示消息 1：警告 2：严重警告',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_at   datetime                           not null on update CURRENT_TIMESTAMP default CURRENT_TIMESTAMP comment '最后更新',
    comment     varchar(100)                       null comment '描述',
    is_delete   bit      default 0 comment '是否已被删除',
    foreign key (preview) references t_image (image_id)
)
    comment '消息表';


drop table if exists logging;
create table if not exists logging
(
    log_id          int primary key auto_increment,
    log_type        int comment '日志类型',
    log_outcome     bit         default 1 comment '结果',
    log_create_time TIMESTAMP   default CURRENT_TIMESTAMP comment '创建时间',
    log_comment     varchar(36) default '' comment '描述',
    log_user        varchar(36) comment '触发人',
    foreign key (log_user) references t_user (user_id) on delete set null on update set null
    );