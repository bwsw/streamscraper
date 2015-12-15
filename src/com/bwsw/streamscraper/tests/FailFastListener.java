package com.bwsw.streamscraper.tests;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class FailFastListener extends RunListener {

	private RunNotifier runNotifier;

	/**
	* Allow this Listener to access runNotifier
	*/
	public FailFastListener(RunNotifier runNotifier) {
	        super();
	        this.runNotifier=runNotifier;
	}

	/*
	 * (non-Javadoc)
	 * @see org.junit.runner.notification.RunListener#testFailure(org.junit.runner.notification.Failure)
	 */
	@Override
	public void testFailure(Failure failure) throws Exception {
	        this.runNotifier.pleaseStop();
	}
}
