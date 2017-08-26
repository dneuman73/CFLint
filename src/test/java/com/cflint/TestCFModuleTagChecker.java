package com.cflint;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.exception.CFLintScanException;
import com.cflint.plugins.core.CFXTagChecker;

public class TestCFModuleTagChecker {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception {
        final CFLintConfig conf = new CFLintConfig();
        final PluginInfoRule pluginRuleX = new PluginInfoRule();
        pluginRuleX.setName("CFXTagChecker");
        pluginRuleX.addParameter("tagName", "cfmodule");
        conf.getRules().add(pluginRuleX);
        final PluginMessage pluginMessageX = new PluginMessage("AVOID_USING_CFMODULE_TAG");
        pluginMessageX.setSeverity(Levels.WARNING);
        pluginMessageX.setMessageText(
                "Avoid Leaving <${tagName}> tags in committed code. Debug information should be ommited from release code");
        pluginRuleX.getMessages().add(pluginMessageX);
        final CFXTagChecker checker = new CFXTagChecker();
        checker.setParameter("tagName", "cfmodule");
        cfBugs = new CFLint(conf, checker);
    }

    @Test
    public void test_BAD() throws CFLintScanException {
        final String cfcSrc = "<cfmodule template=\"tagsExchRateCalculator.cfm\">";
        cfBugs.process(cfcSrc, "test");
        assertEquals(1, cfBugs.getBugs().getBugList().size());
    }

    @Test
    public void test_GOOD() throws CFLintScanException {
        final String cfcSrc = "<cfinsert " + "dataSource = \"data source name\" " + "tableName = \"table name\" "
                + "formFields = \"formfield1, formfield2, ...\" " + "password = \"password\" "
                + "tableOwner = \"owner\" " + "tableQualifier = \"table qualifier\" " + "username = \"user name\">";
        cfBugs.process(cfcSrc, "test");
        assertEquals(0, cfBugs.getBugs().getBugList().size());
    }

}
