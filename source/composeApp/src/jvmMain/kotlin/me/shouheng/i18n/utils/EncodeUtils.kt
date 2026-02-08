package me.shouheng.i18n.utils

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * @author Shouheng Wang (shouheng2020@gmail.com)
 * @version 2019/5/12 15:50
 */
object EncodeUtils  {

    /*---------------------------------- URL --------------------------------------*/
    fun urlEncode(input: String?, charsetName: String = "UTF-8"): String? {
        if (input == null || input.isEmpty()) return ""
        try {
            return URLEncoder.encode(input, charsetName)
        } catch (e: UnsupportedEncodingException) {
            throw AssertionError(e)
        }
    }

    fun urlDecode(input: String?, charsetName: String = "UTF-8"): String? {
        if (input == null || input.isEmpty()) return ""
        try {
            return URLDecoder.decode(input, charsetName)
        } catch (e: UnsupportedEncodingException) {
            throw AssertionError(e)
        }
    }

    /*---------------------------------- Base64 --------------------------------------*/
    fun base64Encode(input: String): ByteArray? {
        return base64Encode(input.toByteArray())
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun base64Encode(input: ByteArray?): ByteArray? {
        if (input == null || input.isEmpty()) return ByteArray(0)
        return runCatching {
            Base64.encodeToByteArray(input)
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun base64Encode2String(input: ByteArray?): String? {
        if (input == null || input.isEmpty()) return ""
        return runCatching {
            Base64.encode(input)
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun base64Decode(input: String?): ByteArray? {
        if (input == null || input.isEmpty()) return ByteArray(0)
        return runCatching {
            Base64.decode(input)
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun base64Decode(input: ByteArray?): ByteArray? {
        if (input == null || input.isEmpty()) return ByteArray(0)
        return runCatching {
            Base64.decode(input)
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }
}