package org.dijure;

import edu.umd.cs.findbugs.Priorities;
import guru.nidi.codeassert.config.AnalyzerConfig;
import guru.nidi.codeassert.config.GradleAnalyzerConfig;
import guru.nidi.codeassert.config.In;
import guru.nidi.codeassert.dependency.DependencyRules;
import guru.nidi.codeassert.findbugs.BugCollector;
import guru.nidi.codeassert.findbugs.FindBugsAnalyzer;
import guru.nidi.codeassert.findbugs.FindBugsResult;
import org.junit.Test;

import static guru.nidi.codeassert.junit.CodeAssertMatchers.hasNoBugs;
import static org.junit.Assert.assertThat;

public class FindBugsTest {
    //@Test (enabled = false)
    public void findBugs() {
        // Analyze all sources in src/main/java
        AnalyzerConfig config = GradleAnalyzerConfig.gradle().main();

        // Only treat bugs with rank < 17 and with NORMAL_PRIORITY or higher
        // Ignore the given bug types in the given classes / methods.
        BugCollector collector = new BugCollector().maxRank(17).minPriority(Priorities.NORMAL_PRIORITY);
           /*     .just(In.everywhere().ignore("UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR"))
                .because("It's checked and OK like this",
                        In.classes(DependencyRules.class, PmdRuleset.class).ignore("DP_DO_INSIDE_DO_PRIVILEGED"),
                        In.classes("*Test", "Rulesets")
                                .and(In.classes("ClassFileParser").withMethods("parse"))
                                .ignore("URF_UNREAD_FIELD"));*/

        FindBugsResult result = new FindBugsAnalyzer(config, collector).analyze();
        assertThat(result, hasNoBugs());
    }
}