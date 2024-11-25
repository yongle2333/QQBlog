package com.qiu.controller;

import com.qiu.domain.ResponseResult;
import com.qiu.domain.entity.LoginUser;
import com.qiu.domain.entity.Menu;
import com.qiu.domain.entity.User;
import com.qiu.domain.vo.AdminUserInfoVO;
import com.qiu.domain.vo.RoutersVO;
import com.qiu.domain.vo.UserInfoVO;
import com.qiu.enums.AppHttpCodeEnum;
import com.qiu.exception.SystemException;
import com.qiu.service.AdminLoginService;
import com.qiu.service.MenuService;
import com.qiu.service.RoleService;
import com.qiu.utils.BeanCopyUtils;
import com.qiu.utils.RedisCache;
import com.qiu.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qiu
 * @version 1.0
 */
@RestController
@Tag(name = "后台用户登录接口")
public class AdminLoginController {

    @Autowired
    private AdminLoginService adminLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    @Operation(summary = "用户登录")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示，必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }

        return adminLoginService.login(user);
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVO> getInfo(){

        //获取当前登录的用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //获取用户信息
        UserInfoVO userInfoVO = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVO.class);
        //封装数据返回
        AdminUserInfoVO adminUserInfoVO = new AdminUserInfoVO(perms, roleKeyList, userInfoVO);
        return ResponseResult.okResult(adminUserInfoVO);
    }


    @GetMapping("/getRouters")
    public ResponseResult<RoutersVO> getRouters(){
        //查询menu 的结果形式是tree(父子层级关系)
        Long userId = SecurityUtils.getUserId();
        //List<Menu> menus = menuService.selectRouterTreeByUserId(userId);
        //封装返回数据
//        List<MenuVO> menuVOS = BeanCopyUtils.copyBeanList(menus, MenuVO.class);
//        return ResponseResult.okResult(new RoutersVO(menuVOS));

        //直接一步封装
//          List<MenuVO> menuVOS = menuService.selectRouterTreeByUserId(userId);
//          return ResponseResult.okResult(new RoutersVO(menuVOS));
        List<Menu> menus = menuService.selectRouterTreeByUserId(userId);
        //封装返回数据
        //List<MenuVO> menuVOS = BeanCopyUtils.copyBeanList(menus, MenuVO.class);
        return ResponseResult.okResult(new RoutersVO(menus));
    }


    @PostMapping("/user/logout")
    public ResponseResult logout(){


        return adminLoginService.logout();



    }


}
