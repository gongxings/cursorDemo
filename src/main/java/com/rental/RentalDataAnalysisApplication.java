package com.rental;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.rental.dataAnalysis.mapper")
//开启定时任务
@EnableScheduling
public class RentalDataAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalDataAnalysisApplication.class, args);
	}

}
