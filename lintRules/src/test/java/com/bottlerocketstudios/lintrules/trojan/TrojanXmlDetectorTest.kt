package com.bottlerocketstudios.lintrules.trojan

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

@Suppress("UnstableApiUsage")
class TrojanXmlDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = TrojanXmlDetector()
    override fun getIssues(): MutableList<Issue> = mutableListOf(TrojanXmlDetector.ISSUE)

    @Test
    fun lintScan_nonAsciiXmlInStringsXml_sixLintFailuresFound() {
        @Language("XML")
        val source = """
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- All supported ascii characters (32-126): !"#${'$'}%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~ -->
    <string name="api_key">⁧⁦123⁩⁦456⁩⁩</string> <!-- malicious api_key rendered as 456123 even though it seems to be 123456 -->
</resources>
        """
        val stubFile = xml("res/values/strings.xml", source).indented()

        val lintResult = lint()
            .files(stubFile)
            .allowDuplicates()
            .allowMissingSdk()
            .run()

        lintResult.expectErrorCount(6)
        lintResult.expect(
            """
    res/values/strings.xml:3: Error: Found '⁦' on line 3 column 30: unicode characters (non-ascii) are forbidden in source and/or xml files.
      * int value 8294: https://unicodelookup.com/#8294
      * unicode value U+2066: https://unicode-table.com/en/2066
     [TrojanXmlDetector]
        <string name="api_key">⁧⁦123⁩⁦456⁩⁩</string> <!-- malicious api_key rendered as 456123 even though it seems to be 123456 -->
        ^
    res/values/strings.xml:3: Error: Found '⁦' on line 3 column 35: unicode characters (non-ascii) are forbidden in source and/or xml files.
      * int value 8294: https://unicodelookup.com/#8294
      * unicode value U+2066: https://unicode-table.com/en/2066
     [TrojanXmlDetector]
        <string name="api_key">⁧⁦123⁩⁦456⁩⁩</string> <!-- malicious api_key rendered as 456123 even though it seems to be 123456 -->
        ^
    res/values/strings.xml:3: Error: Found '⁧' on line 3 column 29: unicode characters (non-ascii) are forbidden in source and/or xml files.
      * int value 8295: https://unicodelookup.com/#8295
      * unicode value U+2067: https://unicode-table.com/en/2067
     [TrojanXmlDetector]
        <string name="api_key">⁧⁦123⁩⁦456⁩⁩</string> <!-- malicious api_key rendered as 456123 even though it seems to be 123456 -->
        ^
    res/values/strings.xml:3: Error: Found '⁩' on line 3 column 34: unicode characters (non-ascii) are forbidden in source and/or xml files.
      * int value 8297: https://unicodelookup.com/#8297
      * unicode value U+2069: https://unicode-table.com/en/2069
     [TrojanXmlDetector]
        <string name="api_key">⁧⁦123⁩⁦456⁩⁩</string> <!-- malicious api_key rendered as 456123 even though it seems to be 123456 -->
        ^
    res/values/strings.xml:3: Error: Found '⁩' on line 3 column 39: unicode characters (non-ascii) are forbidden in source and/or xml files.
      * int value 8297: https://unicodelookup.com/#8297
      * unicode value U+2069: https://unicode-table.com/en/2069
     [TrojanXmlDetector]
        <string name="api_key">⁧⁦123⁩⁦456⁩⁩</string> <!-- malicious api_key rendered as 456123 even though it seems to be 123456 -->
        ^
    res/values/strings.xml:3: Error: Found '⁩' on line 3 column 40: unicode characters (non-ascii) are forbidden in source and/or xml files.
      * int value 8297: https://unicodelookup.com/#8297
      * unicode value U+2069: https://unicode-table.com/en/2069
     [TrojanXmlDetector]
        <string name="api_key">⁧⁦123⁩⁦456⁩⁩</string> <!-- malicious api_key rendered as 456123 even though it seems to be 123456 -->
        ^
    6 errors, 0 warnings
            """.trimIndent()
        )
    }

    @Test
    fun lintScan_asciiOnlyXmlInStringsXml_noLintFailuresFound() {
        @Language("XML")
        val source = """
            <resources xmlns:tools="http://schemas.android.com/tools">
                <!-- All supported ascii characters (32-126): !"#${'$'}%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~ -->
                <string name="app_name">BR Architecture</string>
            </resources>
        """
        val stubFile = xml("res/values/strings.xml", source).indented()

        val lintResult = lint()
            .files(stubFile)
            .allowDuplicates()
            .allowMissingSdk()
            .run()

        lintResult.expectClean()
    }
}
