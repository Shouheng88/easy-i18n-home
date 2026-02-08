package me.shouheng.i18n.utils

import androidx.compose.runtime.Composable
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.data.Const
import org.jetbrains.compose.resources.stringResource
import java.util.regex.Pattern
import kotlin.math.ceil

/** 字符串工具类 */
object StringUtils {

    private val HEX_DIGITS: CharArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    private val BASE_64_DIGITS: CharArray = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8',
        '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
        'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
        'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
        'Z', '+', '/',
    )

    /**
     * 判断字符串是否仅由大小写字母（A-Z、a-z）和下划线（_）组成
     * @param str 待检查的字符串
     * @return 符合条件返回 true，否则返回 false（空字符串也返回 false）
     */
    fun isAlphaUnderline(str: String): Boolean {
        // 正则表达式：^ 表示开头，$ 表示结尾，[A-Za-z_] 匹配大小写字母和下划线，+ 表示至少出现一次
        val regex = "^[A-Za-z_]+$".toRegex()
        return regex.matches(str)
    }

    /** 格式化时长信息 */
    @Composable
    fun formatDuration(duration: Long): String {
        val second = 1000
        val minute = 60 * second
        val hour = 60 * minute
        val day = 24 * hour

        val days = duration / day
        if (days > 0) {
            return "$days ${stringResource(Res.string.text_days)}"
        }
        val hours = duration / hour
        if (hours > 0) {
            return "$hours ${stringResource(Res.string.text_hours)}"
        }
        val minutes = duration / minute
        if (minutes > 0) {
            return "$minutes ${stringResource(Res.string.text_minutes)}"
        }
        val seconds = ceil(duration.toDouble() / second).toInt()
        return "$seconds ${stringResource(Res.string.text_seconds)}"
    }

    /** 判断是否为邮箱 */
    fun isEmail(s: String?): Boolean {
        if (s.isNullOrBlank()) return false
        val pattern = Pattern.compile(Const.REGEX_EMAIL)
        val matcher = pattern.matcher(s)
        return matcher.matches()
    }

    /*----------------------------------normal strings--------------------------------------*/
    /**
     * 判断指定字符是否为空白字符，空白符包含：空格、tab 键、换行符
     *
     * @param s 要判断的字符串
     * @return 当字符串为空或者字符串中所有的字符都是空白字符的时候返回 true
     */
    fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s.get(i))) {
                return false
            }
            ++i
        }
        return true
    }

    /**
     * 指定的字符串是否为空，null 或者长度为 0
     *
     * @param s 字符串
     * @return true 表示为空
     */
    fun isEmpty(s: CharSequence?): Boolean {
        return s == null || s.length == 0
    }

    /**
     * 指定的字符串 [String.trim] 之后是否为空
     *
     * @param s 字符串
     * @return true 表示为空
     */
    fun isTrimEmpty(s: String?): Boolean {
        return (s == null || s.trim { it <= ' ' }.length == 0)
    }

    /**
     * 判断两个 CharSequence 是否相等
     *
     * @param s1 CharSequence 1
     * @param s2 CharSequence 2
     * @return true 表示相等
     */
    fun equals(s1: CharSequence?, s2: CharSequence?): Boolean {
        if (s1 === s2) return true
        var length = 0
        if (s1 != null && s2 != null && (s1.length.also { length = it }) == s2.length) {
            if (s1 is String && s2 is String) {
                return s1 == s2
            } else {
                for (i in 0..<length) {
                    if (s1[i] != s2[i]) return false
                }
                return true
            }
        }
        return false
    }

    /**
     * 忽略大小写之后，判断两个 String 是否相等
     *
     * @param s1 String 1
     * @param s2 String 2
     * @return true 表示相等
     */
    fun equalsIgnoreCase(s1: String?, s2: String?): Boolean {
        return s1?.equals(s2, ignoreCase = true) ?: (s2 == null)
    }

    /**
     * 获取 CharSequence 的长度
     *
     * @param s CharSequence
     * @return null 的话返回 0，否则返回字符串长度
     */
    fun length(s: CharSequence?): Int {
        return s?.length ?: 0
    }

    /**
     * 字符串的首字符大写
     *
     * @param s 字符串
     * @return 处理之后的字符串
     */
    fun upperFirstLetter(s: String?): String {
        if (s == null || s.isEmpty()) return ""
        if (!Character.isLowerCase(s[0])) return s
        return (s[0].code - 32).toChar().toString() + s.substring(1)
    }

    /**
     * 字符串的首字符小写
     *
     * @param s 字符串
     * @return 处理之后的字符串
     */
    fun lowerFirstLetter(s: String?): String {
        if (s == null || s.isEmpty()) return ""
        if (!Character.isUpperCase(s[0])) return s
        return (s[0].code + 32).toChar().toString() + s.substring(1)
    }

    /**
     * 字符串反转
     *
     * @param s 字符串
     * @return 反转之后的字符串
     */
    fun reverse(s: String?): String {
        if (s == null) return ""
        val len = s.length
        if (len <= 1) return s
        val mid = len shr 1
        val chars = s.toCharArray()
        var c: Char
        for (i in 0..<mid) {
            c = chars[i]
            chars[i] = chars[len - i - 1]
            chars[len - i - 1] = c
        }
        return String(chars)
    }

    /**
     * 获取指定的字节数组对应的十六进制字符串，按照 ASCII 码表计算
     * 比如 ABCDEFGHIJKLMNOPQRSTUVWXYZ
     * 将得到 4142434445464748494A4B4C4D4E4F505152535455565758595A
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    fun bytes2HexString(bytes: ByteArray?): String {
        if (bytes == null) return ""
        val len = bytes.size
        if (len <= 0) return ""
        val ret = CharArray(len shl 1)
        var i = 0
        var j = 0
        while (i < len) {
            // 字节的高八位
            ret[j++] = HEX_DIGITS[bytes[i].toInt() shr 4 and 0x0f]
            // 字节的低八位
            ret[j++] = HEX_DIGITS[bytes[i].toInt() and 0x0f]
            i++
        }
        return String(ret)
    }

    /**
     * 将十六进制字符串转换回字节数组
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    fun hexString2Bytes(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hexString[i], 16) * 16) +  Character.digit(hexString[i+1], 16)).toByte()
            i += 2
        }
        return data
    }

    /**
     * 将数字转换成六十四进制字符串
     *
     * @param number 数字
     * @return 字符串
     */
    fun long2Base64String(number: Long): String {
        var number = number
        val buf = CharArray(64)
        var charPos = 64
        val radix = 1 shl 6
        val mask = radix - 1L // 截取后几位，在 [0,63] 之间
        do {
            buf[--charPos] = BASE_64_DIGITS[(number and mask).toInt()]
            number = number ushr 6
        } while (number != 0L)
        return String(buf, charPos, (64 - charPos))
    }

    /**
     * 将六十四进制字符串还原回数字
     *
     * @param base64String 六十四进制字符串
     * @return 数字
     */
    fun base64String2Long(base64String: String): Long {
        var result: Long = 0
        val length = base64String.length
        for (i in length - 1 downTo 0) {
            for (j in BASE_64_DIGITS.indices) {
                if (base64String.get(i) == BASE_64_DIGITS[j]) {
                    result += (j.toLong()) shl 6 * (base64String.length - 1 - i)
                }
            }
        }
        return result
    }
}

fun main() {
    println(StringUtils.isAlphaUnderline("1234ssad"))
    println(StringUtils.isAlphaUnderline("adsad_12"))
    println(StringUtils.isAlphaUnderline("adsadAs_das"))
    println(StringUtils.isAlphaUnderline("adsad As_das"))
}