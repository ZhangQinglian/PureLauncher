package com.zql.android.purelauncher;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.zql.android.purelauncher.presentation.framework.ContactBridge;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void contactTest(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        ContactBridge bridge = new ContactBridge();
        bridge.getContactActions("王");
    }
}
