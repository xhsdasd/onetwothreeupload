package com.xh.onetwothreeupload.to;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HostVo {
    private String hostname;
    private int port;
    private String username;
    private String password;
}
