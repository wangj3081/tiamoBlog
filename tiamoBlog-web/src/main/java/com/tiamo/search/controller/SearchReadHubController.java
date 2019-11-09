package com.tiamo.search.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiamo.entity.ReadHubNewsEntity;
import com.tiamo.search.dto.request.SearchReadhubReq;
import com.tiamo.search.dto.response.ReadHubResponse;
import com.tiamo.search.service.SearchReadHubService;
import com.tiamo.util.Result;
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
 * @see SearchReadHubController
 * @since JDK1.8
 */
@Slf4j
@Api(value = "ReadHub 服务搜索")
@RestController
@RequestMapping("/readhub")
public class SearchReadHubController {

    @Resource
    private SearchReadHubService readHubService;

    @ResponseBody
    @RequestMapping(value = "/queryReadhubNews",method = RequestMethod.POST)
    public Result<ReadHubResponse<ReadHubNewsEntity>> queryReadhubNews(@RequestBody SearchReadhubReq readhubReq) {
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(readhubReq));
        Map<String, Object> resultMap = readHubService.queryByReadHubNews(object);
        ReadHubResponse<ReadHubNewsEntity> result = new ReadHubResponse<>();
        if (resultMap != null && resultMap.size() > 0) {
            List<ReadHubNewsEntity> resultList = (List<ReadHubNewsEntity>) resultMap.get("listVal");
            Map<String, Object> map = (Map<String, Object>) resultMap.get("aggs");
            Long total = (Long) resultMap.getOrDefault("total", 0L);
            result.setListVal(resultList);
            result.setGroupMap(map);
            result.setTotal(total);
        }
        return new Result<ReadHubResponse<ReadHubNewsEntity>>().success(result);
    }

    @ResponseBody
    @GetMapping("/queryTitle")
    public Result<JSONArray> queryTitleValues(@RequestParam(name = "title") String title) {
        JSONArray result = readHubService.queryTitleList(title);
        return new Result<JSONArray>().success(result);
    }

    @ApiOperation(value = "获取指定话题的最新消息", notes = "获取指定话题的最新消息",httpMethod = "get")
    @ApiImplicitParam(value = "获取指定话题的最新消息", name = "topic", dataType = "String")
    @ResponseBody
    @GetMapping("/queryLastNews")
    public Result<ReadHubResponse<ReadHubNewsEntity>> queryLastNewsByTopic(@RequestParam(name = "topic") String topic) {
      Map<String, Object> resultMap = readHubService.queryByTopicNewsLast(topic);
      ReadHubResponse<ReadHubNewsEntity> result = new ReadHubResponse<>();
      if (resultMap != null && resultMap.size() > 0) {
        List<ReadHubNewsEntity> resultList = (List<ReadHubNewsEntity>) resultMap.get("listVal");
        Map<String, Object> map = (Map<String, Object>) resultMap.get("aggs");
        Long total = (Long) resultMap.getOrDefault("total", 0L);
        result.setListVal(resultList);
        result.setGroupMap(map);
        result.setTotal(total);
      }
      return new Result<ReadHubResponse<ReadHubNewsEntity>>().success(null);
    }
}
