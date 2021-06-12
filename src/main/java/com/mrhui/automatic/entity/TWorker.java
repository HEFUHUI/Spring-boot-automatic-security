package com.mrhui.automatic.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 上课表
 * @TableName t_worker
 */
@Data
public class TWorker implements Serializable {
    /**
     * 
     */
    private String classId;

    /**
     * 课程ID
     */
    private String courseId;

    /**
     * 上课时间，每天12节课，20则表示第二天的第8节课
     */
    private Integer time;

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