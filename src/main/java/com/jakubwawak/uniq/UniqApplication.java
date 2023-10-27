/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.uniq;

import com.jakubwawak.uniq.engine.RandomWordGeneratorEngine;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Web application for generating random passwords
 */
@SpringBootApplication
@EnableVaadin({"com.jakubwawak"})
public class UniqApplication {

	public static String version = "v1.0.0";
	public static String build = "uq261023REV01";

	public static int testFlag = 0;

	public static RandomWordGeneratorEngine rge;

	/**
	 * Main application function
	 * @param args
	 */
	public static void main(String[] args) {
		if ( testFlag == 1 ){
			// start application tests
			UniqApplicationTests uat = new UniqApplicationTests();
		}
		else{
			// run normal mode
			rge = new RandomWordGeneratorEngine();
			SpringApplication.run(UniqApplication.class, args);
		}

	}

}
