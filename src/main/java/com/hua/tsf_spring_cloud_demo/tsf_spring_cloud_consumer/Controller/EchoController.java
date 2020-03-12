package com.hua.tsf_spring_cloud_demo.tsf_spring_cloud_consumer.Controller;

import com.hua.tsf_spring_cloud_demo.tsf_spring_cloud_consumer.Config.ConsumerConfig;
import com.hua.tsf_spring_cloud_demo.tsf_spring_cloud_consumer.Config.GlobalConfig;
import com.hua.tsf_spring_cloud_demo.tsf_spring_cloud_consumer.Entity.CustomMetadata;
import com.hua.tsf_spring_cloud_demo.tsf_spring_cloud_consumer.Interface.EchoService;
import feign.FeignException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.tsf.faulttolerance.annotation.TsfFaultTolerance;
import org.springframework.cloud.tsf.faulttolerance.model.TsfFaultToleranceStragety;
import org.springframework.http.ResponseEntity;
import org.springframework.tsf.core.TsfContext;
import org.springframework.tsf.core.entity.Tag;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

@RestController
public class EchoController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AsyncRestTemplate asyncRestTemplate;
    @Autowired
    private EchoService echoService;

    // log对象
    private static final Logger LOG = LoggerFactory.getLogger(EchoController.class);

    //   配置中心consumer.config
    @Autowired
    private ConsumerConfig consumerConfig;

    //配置中心global.config
    @Autowired
    private GlobalConfig globalConfig;

    //    /echo-rest/
    @RequestMapping(value = "/echo-rest/{str}", method = RequestMethod.GET)
    @ApiOperation(value = "/echo-rest/{str}", notes = "consumer微服务，调用provider微服务，进行静态方式回显字符串，") // notes 对应 API 描述
    //容错
    @TsfFaultTolerance(strategy = TsfFaultToleranceStragety.FAIL_OVER, parallelism = 2,
            ignoreExceptions = {FeignException.class},
            raisedExceptions = {RuntimeException.class, InterruptedException.class},fallbackMethod = "doWorkFallback")
    public String rest(@PathVariable String str,
                       @RequestParam(required = false) String userID) {

        // 通过url传参设置自定义标签
        if (!StringUtils.isEmpty(userID)) {
            TsfContext.putTag("userID", userID);
            TsfContext.putCustomMetadata(new CustomMetadata("userID", userID));
        }

        // 日志
        LOG.info("tsf-consumer -- request param: [" + userID + "]");

        // 设置application.yml consumer.config.name的值,传入的userID 为name的值
        consumerConfig.setName("test" + userID);

        //设置global.config.name的值，传入的userID为name的值
        globalConfig.setName("test" + userID);


        // 获取consumer.config.name的值
        LOG.info("tsf-consumer -- consumer config name: [" + consumerConfig.getName() + ']');

        //获取global.config.name的值
        LOG.info("tsf-consumer -- global config name: [" + globalConfig.getName() + ']');


        return restTemplate.getForObject("http://tsf-provider/echo/" + str, String.class);
    }

    //    /echo-async-rest/
    @RequestMapping(value = "/echo-async-rest/{str}", method = RequestMethod.GET)
    @ApiOperation(value = "/echo-async-rest/{str}", notes = "consumer微服务，调用provider微服务，进行动态方式回显字符串，") // notes 对应 API 描述
    public String asyncRest(@PathVariable String str) throws Exception {
        ListenableFuture<ResponseEntity<String>> future = asyncRestTemplate.getForEntity("http://tsf-provider/echo/" + str, String.class);
        return future.get().getBody();
    }

    //    /echo-feign/
    @RequestMapping(value = "/echo-feign/{str}", method = RequestMethod.GET)
    @ApiOperation(value = "/echo-feign/{str}", notes = "consumer微服务，调用provider微服务，进行feign方式回显字符串，") // notes 对应 API 描述
    public String feign(@PathVariable String str) {
        return echoService.echo(str);
    }

    //容错处理程序
    public void doWorkFallback() {
        System.out.println("fallback");

    }

}
