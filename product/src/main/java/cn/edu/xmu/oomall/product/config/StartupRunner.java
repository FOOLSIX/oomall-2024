//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.config;

import cn.edu.xmu.javaee.core.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupRunner implements ApplicationRunner {


    /**
     * Description: 启动时运行的方法
     * @author Ming Qiu
     * <p>
     * date: 2022-11-01 1:33
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        JwtHelper jwtHelper = new JwtHelper();
        String adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 0, 3600);
        log.info("admin token = {}", adminToken);
        String shopToken = jwtHelper.createToken(2L, "shop1", 1L, 1, 3600);
        log.info("shop 1 token = {}", shopToken);
    }
}
