package com.hua.tsf_spring_cloud_demo.tsf_spring_cloud_consumer.Controller;

import com.hua.tsf_spring_cloud_demo.tsf_spring_cloud_consumer.Entity.CustomMetadata;
import com.hua.tsf_spring_cloud_demo.tsf_spring_cloud_consumer.Interface.EchoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.tsf.core.TsfContext;
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

    //    /echo-rest/
    @RequestMapping(value = "/echo-rest/{str}", method = RequestMethod.GET)
//    @ApiOperation(value = "/echo-rest/{str}", notes = "consumer微服务，调用provider微服务，进行静态方式回显字符串，") // notes 对应 API 描述
    public String rest(@PathVariable String str,
                       @RequestParam(value = "user", required = false) String userValue,
                       @RequestParam(value = "org", required = false) String orgValue) {

//        通过url传参设置自定义标签
        if (!StringUtils.isEmpty(userValue)) {
            TsfContext.putTag("user", userValue);
            TsfContext.putCustomMetadata(new CustomMetadata("user", userValue));
        }

        if (!StringUtils.isEmpty(orgValue)) {
            TsfContext.putTag("org", orgValue);
            TsfContext.putCustomMetadata(new CustomMetadata("user", orgValue));
        }

        return restTemplate.getForObject("http://tsf-provider/echo/" + str, String.class);

    }

    //    /echo-async-rest/
    @RequestMapping(value = "/echo-async-rest/{str}", method = RequestMethod.GET)
//    @ApiOperation(value = "/echo-async-rest/{str}", notes = "consumer微服务，调用provider微服务，进行动态方式回显字符串，") // notes 对应 API 描述
    public String asyncRest(@PathVariable String str) throws Exception {
        ListenableFuture<ResponseEntity<String>> future = asyncRestTemplate.getForEntity("http://tsf-provider/echo/" + str, String.class);
        return future.get().getBody();
    }

    //    /echo-feign/
    @RequestMapping(value = "/echo-feign/{str}", method = RequestMethod.GET)
//    @ApiOperation(value = "/echo-feign/{str}", notes = "consumer微服务，调用provider微服务，进行feign方式回显字符串，") // notes 对应 API 描述
    public String feign(@PathVariable String str) {
        return echoService.echo(str);
    }

}
