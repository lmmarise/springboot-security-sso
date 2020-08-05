package com.pjb.client1.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jinbin
 * @date 2019-05-20 20:35
 */
@RestController
public class ClientController {

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String index( ) {
        return "用户主页";
    }

    @GetMapping("/normal")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String normal( ) {
        return "用户页面";
    }

    @GetMapping("/medium")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String medium() {
        return "这也是用户页面";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin() {
        return "管理员页面";
    }
}
