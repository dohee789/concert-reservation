package kr.hhplus.concert.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private Info apiInfo() {
        return new Info()
                .title("Concert Reservation API")
                .description("api for concert reservation")
                .version("1.0.0");
    }
}




