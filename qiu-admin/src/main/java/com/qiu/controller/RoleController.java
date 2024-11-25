package com.qiu.controller;

import com.qiu.domain.ResponseResult;
import com.qiu.domain.dto.ChangeRoleStatusDTO;
import com.qiu.domain.entity.Role;
import com.qiu.service.LinkService;
import com.qiu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qiu
 * @version 1.0
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;




    /**
     * 角色列表查询
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Role role, Integer pageNum, Integer pageSize){
        return roleService.selectRolePage(role,pageNum,pageSize);

    }


    /**
     * 修改角色状态
     * @param changeRoleStatusDTO
     * @return
     */
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDTO changeRoleStatusDTO){

        Role role = new Role();
        role.setId(changeRoleStatusDTO.getRoleId());
        role.setStatus(changeRoleStatusDTO.getStatus());
        return ResponseResult.okResult(roleService.updateById(role));

    }


    /**
     * 再新增角色
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody Role role){
        roleService.insertRole(role);
        return ResponseResult.okResult();

    }


    /**
     * 通过角色id回显角色信息
     * @param roleId
     * @return
     */
    @GetMapping("/{roleId}")
    public ResponseResult getRoleById(@PathVariable("roleId") Long roleId){
        Role role = roleService.getById(roleId);
        return ResponseResult.okResult(role);
    }


    /**
     * 更新角色信息
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Role role){

        roleService.updateRole(role);
        return ResponseResult.okResult();


    }


    @DeleteMapping("/{id}")
    public ResponseResult deleteRoleById(@PathVariable("id") Long id){
        roleService.removeById(id);
        return ResponseResult.okResult();

    }


    /**
     * 查询表角色列表
     * @return
     */
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        List<Role> roles = roleService.selectRole();
        return ResponseResult.okResult(roles);

    }






}
