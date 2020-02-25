package com.hua.tsf_spring_cloud_demo.tsf_spring_cloud_consumer.Config;

import com.tencent.tsf.consul.config.watch.ConfigChangeCallback;
import com.tencent.tsf.consul.config.watch.ConfigChangeListener;
import com.tencent.tsf.consul.config.watch.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ConfigChangeListener(prefix = "global.config", value = {"name"})
public class GlobalConfigChangeListener implements ConfigChangeCallback {
    private static final Logger log = LoggerFactory.getLogger(GlobalConfig.class);

    @Override
    public void callback(ConfigProperty lastConfigProperty, ConfigProperty newConfigProperty) {
        log.info("[TSF SDK] Configuration Change Listener: key: {}, old value: {}, new value: {}",
                lastConfigProperty.getKey(), lastConfigProperty.getValue(), newConfigProperty.getValue());
    }
}
