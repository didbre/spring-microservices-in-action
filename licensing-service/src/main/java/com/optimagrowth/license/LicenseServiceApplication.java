package com.optimagrowth.license;

import com.optimagrowth.license.utils.UserContextInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.stream.annotation.EnableBinding;


import java.util.Collections;
import java.util.List;

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
@EnableEurekaClient
@EnableBinding(Sink.class)
public class LicenseServiceApplication
{
	private static final Logger logger = LoggerFactory.getLogger(LicenseServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LicenseServiceApplication.class, args);
	}

	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setBasenames("messages");
		return messageSource;
	}

	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		if (interceptors == null) {
			restTemplate.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
		}
		else {
			restTemplate.setInterceptors(interceptors);
		}

		return restTemplate;
	}

//	@StreamListener(Sink.INPUT)
//	public void loggerSink(OrganizationChangeModel organizationChangeModel) {
//		logger.debug("Received an {} event for organization id {}", organizationChangeModel.getAction(), organizationChangeModel.getOrganizationId());
//	}


}
