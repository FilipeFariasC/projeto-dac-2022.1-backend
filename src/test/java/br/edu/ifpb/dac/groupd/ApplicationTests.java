package br.edu.ifpb.dac.groupd;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.ifpb.dac.groupd.tests.suites.SuiteUnitTests;

@SpringBootTest(classes= {ApplicationTests.class,SuiteUnitTests.class})
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
