package br.edu.ifpb.dac.groupd.tests.integration;

import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
@Suite
@Testable
@SelectClasses({
	UserResourceTests.class,
	SecurityTests.class,
	FenceResourcesTests.class,
	BraceletResourceTests.class,
	LocationResourcesTest.class
})
@SuiteDisplayName("Suite de Testes de Integração")
@ActiveProfiles("test")
@Profile("test")
@Testcontainers(disabledWithoutDocker = true)
@ContextConfiguration()
public class IntegrationTestsSuite {}
