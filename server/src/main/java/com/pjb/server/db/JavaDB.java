package com.pjb.server.db;

import com.pjb.server.DO.UserDO;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 在 Jvm 内存中模拟一个数据库
 * <p>
 * user 表的 id 字段和 username 字段都是 unique, 可以用来做查询
 * </p>
 * created by: tsb
 */
public enum JavaDB {
    INSTANCE;
    private final Set<UserDO> t_user = new HashSet<UserDO>(64) {
        {
            add(new UserDO("1", "cxk", "jntm", "ROLE_USER"));
            add(new UserDO("2", "root", "root", "ROLE_USER"));
            add(new UserDO("3", "admin", "admin", "ROLE_USER"));
        }
    };

    /*=================================================查======================================================*/
    public Optional<UserDO> findUserById(String id) {
        return t_user.stream().filter(userDO -> userDO.getId().equals(id)).findFirst();
    }

    public Optional<UserDO> findUserByUsername(String username) {
        return t_user.stream().filter(userDO -> userDO.getUsername().equals(username)).findFirst();
    }

    public Optional<UserDO> findUserByUsernameAndPassword(String username, String password) {
        return t_user.stream().filter(
                userDO ->
                        userDO.getUsername().equals(username) &&
                        userDO.getPassword().equals(password)
        ).findFirst();
    }

    /*=================================================增======================================================*/
    public String addUser(String username, String password, String authorities) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return "用户名或密码格式不正确";
        }

        if (findUserByUsername(username).isPresent()) {
            return "该用户名已被占用";
        }

        UserDO userDO = new UserDO(String.valueOf(t_user.size() + 1), username, password, authorities);
        t_user.add(userDO);

        return "添加用户成功";
    }

    public String addUser(UserDO userDO) {
        String username = userDO.getUsername();
        String password = userDO.getPassword();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return "用户名或密码格式不正确";
        }

        if (findUserByUsername(username).isPresent()) {
            return "该用户名已被占用";
        }

        t_user.add(userDO);

        return "添加用户成功";
    }

    /*=================================================删======================================================*/
    public void deleteUserById(String id) {
        Optional<UserDO> userDo = findUserById(id);
        if (!userDo.isPresent()) {
            return;
        }
        t_user.remove(userDo.get());
    }

    public void deleteUserByUsername(String username) {
        Optional<UserDO> userDo = findUserByUsername(username);
        if (!userDo.isPresent()) {
            return;
        }
        t_user.remove(userDo.get());
    }

    public void deleteUserByUser(UserDO UserDO) {
        t_user.remove(UserDO);
    }

    /*=================================================改======================================================*/
    public void updateUserById(UserDO userDO) {
        String id = userDO.getId();
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(userDO.getUsername()) ||
                StringUtils.isEmpty(userDO.getPassword())) {
            return;
        }

        Optional<UserDO> userById = findUserById(id);
        if (!userById.isPresent()) {
            return;
        }

        // 删除再插入
        deleteUserByUser(userById.get());
        t_user.add(userDO);
    }
}
