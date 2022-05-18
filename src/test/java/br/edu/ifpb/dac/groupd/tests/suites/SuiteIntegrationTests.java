package br.edu.ifpb.dac.groupd.tests.suites;

import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import br.edu.ifpb.dac.groupd.tests.integration.UserResourceTests;

@Suite
@Testable
//@SelectPackages({"br.edu.ifpb.dac.groupd.tests.unittest"})
@SelectClasses({
	UserResourceTests.class
})
@SuiteDisplayName("Suite de Testes de Integração")
public class SuiteIntegrationTests {}
