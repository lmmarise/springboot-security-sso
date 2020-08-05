package com.pjb.server.service;

import com.pjb.server.DO.UserDO;
import com.pjb.server.db.JavaDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
 * @author tsb
 */
@Component
public class SSOUserDetailsService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDO> optionalUserDO = JavaDB.INSTANCE.findUserByUsername(username);
        if (!optionalUserDO.isPresent()) {
            throw new UsernameNotFoundException("用户不存在");
        }
        UserDO userDO = optionalUserDO.get();
        return new User(userDO.getUsername(), userDO.getPassword(), parseUserAuth(userDO.getAuthorities()));
    }

    // "ROLE_A, ROLE_B, ROLE_C" ==> 转为权限对象
    private List<GrantedAuthority> parseUserAuth(String authorities) {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
    }

    @PostConstruct
    public void initUserDB() {
        // 插入一个
        UserDO userDO = new UserDO(null, "1", passwordEncoder.encode("1"), "ROLE_USER");
        String result = JavaDB.INSTANCE.addUser(userDO);
        System.out.println("向JavaDB插入数据: " + userDO + " --> " + result);

        // 插入第二个
        userDO = new UserDO(null, "2", passwordEncoder.encode("2"), "ROLE_USER, ROLE_ADMIN");
        result = JavaDB.INSTANCE.addUser(userDO);
        System.out.println("向JavaDB插入数据: " + userDO + " --> " + result);
    }
}
