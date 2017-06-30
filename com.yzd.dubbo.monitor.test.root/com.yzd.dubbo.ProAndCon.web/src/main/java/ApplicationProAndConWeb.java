import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by zd.yao on 2017/6/28.
 */
@SpringBootApplication
@ComponentScan("com.yzd.dubbo.ProAndCon.web")
@ImportResource("classpath:com.yzd.dubbo.ProAndCon.web.xml")
@EnableScheduling
public class ApplicationProAndConWeb {
    /**
     * Used when run as JAR
     */
    public static void main(String[] args) {
        SpringApplication.run(ApplicationProAndConWeb.class, args);
    }
}