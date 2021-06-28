package com.mrhui.automatic.entity.vo;

import com.mrhui.automatic.entity.TPermissions;
import com.mrhui.automatic.entity.TRole;
import com.mrhui.automatic.entity.TUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户信息
     */
    private TUser user;

    /**
     * 权限列表
     */
    private List<TPermissions> permissions;

    /**
     * 用户信息
     */
    private List<TRole> roles;
    /**
     * 用户凭证
     */
    private String token;

    public UserVO(TUser user) {
        this.user = user;
    }
}
