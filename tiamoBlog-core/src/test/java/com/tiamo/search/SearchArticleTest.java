package com.tiamo.search;

import com.alibaba.fastjson.JSONObject;
import com.tiamo.Application;
import com.tiamo.entity.BlogEntity;
import com.tiamo.search.service.SearchArticle;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wangjian
 * @Date: 2019-03-11 15:49:28
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchArticleTest {
    @Resource
    private SearchArticle searchArticle;

    @Test
    public void searchTest()  {
        List<BlogEntity> list = searchArticle.queryByAuther("caoz");
        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    public void insertArticleTest() {
        List<BlogEntity> list = new ArrayList<>();
        BlogEntity entity = new BlogEntity();
        entity.setAuthor("caoz的梦呓");
        entity.setArticleId(UuidUtil.getTimeBasedUuid().toString());
        entity.setTitle("研发人员是怎样背锅的");
        String context = "本文完全虚构，不针对任何具体企业，如有雷同，纯属巧合。\n" +
                "\n" +
                "\n" +
                "\n" +
                "那么，我们假设一下，假设某个巨头企业，为了业绩增长也好，为了遏制竞争对手也好，要搞一场撒币大活动，抽奖也好，红包也好，反正有钱任性大活动。\n" +
                "\n" +
                "\n" +
                "\n" +
                "第一步，研发准备支撑活动，这是研发高光时刻，一连串反正说出来老百姓也不懂的数字，就知道很牛逼就对了，技术圈里自high一下，我们证明了自己的价值！实话说，写到简历里有加分，找下家的时候用得到。\n" +
                "\n" +
                "\n" +
                "\n" +
                "第二步，产品下载量激增，羊毛党纷纷出动，一看安装量数据爆表，业务部门开始表功第一轮，我们活动大获成功，新增全面超越竞争对手，创下历史新高。\n" +
                "\n" +
                "\n" +
                "\n" +
                "第三步，下载量不过是一时的暴增，打开率不行啊，开始暗搓搓的搞唤醒活动，偷偷发个推送啊，给一些易混淆的文案啊，反正就是你突然看到手机上有个提醒好像很重要，然后随手就点开了。这下打开率也提升了，业务部门表功第二轮，我们的日活跃数稳定增长，产品持续性得到验证。\n" +
                "\n" +
                "\n" +
                "\n" +
                "第四步，下载率，打开率都提升了，下一步是啥，变现啊！跟广告主吹牛逼，看第三方数据报告，看看，看看，我们比对手高多少 ，看看，我们现在排名在哪里，就一个字，涨价！\n" +
                "\n" +
                "\n" +
                "\n" +
                "有人说了，不应该是按点击付费么，但前面可以拉预付，签年度框架，压代理任务，然后表功第三轮，今年销售预期会有较大提高，目前已经确认预付及代理任务多少多少，老板很高兴，下个季度财务预期上调，投资人很高兴，股民很高兴，一切都很完美。\n" +
                "\n" +
                "\n" +
                "\n" +
                "第五步，一切都很完美，只有一个小问题，羊毛党和那些被各种推送诱惑打开的用户，无法带来真实的商业转化，于是广告主纷纷投诉，花了这么多钱没效果啊，业务部门发现，活跃数变不成匹配的点击数啊。竞争报告发现，活跃用户的广告转化比对手低一截啊。\n" +
                "\n" +
                "\n" +
                "\n" +
                "老板问了，咋回事。\n" +
                "\n" +
                "\n" +
                "\n" +
                "来，背锅侠，轮到你了！\n" +
                "\n" +
                "\n" +
                "\n" +
                "这肯定是技术不过关么，你看广告匹配率不好么，广告推荐算法不如对手么，广告自动优化策略不行么。\n" +
                "\n" +
                "\n" +
                "\n" +
                "研发同学们，其实不用慌，还记得第一步，你们写到简历里有加分么？现在可以用到了！\n" +
                "\n" +
                "\n" +
                "\n" +
                "再次声明，本文完全虚构，不针对任何具体企业，如有雷同，纯属巧合。\n" +
                "\n" +
                "\n" +
                "\n" +
                "但这个逻辑，在互联网企业运营里，其实一直很常见，业务人员靠一些羊毛活动和一些骚扰技巧获得大量用户，实现了账面数据上的成功，然而在商业变现的时候，这个数据下的用户价值，和那些口碑用户是不可比拟的，这时候，锅扔给研发背，把问题推给技术算法上，是很常见的。\n" +
                "\n" +
                "\n" +
                "\n" +
                "当然，你死磕说，研发不就应该有逆转乾坤的本事么，什么用户不能变现，什么流量不能变现。\n" +
                "\n" +
                "\n" +
                "\n" +
                "好吧，可以的，这事其实我懂，性感荷官在线发牌。";
        entity.setContext(context);
        entity.setCreateTime(new Date());
        list.add(entity);
        boolean b = searchArticle.insertArticle(list, "caoz", "article");
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<BlogEntity> articleList = searchArticle.queryByAuther("caoz");
        System.out.println(JSONObject.toJSONString(articleList));
    }
}
