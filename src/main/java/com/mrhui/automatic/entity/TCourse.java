package com.mrhui.automatic.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 课程
 * @TableName t_course
 */
@Data
public class TCourse implements Serializable {
    /**
     * 课程ID
     */
    private String courseId;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 封面图片
     */
    private String preview;

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

    private static final long serialVersionUID = 1L;
}