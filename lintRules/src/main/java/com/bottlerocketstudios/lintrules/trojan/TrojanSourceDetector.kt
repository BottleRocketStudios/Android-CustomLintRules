package com.bottlerocketstudios.lintrules.trojan

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner

/**
 * This detector will report any usages of non-ascii characters in source files (java/kotlin).
 */
@Suppress("UnstableApiUsage")
class TrojanSourceDetector : Detector(), SourceCodeScanner {

    override fun beforeCheckFile(context: Context) {
        super.beforeCheckFile(context)
        checkForUnicodeCharacters(context, ISSUE)
    }

    companion object {
        private val IMPLEMENTATION = Implementation(
            TrojanSourceDetector::class.java,
            Scope.JAVA_FILE_SCOPE,
        )

        val ISSUE: Issue = Issue.create(
            id = "TrojanSourceDetector",
            briefDescription = "Unicode characters found in source file. Only printable(32..126) + horizontal tab/line feed/carriage return(9, 10, 13) ascii characters are allowed.",
            explanation = """
                See the following links for more info:
                * https://www.theregister.com/2021/11/01/trojan_source_language_reversal_unicode/
                * https://nvd.nist.gov/vuln/detail/CVE-2021-42574
                * https://github.com/nickboucher/trojan-source
                * https://trojansource.codes/
            """.trimIndent(),
            category = Category.SECURITY,
            priority = 10,
            severity = Severity.FATAL,
            androidSpecific = null,
            implementation = IMPLEMENTATION,
        )
    }
}
