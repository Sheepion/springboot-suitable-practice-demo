package com.sheepion.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.sheepion.demo.mapper")
public class MybatisConfig {

}
