package com.mrhui.automatic.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * 班级表
 * @TableName t_class
 */
@Data
@ToString
public class TClass implements Serializable {
    /**
     * 班级ID
     */
    private String classId;

    /**
     * 班级名称
     */
    private String name;

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
    private String comment;

    /**
     * 是否已被删除
     */
    private Boolean isDelete;
    /**
     * 是否在上课
     */
    private Boolean isWorking;

    /**
     * 学生列表
     */
    private List<TUser> users;

    private static final long serialVersionUID = 1L;
}