package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintConfiguration;
import com.cflint.exception.CFLintScanException;

public class TestArrayNewChecker {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception {
        final CFLintConfiguration conf = CFLintConfig.createDefaultLimited("ArrayNewChecker");
        cfBugs = new CFLint(conf);
    }

    @Test
    public void testArrayNewInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "var a = 23;\r\n" + "var b = arrayNew(1);\r\n" + "</cfscript>";

        cfBugs.process(scriptSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_ARRAYNEW", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals(Levels.INFO, result.get(0).getSeverity());
        assertEquals("Use implict array construction instead (= []).", result.get(0).getMessage());
    }

    @Test
    public void testArrayNewMultiDimentionInScript() throws CFLintScanException {
        final String scriptSrc = "<cfscript>\r\n" + "var a = 23;\r\n" + "var b = arrayNew(3);\r\n" + "</cfscript>";

        cfBugs.process(scriptSrc, "test");
        assertEquals(0, cfBugs.getBugs().getBugList().size());
    }

    @Test
    public void testArrayNewInTag() throws CFLintScanException {
        final String tagSrc = "<cfset a = 23>\r\n" + "<cfset b = arrayNew(1)>";

        cfBugs.process(tagSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("AVOID_USING_ARRAYNEW", result.get(0).getMessageCode());
        assertEquals(2, result.get(0).getLine());
        assertEquals(Levels.INFO, result.get(0).getSeverity());
        assertEquals("Use implict array construction instead (= []).", result.get(0).getMessage());
    }

}
