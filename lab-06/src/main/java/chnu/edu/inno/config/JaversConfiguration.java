package chnu.edu.inno.config;

import org.javers.spring.auditable.AuthorProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaversConfiguration {

    private static class SimpleAuthorProvider implements AuthorProvider {
        @Override
        public String provide() {
            return "ADMIN";
        }
    }

    @Bean
    public AuthorProvider provideJaversAuthor() {
        return new SimpleAuthorProvider();
    }
}
