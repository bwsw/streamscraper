package com.bwsw.streamscraper.tests;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

public class StopOnFirstFailureSuite extends Suite {
	/**
	 * 
	 * @param klass
	 * @param suiteClasses
	 * @throws InitializationError
	 */
	public StopOnFirstFailureSuite(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
	        super(klass, suiteClasses);
	}

	public StopOnFirstFailureSuite(Class<?> klass) throws InitializationError {
	        super(klass, klass.getAnnotation(SuiteClasses.class).value());
	}


	/*
	 * (non-Javadoc)
	 * @see org.junit.runners.ParentRunner#run(org.junit.runner.notification.RunNotifier)
	 */
	@Override
	public void run(RunNotifier runNotifier) {
	        runNotifier.addListener(new FailFastListener(runNotifier));
	        super.run(runNotifier);
	}
}
