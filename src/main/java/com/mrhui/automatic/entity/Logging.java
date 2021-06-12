package com.mrhui.automatic.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName logging
 */
@Data
public class Logging implements Serializable {
    /**
     * 
     */
    private Integer logId;

    /**
     * 日志类型
     */
    private Integer logType;

    /**
     * 结果
     */
    private Boolean logOutcome;

    /**
     * 创建时间
     */
    private Date logCreateTime;

    /**
     * 描述
     */
    private String logComment;

    /**
     * 触发人
     */
    private String logUser;

    private TUser user;

    private static final long serialVersionUID = 1L;
}