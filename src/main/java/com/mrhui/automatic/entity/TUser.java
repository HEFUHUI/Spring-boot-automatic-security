package com.mrhui.automatic.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.ToString;

/**
 * 用户表
 * @TableName t_user
 */
@Data
@ToString
public class TUser implements Serializable {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户账号
     */
    private String loginName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 用户头像
     */
    private TImage avatar;

    /**
     * 注册IP
     */
    private String loginIp;

    /**
     * 是否在线
     */
    private Boolean online;

    /**
     * 班级ID
     */
    private String classId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新
     */
    private Date updateAt;

    /**
     * 描述
     */
    private String comment = "";

    /**
     * 是否已被删除
     */
    private Boolean isDelete;
    /**
     * 回话ID
     */
    private String sessionId;

    /**
     * 用户的角色列表
     */
    private List<TRole> roles;

    /**
     * 班级
     */
    private TClass classes;

    private static final long serialVersionUID = 1L;
}