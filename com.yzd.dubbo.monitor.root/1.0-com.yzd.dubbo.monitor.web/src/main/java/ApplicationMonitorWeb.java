import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
 * 解决：
 * The dependencies of some of the beans in the application context form a cycle
 * Created by zd.yao on 2017/6/26.
 */
@SpringBootApplication
@ComponentScan("com.yzd.dubbo.monitor.web,com.yzd.dubbo.monitor.service")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ImportResource("classpath:com.yzd.dubbo.monitor.provider.xml")
//todo 目前版本为运维版-关闭数据导入服务与数据收集服务
//@EnableScheduling
public class ApplicationMonitorWeb {
    /**
     * Used when run as JAR
     */
    public static void main(String[] args) {
        SpringApplication.run(ApplicationMonitorWeb.class, args);
    }
}