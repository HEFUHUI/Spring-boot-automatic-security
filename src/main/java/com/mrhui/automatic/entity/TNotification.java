package com.mrhui.automatic.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息表
 * @TableName t_notification
 */
@Data
public class TNotification implements Serializable {
    /**
     * 消息ID
     */
    private String proId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息主体
     */
    private String body;

    /**
     * 预览图片
     */
    private String preview;

    /**
     * 消息类型 0：提示消息 1：警告 2：严重警告
     */
    private Integer notifyType;

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