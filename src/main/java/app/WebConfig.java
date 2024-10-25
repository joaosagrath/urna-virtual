package app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/fotoCandidato/**")
                .addResourceLocations("file:/c:/fotos/fotoCandidato/");
        
        registry.addResourceHandler("/fotoEleitor/**")
                .addResourceLocations("file:/c:/fotos/fotoEleitor/");
    }
}

