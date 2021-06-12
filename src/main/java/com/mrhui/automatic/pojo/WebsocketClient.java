package com.mrhui.automatic.pojo;

import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.Session;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebsocketClient {
    private Session session;
    private TUser user;
}
