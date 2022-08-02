package br.edu.ifpb.dac.groupd.tests.system;

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
	UserSystemTest.class
})
@SuiteDisplayName("Suite de Testes de Sistema")
@ActiveProfiles("test")
@Profile("test")
@Testcontainers(disabledWithoutDocker = true)
@ContextConfiguration()
public class SystemTestsSuite {}
