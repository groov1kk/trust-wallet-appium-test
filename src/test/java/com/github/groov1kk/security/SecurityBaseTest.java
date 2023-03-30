package com.github.groov1kk.security;

import com.github.groov1kk.BaseTest;
import com.github.groov1kk.testng.ReinstallApplicationListener;
import org.testng.annotations.Listeners;

/**
 * We need a fresh copy of application for the security tests. So, this suite re-installs the
 * application after every test.
 *
 * @see ReinstallApplicationListener
 */
@Listeners(ReinstallApplicationListener.class)
public abstract class SecurityBaseTest extends BaseTest {}
