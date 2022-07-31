package br.edu.ifpb.dac.groupd.tests.integration.setup;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Profile("test")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = {AbstractInfrastructure.Initializer.class})
@Testcontainers(disabledWithoutDocker = true)
public class AbstractInfrastructure {
	
    @Container
    public static PostgreSQLContainer<?> postgresDb;
    
    static {
    	init();
    }
    private static void init() {
    	postgresDb = new PostgreSQLContainer<>("postgres:14.4")
	        .withDatabaseName("projeto_dac_2022_1")
	        .withUsername("projeto_dac_2022_1")
	        .withPassword("projetodac20221")
	        .withAccessToHost(true)
	        .withExposedPorts(5432);
    	
    	postgresDb.addExposedPort(5432);
    	org.testcontainers.Testcontainers.exposeHostPorts(5432);
    	
    	postgresDb.start();
    }
   
	@DynamicPropertySource
	public static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url",postgresDb::getJdbcUrl);
		registry.add("spring.datasource.username", postgresDb::getUsername);
		registry.add("spring.datasource.password", postgresDb::getPassword);
	}

    static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        	init();
        	TestPropertySourceUtils.addInlinedPropertiesToEnvironment(configurableApplicationContext, 
        			new String[]{
        				"spring.datasource.url=" + postgresDb.getJdbcUrl(),
        				"spring.datasource.username=" + postgresDb.getUsername(),
        				"spring.datasource.password=" + postgresDb.getPassword(),
    			        "db.host=" + postgresDb.getHost(),
    			        "db.port=" + postgresDb.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
    			        "db.name=" + postgresDb.getDatabaseName(),
    			        "db.username=" + postgresDb.getUsername(),
    			        "db.password=" + postgresDb.getPassword()
        			}
        		);
        	
            TestPropertyValues.of(
              "spring.datasource.url=" + postgresDb.getJdbcUrl(),
              "spring.datasource.username=" + postgresDb.getUsername(),
              "spring.datasource.password=" + postgresDb.getPassword()
            ).applyTo(configurableApplicationContext);
        }
    }
}