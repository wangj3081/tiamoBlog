package com.tiamo.search.controller;

import com.tiamo.entity.BlogEntity;
import com.tiamo.search.dto.response.BlogDto;
import com.tiamo.search.dto.request.BlogRequest;
import com.tiamo.search.service.SearchArticle;
import com.tiamo.util.CustomBeanAndSuperUtils;
import com.tiamo.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: wangjian
 * @Date: 2019-03-12 15:13:29
 */
@Api(tags = "搜索服务",description = "author")
@RestController(value = "/search")
public class SearchController {

    @Resource
    private SearchArticle searchArticle;

    @ApiOperation(value = "查询作者文章列表", notes = "查询列表")
    @ApiImplicitParam(value = "查询作者条件", name = "request",dataType = "BlogRequest")
    @PostMapping(value = "/searchBlogList")
    public Result<List<BlogDto>> searchBlogListByAuthor(@RequestBody BlogRequest request) {
        try {
            List<BlogEntity> result = searchArticle.queryByAuther(request);
            List<BlogDto> resultList = CustomBeanAndSuperUtils.convertPojos(result, BlogDto.class);
            return new Result<List<BlogDto>>().success(resultList);
        } catch (Exception e) {
            return new Result<List<BlogDto>>().error(Result.ERROR_CODE, e.getMessage());
        }
    }

    @ApiOperation(value = "根据文章内容获取文章列表", notes = "查询列表")
    @ApiImplicitParam(value = "根据文章内容获取文章列表", name = "context", dataType = "String")
    @GetMapping(value = "/searchArticleListByContext")
    public Result<List<BlogDto>> searchArticleListByContext(String context) {
        try{
            List<BlogEntity> list = searchArticle.queryByContext(context);
            List<BlogDto> resultList = CustomBeanAndSuperUtils.convertPojos(list, BlogDto.class);
            return new Result<List<BlogDto>>().success(resultList);
        } catch (Exception e) {
            return new Result<List<BlogDto>>().error(Result.ERROR_CODE, e.getMessage());
        }
    }

    @ApiOperation(value = "根据文章 ID 与 文章作者获取文章", notes = "查询指定文章")
    @ApiImplicitParam(value = "根据文章 ID 与 文章作者获取文章", name = "request", dataType = "BlogRequest")
    @PostMapping(value = "/searchAricleByAricleId")
    public Result<BlogDto> searchAricleByAricleId(@RequestBody BlogRequest request) {
        Result<BlogDto> result = new Result<>();
        if (StringUtils.isBlank(request.getArticleId())) {
            return result.error(Result.ERROR_CODE, "文章ID不能为空");
        }
        try {
            BlogEntity entity = searchArticle.queryByArticleId(request.getArticleId(), request.getAuthor());
            BlogDto resultEntity = CustomBeanAndSuperUtils.convertPojo(entity, BlogDto.class);
            return result.success(resultEntity);
        } catch (Exception e) {
            return result.error(Result.ERROR_CODE, e.getMessage());
        }
    }

}
