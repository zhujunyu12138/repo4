package com.deyuan.service.impl;

import com.deyuan.dao.UserDao;
import com.deyuan.pojo.Role;
import com.deyuan.pojo.UserInfo;
import com.deyuan.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userdao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo=userdao.findByUsername(username);
        User user = new User(userInfo.getUsername(),userInfo.getPassword(),userInfo.getStatus()==0?false:true,true,true,true,getAuthority(userInfo.getRoles()));
        return user;
    }

    private List<SimpleGrantedAuthority> getAuthority(List<Role> rolelist) {
        List<SimpleGrantedAuthority> list=new ArrayList<>();
        for (Role role : rolelist) {
            list.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        }

        return list;
    }

    //查询全部
    @Override
    public List<UserInfo> findAll() {
        return userdao.findAll();
    }

    //添加用户
    @Override
    public void save(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userdao.save(userInfo);
    }

    @Override
    public UserInfo findById(String id) {
        return userdao.findById(id);
    }

    //用户关联角色操作
    @Override
    public List<Role> findOtherRoles(String userId) {
        return userdao.findOtherRoles(userId);
    }

    //用户关联角色操作-添加角色
    @Override
    public void addRoleToUser(String userId, String[] roles) {
        for (String roleId : roles) {
            userdao.addRoleToUser(userId,roleId);
        }
    }
}
