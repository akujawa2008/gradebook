package com.britenet.gradebook.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@PropertySource(value = "classpath:/open-api.properties")
public class SwaggerConfig {

    private final Environment environment;

    private List<Tag> getTagList() {
        return List.of(
                new Tag().name("classroom-controller").description(environment.getProperty("swagger.classroom-controller-v-1.description")),
                new Tag().name("grade-controller").description(environment.getProperty("swagger.grade-controller-v-1.description")),
                new Tag().name("password-reset-controller").description(environment.getProperty("swagger.password-reset-controller-v-1.description")),
                new Tag().name("subject-controller").description(environment.getProperty("swagger.subject-controller-v-1.description")),
                new Tag().name("users-controller").description(environment.getProperty("swagger.users-controller-v-1.description"))
        );
    }
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(apiInfo())
                .tags(getTagList());
    }

    private Info apiInfo() {
        return new Info()
                .title("Gradebook")
                .description("API for Gradebook Application")
                .version("1.0");
    }
}
