package com.yzd.dubbo.monitor.web.conf;

import com.yzd.dubbo.monitor.service.service.inf.RegistryServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by zd.yao on 2017/7/13.
 */
@Component
public class StartupListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
    @Autowired
    RegistryServiceInf registryServiceInf;
    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        registryServiceInf.subscribe();
    }
}
