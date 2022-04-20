package br.edu.ifpb.dac.groupd.tests.suites;

import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import br.edu.ifpb.dac.groupd.tests.unit.BraceletTests;
import br.edu.ifpb.dac.groupd.tests.unit.CoordinateTests;
import br.edu.ifpb.dac.groupd.tests.unit.LocationTests;
import br.edu.ifpb.dac.groupd.tests.unit.UserTests;

@Suite
@Testable
//@SelectPackages({"br.edu.ifpb.dac.groupd.tests.unittest"})
@SelectClasses({
	UserTests.class,
	BraceletTests.class,
	CoordinateTests.class,
	LocationTests.class
})
@SuiteDisplayName("Suite de Testes Unitários")
public class SuiteUnitTests {}
