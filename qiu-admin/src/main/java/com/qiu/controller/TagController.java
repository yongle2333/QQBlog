package com.qiu.controller;

import com.qiu.domain.ResponseResult;
import com.qiu.domain.dto.TagDTO;
import com.qiu.domain.dto.TagListDTO;
import com.qiu.domain.entity.Tag;
import com.qiu.domain.vo.PageVO;
import com.qiu.domain.vo.TagVO;
import com.qiu.service.TagService;
import com.qiu.utils.BeanCopyUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qiu
 * @version 1.0
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;
    @GetMapping("/list")
    public ResponseResult<PageVO> list(Integer pageNum, Integer pageSize, TagListDTO tagListDTO){

        return tagService.pageTagList(pageNum,pageSize,tagListDTO);
    }


    /**
     * 添加标签
     * @param tagListDTO
     * @return
     */
    @PostMapping
    public ResponseResult addTag(@RequestBody TagListDTO tagListDTO){
        Tag tag = BeanCopyUtils.copyBean(tagListDTO, Tag.class);
        tagService.addTag(tag);
        return ResponseResult.okResult();
    }


    /**
     * 删除标签
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id){

        tagService.deleteTag(id);
        return ResponseResult.okResult();

    }


    /**
     * 获取标签信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable("id") Long id){
        return tagService.getTag(id);

    }


    /**
     * 修改标签信息
     * @return
     */
    @PutMapping
    public ResponseResult updateTag(@RequestBody TagDTO tagDTO){

        return tagService.updateTag(tagDTO);
    }


    /**
     * 查询所有标签
     * @return
     */
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVO> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
}
