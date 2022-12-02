package com.btxdev.kardex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class App {

	static {
		Locale.setDefault(Locale.forLanguageTag("es"));
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
