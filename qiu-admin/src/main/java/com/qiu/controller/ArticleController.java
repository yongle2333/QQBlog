package com.qiu.controller;

import com.qiu.domain.ResponseResult;
import com.qiu.domain.dto.AddArticleDTO;
import com.qiu.domain.dto.ArticleDTO;
import com.qiu.domain.entity.Article;
import com.qiu.domain.vo.ArticleByIdVO;
import com.qiu.domain.vo.PageVO;
import com.qiu.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qiu
 * @version 1.0
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDTO addArticleDTO){

        return articleService.add(addArticleDTO);
    }


    /**
     * 查询文章列表
     * @param pageNum
     * @param pageSize
     * @param article
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listArticle(Integer pageNum, Integer pageSize, Article article){

        PageVO pageVO = articleService.listArticle(pageNum,pageSize,article);
        return ResponseResult.okResult(pageVO);

    }


    /**
     * 根据文章id查询文章详情
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getArticleDetails(@PathVariable("id") Long id){
        ArticleByIdVO articleByIdVO = articleService.getInfo(id);
        return ResponseResult.okResult(articleByIdVO);

    }


    /**
     * 编辑文章并更新
     * @param articleDTO
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody ArticleDTO articleDTO){

        articleService.edit(articleDTO);
        return ResponseResult.okResult();
    }


    /**
     * 删除文章(逻辑删除)
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id")Long id){
        articleService.deleteArticle(id);
        return ResponseResult.okResult();

    }



}
