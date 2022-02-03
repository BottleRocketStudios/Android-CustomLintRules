package com.bottlerocketstudios.lintrules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.bottlerocketstudios.lintrules.trojan.TrojanSourceDetector
import com.bottlerocketstudios.lintrules.trojan.TrojanXmlDetector

@Suppress("UnstableApiUsage")
/**
 * References the custom lint rules (detectors) to be picked up by lint
 *
 * Linkage performed via the Service Locator pattern where lintRules/src/main/resources/META-INF/services/com.android.tools.lint.client.api.IssueRegistry contents point to this file.
 */
class IssueRegistry : IssueRegistry() {
    override val api: Int = CURRENT_API
    override val issues: List<Issue> = listOf(TrojanSourceDetector.ISSUE, TrojanXmlDetector.ISSUE)
}
