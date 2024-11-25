package com.qiu.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.qiu.domain.ResponseResult;
import com.qiu.domain.entity.Category;
import com.qiu.domain.vo.CategoryVO;
import com.qiu.domain.vo.ExcelCategoryVO;
import com.qiu.enums.AppHttpCodeEnum;
import com.qiu.service.CategoryService;
import com.qiu.utils.BeanCopyUtils;
import com.qiu.utils.WebUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author qiu
 * @version 1.0
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有分类列表
     * @return
     */
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVO> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);

    }


    @PreAuthorize("@ps.hasPermission('ontent:category:export')")    //权限校验
    @GetMapping("/export")
    public void export(HttpServletResponse response){

        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryS = categoryService.list();
            List<ExcelCategoryVO> excelCategoryVOList = BeanCopyUtils.copyBeanList(categoryS, ExcelCategoryVO.class);

            //把数据写入Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVO.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVOList);
        } catch (Exception e) {
            //出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }

    }
}
