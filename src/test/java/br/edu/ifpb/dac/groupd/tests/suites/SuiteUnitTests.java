package br.edu.ifpb.dac.groupd.tests.suites;

import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import br.edu.ifpb.dac.groupd.tests.unittest.BraceletTests;
import br.edu.ifpb.dac.groupd.tests.unittest.LocationTests;
import br.edu.ifpb.dac.groupd.tests.unittest.UserTests;

@Suite
@Testable
//@SelectPackages({"br.edu.ifpb.dac.groupd.tests.unittest"})
@SelectClasses({
	UserTests.class,
	BraceletTests.class,
	LocationTests.class
})
@SuiteDisplayName("Suite de Testes Unitários")
class SuiteUnitTests {}
