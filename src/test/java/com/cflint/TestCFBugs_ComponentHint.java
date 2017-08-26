package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.exception.CFLintScanException;
import com.cflint.plugins.core.ComponentHintChecker;

public class TestCFBugs_ComponentHint {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception {
        final CFLintConfig conf = new CFLintConfig();
        final PluginInfoRule pluginRule = new PluginInfoRule();
        pluginRule.setName("ComponentHintChecker");
        conf.getRules().add(pluginRule);
        final PluginMessage pluginMessage = new PluginMessage("COMPONENT_HINT_MISSING");
        pluginMessage.setSeverity(Levels.WARNING);
        pluginMessage.setMessageText("Component ${variable} is missing a hint.");
        pluginRule.getMessages().add(pluginMessage);

        cfBugs = new CFLint(conf, new ComponentHintChecker());
    }

    @Test
    public void testMissingHint() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"123\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_HINT_MISSING", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testBlankHint() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfargument name=\"xyz\" default=\"123\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_HINT_MISSING", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testHasHint() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent hint=\"This is a test component.\">\r\n" + "<cffunction name=\"test\" >\r\n"
                + "	<cfargument name=\"xyz\" default=\"123\">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        cfBugs.process(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

}
