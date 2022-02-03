package com.bottlerocketstudios.lintrules.trojan

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.jupiter.api.Test

@Suppress("UnstableApiUsage")
class TrojanSourceDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = TrojanSourceDetector()
    override fun getIssues(): MutableList<Issue> = mutableListOf(TrojanSourceDetector.ISSUE)

    @Suppress("LongMethod") // sample source and result text add quite a few lines to the otherwise short function
    @Test
    fun lintScan_nonAsciiSource_sevenLintFailuresFound() {
        val stubFile = kotlin(
            """
package com.foo.bar.auth

class LoginThing {

    /** Don't use non-ascii characters anywhere in source code, like € or 👍 */
    fun runReports() {
        val isAdmin = false
        
        /*‮ } ⁦if (isAdmin)⁩ ⁦ begin admins only */
            // do admin only things
/* end admins only ‮ { ⁦*/
        // non-admin execution
    }
}
        """
        ).indented()

        val lintResult = lint()
            .files(stubFile)
            .allowDuplicates()
            .allowMissingSdk()
            .run()

        lintResult
            .expectErrorCount(9)
            .expect(
                """
src/com/foo/bar/auth/LoginThing.kt:5: Error: Found '?' on line 5 column 76: unicode characters (non-ascii) are forbidden in source and/or xml files.
  * int value 55357: https://unicodelookup.com/#55357
  * unicode value U+D83D: https://unicode-table.com/en/D83D
 [TrojanSourceDetector]
    /** Don't use non-ascii characters anywhere in source code, like € or 👍 */
    ^
src/com/foo/bar/auth/LoginThing.kt:5: Error: Found '?' on line 5 column 77: unicode characters (non-ascii) are forbidden in source and/or xml files.
  * int value 56397: https://unicodelookup.com/#56397
  * unicode value U+DC4D: https://unicode-table.com/en/DC4D
 [TrojanSourceDetector]
    /** Don't use non-ascii characters anywhere in source code, like € or 👍 */
    ^
src/com/foo/bar/auth/LoginThing.kt:5: Error: Found '€' on line 5 column 71: unicode characters (non-ascii) are forbidden in source and/or xml files.
  * int value 8364: https://unicodelookup.com/#8364
  * unicode value U+20AC: https://unicode-table.com/en/20AC
 [TrojanSourceDetector]
    /** Don't use non-ascii characters anywhere in source code, like € or 👍 */
    ^
src/com/foo/bar/auth/LoginThing.kt:9: Error: Found '‮' on line 9 column 12: unicode characters (non-ascii) are forbidden in source and/or xml files.
  * int value 8238: https://unicodelookup.com/#8238
  * unicode value U+202E: https://unicode-table.com/en/202E
 [TrojanSourceDetector]
        /*‮ } ⁦if (isAdmin)⁩ ⁦ begin admins only */
        ^
src/com/foo/bar/auth/LoginThing.kt:9: Error: Found '⁦' on line 9 column 16: unicode characters (non-ascii) are forbidden in source and/or xml files.
  * int value 8294: https://unicodelookup.com/#8294
  * unicode value U+2066: https://unicode-table.com/en/2066
 [TrojanSourceDetector]
        /*‮ } ⁦if (isAdmin)⁩ ⁦ begin admins only */
        ^
src/com/foo/bar/auth/LoginThing.kt:9: Error: Found '⁦' on line 9 column 31: unicode characters (non-ascii) are forbidden in source and/or xml files.
  * int value 8294: https://unicodelookup.com/#8294
  * unicode value U+2066: https://unicode-table.com/en/2066
 [TrojanSourceDetector]
        /*‮ } ⁦if (isAdmin)⁩ ⁦ begin admins only */
        ^
src/com/foo/bar/auth/LoginThing.kt:9: Error: Found '⁩' on line 9 column 29: unicode characters (non-ascii) are forbidden in source and/or xml files.
  * int value 8297: https://unicodelookup.com/#8297
  * unicode value U+2069: https://unicode-table.com/en/2069
 [TrojanSourceDetector]
        /*‮ } ⁦if (isAdmin)⁩ ⁦ begin admins only */
        ^
src/com/foo/bar/auth/LoginThing.kt:11: Error: Found '‮' on line 11 column 21: unicode characters (non-ascii) are forbidden in source and/or xml files.
  * int value 8238: https://unicodelookup.com/#8238
  * unicode value U+202E: https://unicode-table.com/en/202E
 [TrojanSourceDetector]
/* end admins only ‮ { ⁦*/
^
src/com/foo/bar/auth/LoginThing.kt:11: Error: Found '⁦' on line 11 column 25: unicode characters (non-ascii) are forbidden in source and/or xml files.
  * int value 8294: https://unicodelookup.com/#8294
  * unicode value U+2066: https://unicode-table.com/en/2066
 [TrojanSourceDetector]
/* end admins only ‮ { ⁦*/
^
9 errors, 0 warnings
                """.trimIndent()
            )
    }

    @Test
    fun lintScan_asciiOnlySource_noLintFailuresFound() {
        val stubFile = kotlin(
            """
package com.foo.bar.auth

class LoginThing {

    /** Can use ascii characters like $ */
    /** All supported ascii printable characters (32-126): !"#${'$'}%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~ */ 
    fun runReports() {
        val isAdmin = false
        
        if (isAdmin) {
            // do admin only things
        }
        // non-admin execution
    }
}
        """
        ).indented()

        val lintResult = lint()
            .files(stubFile)
            .allowDuplicates()
            .allowMissingSdk()
            .run()

        lintResult.expectClean()
    }

    @Test
    fun printOutAsciiValues() {
        val asciiControlChars = ASCII_CONTROL_CHARACTERS.map { asciiCharIntValue ->
            asciiCharIntValue.toChar()
        }.joinToString(separator = "")
        val asciiPrintableChars = ASCII_PRINTABLE_CHARACTERS.map { asciiCharIntValue ->
            asciiCharIntValue.toChar()
        }.joinToString(separator = "")
        println("ascii control chars($ASCII_CONTROL_CHARACTERS)=$asciiControlChars")
        println("ascii printable chars($ASCII_PRINTABLE_CHARACTERS)=$asciiPrintableChars")
    }
}
