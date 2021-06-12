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

    private TUser user;

    private List<TPermissions> permissions;

    private List<TRole> roles;

    public UserVO(TUser user) {
        this.user = user;
    }
}
