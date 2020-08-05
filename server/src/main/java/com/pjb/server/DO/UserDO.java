package com.pjb.server.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDO {
    private String id;
    private String username;
    private String password;
    private String authorities;
}
