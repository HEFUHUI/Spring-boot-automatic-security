package com.mrhui.automatic.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片表
 * @TableName t_image
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TImage implements Serializable {
    /**
     * 图片ID
     */
    private String imageId;

    /**
     * 图片地址
     */
    private String url;

    /**
     * 别名
     */
    private String alias;

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

    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        return "TImage{" +
                "imageId='" + imageId + '\'' +
                ", url='" + url + '\'' +
                ", alias='" + alias + '\'' +
                ", createTime=" + createTime +
                ", updateAt=" + updateAt +
                ", comment='" + comment + '\'' +
                ", isDelete=" + isDelete +
                '}';
    }
}