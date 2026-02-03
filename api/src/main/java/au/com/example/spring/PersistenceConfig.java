package au.com.example.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class PersistenceConfig {
    // Spring Boot auto-configures JPA/EntityManager based on application.properties
    // No manual configuration needed
}
