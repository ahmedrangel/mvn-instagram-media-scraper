package com.ahmedrangel.mvninstagrammediascraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MvnInstagramMediaScraperApplication {
	public static void main(String[] args) {
	  SpringApplication.run(MvnInstagramMediaScraperApplication.class, args);
		Scraper.scraper();
	}
}
