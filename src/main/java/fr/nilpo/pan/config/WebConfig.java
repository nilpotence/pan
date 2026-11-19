package fr.nilpo.pan.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	/**
	 * Bundles are cached for a year: safe only because their URLs carry a
	 * content hash (/bundle/index-<md5>.css), so a deploy changes them.
	 * Templates must link bundles through <@spring.url>, which adds the hash
	 * (ResourceUrlEncodingFilter, enabled by spring.web.resources.chain.enabled).
	 *
	 * Other static files (/img/**) keep Spring Boot's handler, without a
	 * long cache: their URLs don't change with their content. They are
	 * revalidated on every use (spring.web.resources.cache.cachecontrol).
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/bundle/**")
				.addResourceLocations("classpath:/static/bundle/")
				.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic())
				.resourceChain(true)
				.addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
	}
}
