package com.android.shouldiwalk.utils;

import com.android.shouldiwalk.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class UtilTestWithRobolectric {

    @Test
    public void when_runWithRobolectric_then_isRoboUnitTestReturnsTrue() throws Exception {
        assertTrue(Util.isRoboUnitTest());
    }
}