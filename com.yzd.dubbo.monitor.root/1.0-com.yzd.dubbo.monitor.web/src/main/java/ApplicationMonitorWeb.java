import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by zd.yao on 2017/6/26.
 */

@SpringBootApplication
@ComponentScan("com.yzd.dubbo.monitor.web")
@ImportResource("classpath:com.yzd.dubbo.monitor.provider.xml")
public class ApplicationMonitorWeb {
    /**
     * Used when run as JAR
     */
    public static void main(String[] args) {
        SpringApplication.run(ApplicationMonitorWeb.class, args);
    }
}