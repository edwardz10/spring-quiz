package hello.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {

	@Bean
	public FreeMarkerConfigurer freemarkerConfig() {
		FreeMarkerConfigurer freemarkerConfig = new FreeMarkerConfigurer();
		freemarkerConfig.setTemplateLoaderPath("classpath:/templates");
		freemarkerConfig.setDefaultEncoding("UTF-8");

		return freemarkerConfig;
	}

	@Bean
	public ViewResolver viewResolver() {
		FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
		viewResolver.setCache(false);
		viewResolver.setPrefix("");
		viewResolver.setSuffix(".ftl");
		viewResolver.setContentType("text/html; charset=utf-8");
		return viewResolver;
	}
}
