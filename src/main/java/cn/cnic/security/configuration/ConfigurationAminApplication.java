package cn.cnic.security.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("cn.cnic.security.configuration.dao")
public class ConfigurationAminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigurationAminApplication.class);
    }
}
