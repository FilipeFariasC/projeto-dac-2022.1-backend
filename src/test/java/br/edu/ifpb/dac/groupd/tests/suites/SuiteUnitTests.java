package br.edu.ifpb.dac.groupd.tests.suites;

import org.junit.jupiter.api.DisplayName;
import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import br.edu.ifpb.dac.groupd.tests.unittest.BraceletTests;
import br.edu.ifpb.dac.groupd.tests.unittest.UserTests;

@Suite
@Testable
//@SelectPackages({"br.edu.ifpb.dac.groupd.tests.unittest"})
@SelectClasses({
	UserTests.class,
	BraceletTests.class
})
@DisplayName("Suite Testes Unitários")
class SuiteUnitTests {}
