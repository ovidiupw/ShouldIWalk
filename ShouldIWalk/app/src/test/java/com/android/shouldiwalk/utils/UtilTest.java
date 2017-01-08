package com.android.shouldiwalk.utils;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilTest {

    @Test
    public void when_notRunningWithRobolectric_then_isRoboUnitTestReturnsFalse() throws Exception {
        assertFalse(Util.isRoboUnitTest());
    }
}
