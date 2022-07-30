package br.edu.ifpb.dac.groupd.tests.unit;

import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@Testable
@SelectClasses({
	UserTests.class,
	BraceletTests.class,
	CoordinateTests.class,
	LocationTests.class,
	FenceTests.class,
	AlarmTests.class
})
@SuiteDisplayName("Suite de Testes Unitários")
public class UnitTestsSuite {}
