package com.tiamo.search.controller;

import com.tiamo.entity.BlogEntity;
import com.tiamo.search.dto.BlogDto;
import com.tiamo.search.dto.BlogRequest;
import com.tiamo.search.service.SearchArticle;
import com.tiamo.util.CustomBeanAndSuperUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "查询作者库存列表", notes = "查询列表")
    @ApiImplicitParam(value = "查询条件", name = "request",dataType = "BlogDto")
    @PostMapping(value = "/searchBlogList")
    public List<BlogDto> searchBlogListByAuthor(@RequestBody BlogRequest request) {
        List<BlogEntity> result = searchArticle.queryByAuther(request.getAuthor());
        List<BlogDto> resultList = CustomBeanAndSuperUtils.convertPojos(result, BlogDto.class);
        return resultList;
    }


}
