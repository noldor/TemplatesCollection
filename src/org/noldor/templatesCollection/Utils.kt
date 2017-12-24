package org.noldor.templatesCollection

import java.lang.Character.isLetter

fun toSnakeCase(string: String): String {
    return string.trim().replace('-', '_').replace(' ', '_').toLowerCase()
}

fun toKebabCase(string: String): String {
    return string.trim().replace('_', '-').replace(' ', '-').toLowerCase()
}

fun toCamelCase(string: String): String {
    return transform(string)
}

fun toPascalCase(string: String): String {
    val s = transform(string)
    return s.substring(0, 1).toUpperCase() + s.substring(1)
}

private fun transform(string: String): String {
    val words = org.apache.commons.lang.StringUtils.splitByCharacterTypeCamelCase(string)

    var firstWordNotFound = true
    for (i in words.indices) {
        val word = words[i]
        if (word === "") {
            continue
        } else if (firstWordNotFound && startsWithLetter(word)) {
            words[i] = word.toLowerCase()
            firstWordNotFound = false
        } else {
            words[i] = com.intellij.openapi.util.text.StringUtil.capitalize(word.toLowerCase())
        }
    }

    return join(words).replace("-", "").replace("_", "").replace(" ", "")
}

private fun join(array: Array<out String>): String {
    return join(array, "")
}

private fun join(array: Array<out String>, separator: String?): String {
    return join(array, separator, 0, array.size)
}

private fun join(array: Array<out String>, separator: String?, startIndex: Int, endIndex: Int): String {
    var s = separator
    val empty = ""

    if (s == null) {
        s = empty
    }

    var bufSize = endIndex - startIndex
    if (bufSize <= 0) {
        return empty
    }

    bufSize *= array[startIndex].length + s.length

    val buf = StringBuffer(bufSize)

    for (i in startIndex until endIndex) {
        if (i > startIndex) {
            buf.append(separator)
        }
        buf.append(array[i])
    }
    return buf.toString()
}

private fun startsWithLetter(word: String): Boolean {
    return word.isNotEmpty() && isLetter(word[0])
}
