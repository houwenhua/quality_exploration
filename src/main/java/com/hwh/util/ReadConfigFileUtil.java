package com.hwh.util;

import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadConfigFileUtil {
    public static Logger log = Logger.getLogger(ReadConfigFileUtil.class);

    /**
     * 获取properties格式的配置文件
     * @return
     */
    public static Properties readConfig() {
        //用于读取jar包内的配置文件
        //ClassPathResource resource = new ClassPathResource("quality_exploration.properties");
        //IOUtils.toString(resource.getStream(), StandardCharsets.UTF_8);

        //读取jar同目录配置文件
        String content = null;
        Properties p = null;
        String configFilePath = System.getProperty("user.dir") + File.separator + "quality_exploration.properties";
        log.info("获取配置文件【quality_exploration.properties】路径......" + configFilePath);
        p = readConfigContent(configFilePath);
        log.info(p.getProperty("regexp"));
        log.info("配置文件【quality_exploration.properties】读取成功......");
        return p;
    }

    /**
     * 读取配置文件内容
     */
    public static Properties readConfigContent(String configFilePath) {
        FileInputStream fis = null;
        Properties p = null;
        try {
            p = new Properties();
            fis = new FileInputStream(configFilePath);
            p.load(fis);
        } catch (IOException e) {
            log.error("配置文件【quality_exploration.properties】读取失败!!!" + e.getMessage());
            e.printStackTrace();
            //抛一个运行时异常，终止程序继续运行
            throw new RuntimeException(e.getMessage());
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return p;
    }

    public static void main(String[] args) {
        readConfig();
    }
}
