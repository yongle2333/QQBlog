package com.qiu.controller;

import com.qiu.domain.ResponseResult;
import com.qiu.domain.entity.Menu;
import com.qiu.domain.vo.MenuListVO;
import com.qiu.domain.vo.MenuTreeVO;
import com.qiu.domain.vo.MenuVO;
import com.qiu.domain.vo.RoleMenuTreeSelectVO;
import com.qiu.service.MenuService;
import com.qiu.utils.BeanCopyUtils;
import com.qiu.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qiu
 * @version 1.0
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;


    /**
     * 菜单列表
     * @param menu
     * @return
     */
    @GetMapping("/list")    //(selectMenuList方法)
    public ResponseResult listMenu(Menu menu){
        List<Menu> menus = menuService.selectMenuList(menu);
        List<MenuListVO> menuListVOS = BeanCopyUtils.copyBeanList(menus, MenuListVO.class);
        return ResponseResult.okResult(menuListVOS);
    }


    /**
     * 新增菜单
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }


    /**
     * 根据id查询菜单数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable("id")Long id){
        return ResponseResult.okResult(menuService.getById(id));
    }


    /**
     * 更新菜单
     * @return
     */
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
       if(menu.getId().equals(menu.getParentId())){
           return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
       }
       menuService.updateById(menu);
        return ResponseResult.okResult();
    }


    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable("menuId")Long menuId){
        if(menuService.hasChild(menuId)){
            return ResponseResult.errorResult(500,"存在子菜单不允许删除");

        }
        menuService.removeById(menuId);
        return ResponseResult.okResult();

    }


    /**
     * 先把菜单tree查询出来
     * @return
     */
    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        //复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，
        // 而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<MenuTreeVO> options = SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }


    /**
     * 加载对应角色菜单列表树
     * id(角色id)
     * @return
     */
    @GetMapping("/roleMenuTreeselect/{roleId}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("roleId") Long roleId){
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(roleId);
        List<MenuTreeVO> menuTreeVOS = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVO vo = new RoleMenuTreeSelectVO(menuTreeVOS,checkedKeys);
        return ResponseResult.okResult(vo);

    }


}
