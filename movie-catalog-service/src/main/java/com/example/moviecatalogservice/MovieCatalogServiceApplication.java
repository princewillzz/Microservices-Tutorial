package com.example.moviecatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableEurekaClient
public class MovieCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

	@Bean
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();

	}

	// Fault tolerant and resileance
	// Q.- Slow response is a problem
	// 1. use timeout to solve the problem of slow response
	// Does timeout solve the problem? -> Probably No, if the frequency of request
	// is greater than that of response (partly solved)
	// is faster than timeout the problem
	// 2. Be wise. if we are sending too mant requests and not getting responses
	// that
	// fast than maybe wait for sometimes and send request once in a while to check
	// if everything is back up and than continue. This is very common in order to
	// solve fault tolerant in microservices

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(2000); // wait for 2 seconds for the response and if it takes longer
															// then timeout
		return new RestTemplate(clientHttpRequestFactory);
	}

	// Communcation between microservices (creation of a RestTemplate Bean)
	// @Bean
	// @LoadBalanced
	// public RestTemplate getRestTemplate() {
	// return new RestTemplate();
	// }

}
