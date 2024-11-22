package com.acousea.backend;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectPackages("com.acousea.backend")
public class AllTestsSuite {

	@Test
	void contextLoads() {
	}

}
