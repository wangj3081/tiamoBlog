package com.tiamo.search.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiamo.entity.ReadHubNewsEntity;
import com.tiamo.entity.ReadHubTopicEntity;
import com.tiamo.search.dto.request.SearchReadhubReq;
import com.tiamo.search.dto.response.ReadHubResponse;
import com.tiamo.search.dto.vo.ReadHubTopicVo;
import com.tiamo.search.dto.vo.ReadHubVo;
import com.tiamo.search.service.SearchReadHubService;
import com.tiamo.util.CustomBeanAndSuperUtils;
import com.tiamo.util.ObjectUtils;
import com.tiamo.util.Result;
import com.tiamo.webdata.topic.ReadHubTopic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 请求
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.search.controller.SearchReadHubController
 * @since JDK1.8
 */
@Slf4j
@Api(value = "ReadHub 服务搜索")
@RestController
@RequestMapping("/readhub")
public class SearchReadHubController {

    @Resource
    private SearchReadHubService readHubService;

    @ApiImplicitParam(value = "获取「通用」的新闻内容参数", name = "readhubReq", dataType = "SearchReadhubReq")
    @ApiOperation(value = "获取「通用」的新闻内容", notes = "获取「通用」的新闻内容")
    @ResponseBody
    @RequestMapping(value = "/queryReadhubNews",method = RequestMethod.POST)
    public Result<ReadHubResponse<ReadHubVo>> queryReadhubNews(@RequestBody SearchReadhubReq readhubReq) {
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(readhubReq));
        Map<String, Object> resultMap = readHubService.queryByReadHubNews(object, ReadHubNewsEntity.class);
        ReadHubResponse<ReadHubVo> result = getSearchListResponse(resultMap, ReadHubVo.class);
        return new Result<ReadHubResponse<ReadHubVo>>().success(result);
    }

    @ApiOperation(value = "获取「热门话题」的新闻内容参数", notes = "获取「热门话题」的新闻内容参数")
    @ApiImplicitParam(value = "获取「热门话题」的新闻内容参数", name = "readhubReq", dataType = "SearchReadhubReq")
    @ResponseBody
    @RequestMapping(value = "/queryReadhubTopicNews",method = RequestMethod.POST)
    public Result<ReadHubResponse<ReadHubTopicVo>> queryReadhubTopicNews(@RequestBody SearchReadhubReq readhubReq) {
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(readhubReq));
        Map<String, Object> resultMap = readHubService.queryByReadHubNews(object, ReadHubTopicEntity.class);
        ReadHubResponse<ReadHubTopicVo> result = getSearchListResponse(resultMap, ReadHubTopicVo.class);
        return new Result<ReadHubResponse<ReadHubTopicVo>>().success(result);
    }

    @ApiOperation(value = "获取相关的新闻标题", notes = "获取相关的新闻标题")
    @ApiImplicitParam(value = "输入要搜索的内容", name = "title", dataType = "String")
    @ResponseBody
    @GetMapping("/queryTitle")
    public Result<JSONArray> queryTitleValues(@RequestParam(name = "title") String title) {
        JSONArray result = readHubService.queryTitleList(title);
        return new Result<JSONArray>().success(result);
    }

    @ApiOperation(value = "获取指定话题的最新消息", notes = "获取指定话题的最新消息",httpMethod = "GET")
    @ApiImplicitParam(value = "获取指定话题的最新消息", name = "topic", dataType = "String")
    @ResponseBody
    @GetMapping("/queryLastNews")
    public  Result<ReadHubResponse<ReadHubVo>> queryLastNewsByTopic(@RequestParam(name = "topic") String topic) {
        Map<String, Object> resultMap = readHubService.queryByTopicNewsLast(topic, ReadHubNewsEntity.class);
        ReadHubResponse<ReadHubVo> result = getSearchListResponse(resultMap, ReadHubVo.class);
        return new Result<ReadHubResponse<ReadHubVo>>().success(result);
    }

    @ApiOperation(value = "获取热门话题的最新消息", notes = "获取指定话题的最新消息",httpMethod = "GET")
    @ApiImplicitParam(value = "获取指定话题的最新消息", name = "topic", dataType = "String")
    @ResponseBody
    @GetMapping("/queryLastTopicNewsByTopic")
    public  Result<ReadHubResponse<ReadHubTopicVo>> queryLastTopicNewsByTopic(@RequestParam(name = "topic") String topic) {
        Map<String, Object> resultMap = readHubService.queryByTopicNewsLast(topic, ReadHubTopicEntity.class);
        ReadHubResponse<ReadHubTopicVo> result = getSearchListResponse(resultMap, ReadHubTopicVo.class);
        return new Result<ReadHubResponse<ReadHubTopicVo>>().success(result);
    }

    /**
     * 做数据搜索返回结果转换
     * @param resultMap
     * @param clazz 转换为需要在类型
     * @return
     */
    private <T> ReadHubResponse<T> getSearchListResponse(Map<String, Object> resultMap, Class<T> clazz) {
        ReadHubResponse<T> result = new ReadHubResponse<>();
        if (resultMap != null && resultMap.size() > 0) {
            List<ReadHubNewsEntity> resultList = (List<ReadHubNewsEntity>) resultMap.get("listVal");
            Map<String, Object> map = (Map<String, Object>) resultMap.get("aggs");
            Long total = (Long) resultMap.getOrDefault("total", 0L);
            List<T> readHubVoList = CustomBeanAndSuperUtils.convertPojos(resultList, clazz);
            result.setListVal(readHubVoList);
            result.setGroupMap(map);
            result.setTotal(total);
        }
        return result;
    }
}
