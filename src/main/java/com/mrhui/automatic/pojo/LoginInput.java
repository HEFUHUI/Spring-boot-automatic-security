package com.mrhui.automatic.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class LoginInput implements Serializable {
    private static final long serialVersionUID = 2L;

    private String userName;
    private String password;
}
