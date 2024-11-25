package com.qiu.controller;

import com.qiu.domain.ResponseResult;
import com.qiu.domain.entity.Role;
import com.qiu.domain.entity.User;
import com.qiu.domain.vo.UserInfoAndRoleIdsVO;
import com.qiu.service.RoleService;
import com.qiu.service.UserService;
import nonapi.io.github.classgraph.utils.LogNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qiu
 * @version 1.0
 */
@RestController
@RequestMapping("/system/user")
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 用户分页列表查询
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listPage(Integer pageNum, Integer pageSize, User user){

        return userService.listPage(pageNum,pageSize,user);
    }


    /**
     * 新增用户
     * @param user
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody User user){

        return userService.add(user);

    }


    /**
     * 删除用户
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id") Long id){

        userService.removeById(id);
        return ResponseResult.okResult();

    }


    /**
     * 根据用户id回显用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable("id")Long id){
        //获取用户信息
        User user = userService.getById(id);
        //获取角色信息
        List<Role> roles = roleService.selectRole();
        //获取角色id集合
        List<Long> roleIds = roleService.selectRoleIdByUserId(id);
        UserInfoAndRoleIdsVO vo  = new UserInfoAndRoleIdsVO(user,roles,roleIds);
        return ResponseResult.okResult(vo);

    }

    /**
     * 更新用户信息
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody User user){
        userService.updateUser(user);
        return ResponseResult.okResult();
    }






}
