# CustomLintRules
## What this library does?
This library currently provides the following lint rules that detect unsupported characters in source and xml files:

 * `TrojanSourceDetector` - covers Java and Kotlin files
 * `TrojanXmlDetector` - covers XML resource and Android manifest files

A fatal lint error will be reported if any unicode (or unsupported ascii) characters are detected. Only printable(`32..126`) + horizontal tab/line feed/carriage return(`9`, `10`, `13`) ascii characters are allowed.

## Sample characters that will cause a lint error 
* BIDI symbols: `LRE`, `RLE`, `LRO`, `RLO`, `LRI`, `RLI`, `FSI`, `PDF`, `PDI` (some of the actual symbols ⁦⁧⁩‮)
    * See https://trojansource.codes/trojan-source.pdf for more details
* Homoglyphs such as Н ([cyrillic en](https://en.wikipedia.org/wiki/En_(Cyrillic)))
* Symbols such as € or 👍
* Ascii control characters: `0..31` and `127`

## Details of the attack
* https://www.theregister.com/2021/11/01/trojan_source_language_reversal_unicode/
* https://nvd.nist.gov/vuln/detail/CVE-2021-42574
* https://trojansource.codes/
* https://github.com/nickboucher/trojan-source

## Additional Links
* https://www.w3schools.com/charsets/ref_html_ascii.asp - ascii ranges

## Build
* You can generate a locally built aar by executing the `publishReleasePublicationToMavenLocal` Run Configuration.
* Quickly navigate to the output by executing the `Open Local Maven Publication Folder` Run Configuration.
* View all jitpack builds at https://jitpack.io/#BottleRocketStudios/Android-CustomLintRules
* View root of jitpack generated data for this project at https://jitpack.io/com/github/BottleRocketStudios/Android-CustomLintRules/
    * Copy-paste additional values from page responses to the url path to traverse down the tree of all files managed by jitpack for your builds and view/retrieve things like log files, module files, pom files, and aars.