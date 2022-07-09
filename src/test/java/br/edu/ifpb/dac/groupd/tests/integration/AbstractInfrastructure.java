package br.edu.ifpb.dac.groupd.tests.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {AbstractInfrastructure.Initializer.class})
@Testcontainers
public class AbstractInfrastructure {

    @Container
    public static PostgreSQLContainer<?> postgresDb = new PostgreSQLContainer<>("postgres:14.4")
      .withDatabaseName("projeto_dac_2022_1_test_db")
      .withUsername("projeto_dac_2022_1_test_user")
      .withPassword("projetodac20221test");
    
    static {
    	postgresDb.start();
    }

    static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        	
        	TestPropertySourceUtils.addInlinedPropertiesToEnvironment(configurableApplicationContext, 
        			new String[]{
        				"spring.datasource.url=" + postgresDb.getJdbcUrl(),
        				"spring.datasource.username=" + postgresDb.getUsername(),
        				"spring.datasource.password=" + postgresDb.getPassword()
        			}
        		);
        	
            TestPropertyValues.of(
              "spring.datasource.url=" + postgresDb.getJdbcUrl(),
              "spring.datasource.username=" + postgresDb.getUsername(),
              "spring.datasource.password=" + postgresDb.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}