package com.mrhui.automatic.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;

/**
 * 角色
 * @TableName t_role
 */
@Data
public class TRole implements Serializable {
    /**
     * 
     */
    private String roleId;

    /**
     * 
     */
    private String name;

    /**
     * 描述
     */
    private String comment;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新
     */
    private Date updateAt;

    /**
     * 权限列表
     */
    private List<TPermissions> permissions = new LinkedList<>();

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "TRole{" +
                "roleId='" + roleId + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", createTime=" + createTime +
                ", updateAt=" + updateAt +
                ", permissions=" + permissions +
                '}';
    }
}
