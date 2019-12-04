package com.crazy.kotlin_mvvm.ext

import java.math.BigDecimal

/**
 * Created by wtc on 2019/11/13
 */

/**
 * 格式化json字符串
 *
 * @return 格式化后的json串
 */
fun String.formatJson(): String {
    if (isNullOrEmpty()) return ""
    val sb = StringBuilder()
    var last = '\u0000'
    var current = '\u0000'
    var indent = 0
    for (element in this) {
        last = current
        current = element
        //遇到{ [换行，且下一行缩进
        when (current) {
            '{', '[' -> {
                sb.append(current)
                sb.append('\n')
                indent++
                addIndentBlank(sb, indent)
            }
            //遇到} ]换行，当前行缩进
            '}', ']' -> {
                sb.append('\n')
                indent--
                addIndentBlank(sb, indent)
                sb.append(current)
            }
            //遇到,换行
            ',' -> {
                sb.append(current)
                if (last != '\\') {
                    sb.append('\n')
                    addIndentBlank(sb, indent)
                }
            }
            else -> sb.append(current)
        }
    }
    return sb.toString()
}

/**
 * 添加space
 *
 * @param sb
 * @param indent
 */
private fun addIndentBlank(sb: StringBuilder, indent: Int) {
    for (i in 0 until indent) {
        sb.append('\t')
    }
}

fun String.stripTrailingZeros():String{
    if (isNullOrEmpty()) return "0.00"
    val decimal = BigDecimal(this)
    if (decimal.compareTo(BigDecimal.ZERO) == 0) return "0.00"
    return decimal.stripTrailingZeros().toPlainString()
}