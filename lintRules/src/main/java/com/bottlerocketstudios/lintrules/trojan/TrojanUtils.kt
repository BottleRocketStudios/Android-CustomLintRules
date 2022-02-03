package com.bottlerocketstudios.lintrules.trojan

import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Location
import java.util.Locale

@Suppress("UnstableApiUsage")
internal fun checkForUnicodeCharacters(context: Context, issue: Issue) {
    var zeroBasedLine = 0
    var zeroBasedColumn = 0
    val contents = context.getContents()
    contents?.forEach { char ->
        zeroBasedColumn++
        if (char == '\n' || char == '\r') {
            zeroBasedLine++
            zeroBasedColumn = 0
        }
        if (char.code !in SUPPORTED_ASCII_CHARACTERS) {
            reportUsage(context, issue, char, contents, zeroBasedLine, zeroBasedColumn)
        }
    }
}

@Suppress("UnstableApiUsage", "LongParameterList")
internal fun reportUsage(context: Context, issue: Issue, nonAsciiChar: Char, contents: CharSequence, zeroBasedLine: Int, zeroBasedColumn: Int) {
    val oneBasedLine = zeroBasedLine + 1
    val oneBasedColumn = zeroBasedColumn + 1
    val intValue = nonAsciiChar.code
    val unicodeValue = String.format(Locale.US, "u+%04x", intValue).uppercase(Locale.US)
    val unicodeValueNumberOnly = String.format(Locale.US, "%04x", intValue).uppercase(Locale.US)

    context.report(
        issue = issue,
        location = Location.create(context.file, contents.toString(), zeroBasedLine),
        message = """Found '$nonAsciiChar' on line $oneBasedLine column $oneBasedColumn: unicode characters (non-ascii) are forbidden in source and/or xml files.
            |  * int value $intValue: https://unicodelookup.com/#$intValue
            |  * unicode value $unicodeValue: https://unicode-table.com/en/$unicodeValueNumberOnly
            |""".trimMargin()
    )
}

@Suppress("MagicNumber") // AFAIK there are no java/kotlin constants to key off of so using direct integers
internal val ASCII_CONTROL_CHARACTERS: Set<Int> = (0..31).toSet().plus(setOf(127))

@Suppress("MagicNumber") // AFAIK there are no java/kotlin constants to key off of so using direct integers
internal val ASCII_TAB_AND_NEWLINE_CHARACTERS: Set<Int> = setOf(9, 10, 13)

@Suppress("MagicNumber") // AFAIK there are no java/kotlin constants to key off of so using direct integers
/** Ranges listed at https://www.w3schools.com/charsets/ref_html_ascii.asp */
internal val ASCII_PRINTABLE_CHARACTERS: Set<Int> = (32..126).toSet()
internal val SUPPORTED_ASCII_CHARACTERS: Set<Int> = ASCII_PRINTABLE_CHARACTERS + ASCII_TAB_AND_NEWLINE_CHARACTERS
