package com.tairui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TaiRuiApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(TaiRuiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  智能钥匙管理系统启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " _  __  ______\n" +
                "| |/ / | ___ \\\n" +
                "| ' /  | |_/ /\n" +
                "|  <   | ___ \\\n" +
                "| . \\  | |_/ /\n" +
                "|_|\\_\\ \\____/\n" +
                "\n(♥◠‿◠)ﾉﾞ");
    }
}
