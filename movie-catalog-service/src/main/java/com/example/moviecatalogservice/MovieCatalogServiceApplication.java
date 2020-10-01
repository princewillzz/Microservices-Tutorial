package com.example.moviecatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
// One of the two ways
// make this application a hystric dashboard in addition to what it does
// On production you would set up another application as a hystrix dashboard and
// set up to listen to all requests
@EnableHystrixDashboard
public class MovieCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

	@Bean
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();

	}

	// Fault tolerant and resilience
	// Q.- Slow response is a problem
	// 0... Create more instances of the application
	// 1... use timeout to solve the problem of slow response
	// Does timeout solve the problem? -> Probably No, if the frequency of request
	// is greater than that of response (partly solved)
	// is faster than timeout the problem
	// 2... Use Intelligent circuit breaker (Be wise). if we are sending too mant
	// requests and not getting responses
	// that
	// fast than maybe wait for sometimes and send request once in a while to check
	// if everything is back up and than continue. This is very common in order to
	// solve fault tolerant in microservices
	// 3... Use Bulkhead Pattern.
	// Create Separate Thread Pool for each resources, so that one resource does not
	// consumes all the thread and rest of the resources suffer because of it.
	// Rather create separate thread pool which are isolated and reaching one's
	// maximum limit does not affect other services to slow down

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
