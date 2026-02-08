package me.shouheng.i18n.utils

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.*
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * The encrypt utils.
 *
 * @author Shouheng Wang (shouheng2020@gmail.com)
 * @version 2019/5/7 23:21
 */
object EncryptUtils {

    fun md2(data: String?): String {
        if (data == null || data.isEmpty()) return ""
        return md2(data.toByteArray())
    }

    fun md2(data: ByteArray?): String {
        return StringUtils.bytes2HexString(md2ToBytes(data))
    }

    fun md2ToBytes(data: ByteArray?): ByteArray? {
        return hashTemplate(data, "MD2")
    }

    fun md5(data: String?): String {
        if (data == null || data.isEmpty()) return ""
        return md5(data.toByteArray())
    }

    fun md5(data: String?, salt: String?): String {
        if (data == null && salt == null) return ""
        if (salt == null) return StringUtils.bytes2HexString(md5ToBytes(data!!.toByteArray()))
        if (data == null) return StringUtils.bytes2HexString(md5ToBytes(salt.toByteArray()))
        return StringUtils.bytes2HexString(md5ToBytes((data + salt).toByteArray()))
    }

    fun md5(data: ByteArray?): String {
        return StringUtils.bytes2HexString(md5ToBytes(data))
    }

    fun md5(data: ByteArray?, salt: ByteArray?): String {
        if (data == null && salt == null) return ""
        if (salt == null) return StringUtils.bytes2HexString(md5ToBytes(data))
        if (data == null) return StringUtils.bytes2HexString(md5ToBytes(salt))
        val dataSalt = ByteArray(data.size + salt.size)
        System.arraycopy(data, 0, dataSalt, 0, data.size)
        System.arraycopy(salt, 0, dataSalt, data.size, salt.size)
        return StringUtils.bytes2HexString(md5ToBytes(dataSalt))
    }

    fun md5ToBytes(data: ByteArray?): ByteArray? {
        return hashTemplate(data, "MD5")
    }

    fun md5File(filePath: String): String {
        val file = if (StringUtils.isSpace(filePath)) null else File(filePath)
        return md5File(file)
    }

    fun md5FileToBytes(filePath: String): ByteArray? {
        val file = if (StringUtils.isSpace(filePath)) null else File(filePath)
        return md5FileToBytes(file)
    }

    fun md5File(file: File?): String {
        return StringUtils.bytes2HexString(md5FileToBytes(file))
    }

    fun md5FileToBytes(file: File?): ByteArray? {
        if (file == null) return null
        var fis: FileInputStream? = null
        val digestInputStream: DigestInputStream?
        try {
            fis = FileInputStream(file)
            var md = MessageDigest.getInstance("MD5")
            digestInputStream = DigestInputStream(fis, md)
            val buffer = ByteArray(256 * 1024)
            while (true) {
                if (digestInputStream.read(buffer) <= 0) break
            }
            md = digestInputStream.messageDigest
            return md.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                fis?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun sha1(data: String?): String {
        if (data == null || data.isEmpty()) return ""
        return sha1(data.toByteArray())
    }

    fun sha1(data: ByteArray?): String {
        return StringUtils.bytes2HexString(sha1ToBytes(data))
    }

    fun sha1ToBytes(data: ByteArray?): ByteArray? {
        return hashTemplate(data, "SHA1")
    }

    fun sha224(data: String?): String {
        if (data == null || data.isEmpty()) return ""
        return sha224(data.toByteArray())
    }

    fun sha224(data: ByteArray?): String {
        return StringUtils.bytes2HexString(sha224ToBytes(data))
    }

    fun sha224ToBytes(data: ByteArray?): ByteArray? {
        return hashTemplate(data, "SHA224")
    }

    fun sha256(data: String?): String {
        if (data == null || data.isEmpty()) return ""
        return sha256(data.toByteArray())
    }

    fun sha256(data: ByteArray?): String {
        return StringUtils.bytes2HexString(sha256ToBytes(data))
    }

    fun sha256ToBytes(data: ByteArray?): ByteArray? {
        return hashTemplate(data, "SHA256")
    }

    fun sha384(data: String?): String {
        if (data == null || data.isEmpty()) return ""
        return sha384(data.toByteArray())
    }

    fun sha384(data: ByteArray?): String {
        return StringUtils.bytes2HexString(sha384ToBytes(data))
    }

    fun sha384ToBytes(data: ByteArray?): ByteArray? {
        return hashTemplate(data, "SHA384")
    }

    fun sha512(data: String?): String {
        if (data == null || data.isEmpty()) return ""
        return sha512(data.toByteArray())
    }

    fun sha512(data: ByteArray?): String {
        return StringUtils.bytes2HexString(sha512ToBytes(data))
    }

    fun sha512ToBytes(data: ByteArray?): ByteArray? {
        return hashTemplate(data, "SHA512")
    }

    /**
     * Return the hex string of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacMD5 encryption
     */
    fun HmacMD5(data: String?, key: String?): String {
        if (data == null || data.isEmpty() || key == null || key.isEmpty()) return ""
        return HmacMD5(data.toByteArray(), key.toByteArray())
    }

    /**
     * Return the hex string of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacMD5 encryption
     */
    fun HmacMD5(data: ByteArray?, key: ByteArray?): String {
        return StringUtils.bytes2HexString(HmacMD5ToBytes(data, key))
    }

    /**
     * Return the bytes of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacMD5 encryption
     */
    fun HmacMD5ToBytes(data: ByteArray?, key: ByteArray?): ByteArray? {
        return hmacTemplate(data, key, "HmacMD5")
    }

    /**
     * Return the hex string of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA1 encryption
     */
    fun HmacSHA1(data: String?, key: String?): String {
        if (data == null || data.isEmpty() || key == null || key.isEmpty()) return ""
        return HmacSHA1(data.toByteArray(), key.toByteArray())
    }

    /**
     * Return the hex string of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA1 encryption
     */
    fun HmacSHA1(data: ByteArray?, key: ByteArray?): String {
        return StringUtils.bytes2HexString(HmacSHA1ToBytes(data, key))
    }

    /**
     * Return the bytes of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA1 encryption
     */
    fun HmacSHA1ToBytes(data: ByteArray?, key: ByteArray?): ByteArray? {
        return hmacTemplate(data, key, "HmacSHA1")
    }

    /**
     * Return the hex string of HmacSHA224 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA224 encryption
     */
    fun HmacSHA224(data: String?, key: String?): String {
        if (data == null || data.isEmpty() || key == null || key.isEmpty()) return ""
        return HmacSHA224(data.toByteArray(), key.toByteArray())
    }

    /**
     * Return the hex string of HmacSHA224 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA224 encryption
     */
    fun HmacSHA224(data: ByteArray?, key: ByteArray?): String {
        return StringUtils.bytes2HexString(HmacSHA224ToBytes(data, key))
    }

    /**
     * Return the bytes of HmacSHA224 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA224 encryption
     */
    fun HmacSHA224ToBytes(data: ByteArray?, key: ByteArray?): ByteArray? {
        return hmacTemplate(data, key, "HmacSHA224")
    }

    /**
     * Return the hex string of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA256 encryption
     */
    fun HmacSHA256(data: String?, key: String?): String {
        if (data == null || data.isEmpty() || key == null || key.isEmpty()) return ""
        return HmacSHA256(data.toByteArray(), key.toByteArray())
    }

    /**
     * Return the hex string of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA256 encryption
     */
    fun HmacSHA256(data: ByteArray?, key: ByteArray?): String {
        return StringUtils.bytes2HexString(HmacSHA256ToBytes(data, key))
    }

    /**
     * Return the bytes of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA256 encryption
     */
    fun HmacSHA256ToBytes(data: ByteArray?, key: ByteArray?): ByteArray? {
        return hmacTemplate(data, key, "HmacSHA256")
    }

    /**
     * Return the hex string of HmacSHA384 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA384 encryption
     */
    fun HmacSHA384(data: String?, key: String?): String {
        if (data == null || data.isEmpty() || key == null || key.isEmpty()) return ""
        return HmacSHA384(data.toByteArray(), key.toByteArray())
    }

    /**
     * Return the hex string of HmacSHA384 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA384 encryption
     */
    fun HmacSHA384(data: ByteArray?, key: ByteArray?): String {
        return StringUtils.bytes2HexString(HmacSHA384ToBytes(data, key))
    }

    /**
     * Return the bytes of HmacSHA384 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA384 encryption
     */
    fun HmacSHA384ToBytes(data: ByteArray?, key: ByteArray?): ByteArray? {
        return hmacTemplate(data, key, "HmacSHA384")
    }

    /**
     * Return the hex string of HmacSHA512 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA512 encryption
     */
    fun HmacSHA512(data: String?, key: String?): String {
        if (data == null || data.isEmpty() || key == null || key.isEmpty()) return ""
        return HmacSHA512(data.toByteArray(), key.toByteArray())
    }

    /**
     * Return the hex string of HmacSHA512 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA512 encryption
     */
    fun HmacSHA512(data: ByteArray?, key: ByteArray?): String {
        return StringUtils.bytes2HexString(HmacSHA512ToBytes(data, key))
    }

    /**
     * Return the bytes of HmacSHA512 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA512 encryption
     */
    fun HmacSHA512ToBytes(data: ByteArray?, key: ByteArray?): ByteArray? {
        return hmacTemplate(data, key, "HmacSHA512")
    }

    /**
     * Return the Base64-encode bytes of DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the Base64-encode bytes of DES encryption
     */
    fun encryptDES2Base64(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return EncodeUtils.base64Encode(encryptDES(data, key, transformation, iv))
    }

    /**
     * Return the hex string of DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the hex string of DES encryption
     */
    fun encryptDES2HexString(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): String {
        return StringUtils.bytes2HexString(encryptDES(data, key, transformation, iv))
    }

    /**
     * Return the bytes of DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of DES encryption
     */
    fun encryptDES(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return symmetricTemplate(data, key, "DES", transformation, iv, true)
    }

    /**
     * Return the bytes of DES decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of DES decryption for Base64-encode bytes
     */
    fun decryptBase64DES(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return decryptDES(EncodeUtils.base64Decode(data), key, transformation, iv)
    }

    /**
     * Return the bytes of DES decryption for hex string.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of DES decryption for hex string
     */
    fun decryptHexStringDES(
        data: String,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return decryptDES(StringUtils.hexString2Bytes(data), key, transformation, iv)
    }

    /**
     * Return the bytes of DES decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of DES decryption
     */
    fun decryptDES(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return symmetricTemplate(data, key, "DES", transformation, iv, false)
    }

    /**
     * Return the Base64-encode bytes of 3DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the Base64-encode bytes of 3DES encryption
     */
    fun encrypt3DES2Base64(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return EncodeUtils.base64Encode(encrypt3DES(data, key, transformation, iv))
    }

    /**
     * Return the hex string of 3DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the hex string of 3DES encryption
     */
    fun encrypt3DES2HexString(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): String {
        return StringUtils.bytes2HexString(encrypt3DES(data, key, transformation, iv))
    }

    /**
     * Return the bytes of 3DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of 3DES encryption
     */
    fun encrypt3DES(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return symmetricTemplate(data, key, "DESede", transformation, iv, true)
    }

    /**
     * Return the bytes of 3DES decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of 3DES decryption for Base64-encode bytes
     */
    fun decryptBase64_3DES(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return decrypt3DES(EncodeUtils.base64Decode(data), key, transformation, iv)
    }

    /**
     * Return the bytes of 3DES decryption for hex string.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of 3DES decryption for hex string
     */
    fun decryptHexString3DES(
        data: String,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return decrypt3DES(StringUtils.hexString2Bytes(data), key, transformation, iv)
    }

    /**
     * Return the bytes of 3DES decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of 3DES decryption
     */
    fun decrypt3DES(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return symmetricTemplate(data, key, "DESede", transformation, iv, false)
    }

    /**
     * Return the Base64-encode bytes of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the Base64-encode bytes of AES encryption
     */
    fun encryptAES2Base64(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return EncodeUtils.base64Encode(encryptAES(data, key, transformation, iv))
    }

    /**
     * Return the hex string of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the hex string of AES encryption
     */
    fun encryptAES2HexString(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): String {
        return StringUtils.bytes2HexString(encryptAES(data, key, transformation, iv))
    }

    /**
     * Return the bytes of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of AES encryption
     */
    fun encryptAES(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return symmetricTemplate(data, key, "AES", transformation, iv, true)
    }

    /**
     * Return the bytes of AES decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of AES decryption for Base64-encode bytes
     */
    fun decryptBase64AES(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return decryptAES(EncodeUtils.base64Decode(data), key, transformation, iv)
    }

    /**
     * Return the bytes of AES decryption for hex string.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of AES decryption for hex string
     */
    fun decryptHexStringAES(
        data: String,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return decryptAES(StringUtils.hexString2Bytes(data), key, transformation, iv)
    }

    /**
     * Return the bytes of AES decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of AES decryption
     */
    fun decryptAES(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String,
        iv: ByteArray?
    ): ByteArray? {
        return symmetricTemplate(data, key, "AES", transformation, iv, false)
    }

    /**
     * Return the bytes of symmetric encryption or decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param algorithm      The name of algorithm.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param isEncrypt      True to encrypt, false otherwise.
     * @return the bytes of symmetric encryption or decryption
     */
    private fun symmetricTemplate(
        data: ByteArray?,
        key: ByteArray?,
        algorithm: String,
        transformation: String,
        iv: ByteArray?,
        isEncrypt: Boolean
    ): ByteArray? {
        if (data == null || data.size == 0 || key == null || key.size == 0) return null
        try {
            val secretKey: SecretKey?
            if ("DES" == algorithm) {
                val desKey = DESKeySpec(key)
                val keyFactory = SecretKeyFactory.getInstance(algorithm)
                secretKey = keyFactory.generateSecret(desKey)
            } else {
                secretKey = SecretKeySpec(key, algorithm)
            }
            val cipher = Cipher.getInstance(transformation)
            if (iv == null || iv.size == 0) {
                cipher.init(if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, secretKey)
            } else {
                val params: AlgorithmParameterSpec = IvParameterSpec(iv)
                cipher.init(if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, secretKey, params)
            }
            return cipher.doFinal(data)
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Return the Base64-encode bytes of RSA encryption.
     *
     * @param data           The data.
     * @param publicKey      The public key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *RSA/CBC/PKCS1Padding*.
     * @return the Base64-encode bytes of RSA encryption
     */
    fun encryptRSA2Base64(
        data: ByteArray?,
        publicKey: ByteArray?,
        keySize: Int,
        transformation: String
    ): ByteArray? {
        return EncodeUtils.base64Encode(encryptRSA(data, publicKey, keySize, transformation))
    }

    /**
     * Return the hex string of RSA encryption.
     *
     * @param data           The data.
     * @param publicKey      The public key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *RSA/CBC/PKCS1Padding*.
     * @return the hex string of RSA encryption
     */
    fun encryptRSA2HexString(
        data: ByteArray?,
        publicKey: ByteArray?,
        keySize: Int,
        transformation: String
    ): String {
        return StringUtils.bytes2HexString(encryptRSA(data, publicKey, keySize, transformation))
    }

    /**
     * Return the bytes of RSA encryption.
     *
     * @param data           The data.
     * @param publicKey      The public key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *RSA/CBC/PKCS1Padding*.
     * @return the bytes of RSA encryption
     */
    fun encryptRSA(
        data: ByteArray?,
        publicKey: ByteArray?,
        keySize: Int,
        transformation: String
    ): ByteArray? {
        return rsaTemplate(data, publicKey, keySize, transformation, true)
    }

    /**
     * Return the bytes of RSA decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param privateKey     The private key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *RSA/CBC/PKCS1Padding*.
     * @return the bytes of RSA decryption for Base64-encode bytes
     */
    fun decryptBase64RSA(
        data: ByteArray?,
        privateKey: ByteArray?,
        keySize: Int,
        transformation: String
    ): ByteArray? {
        return decryptRSA(EncodeUtils.base64Decode(data), privateKey, keySize, transformation)
    }

    /**
     * Return the bytes of RSA decryption for hex string.
     *
     * @param data           The data.
     * @param privateKey     The private key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *RSA/CBC/PKCS1Padding*.
     * @return the bytes of RSA decryption for hex string
     */
    fun decryptHexStringRSA(
        data: String,
        privateKey: ByteArray?,
        keySize: Int,
        transformation: String
    ): ByteArray? {
        return decryptRSA(StringUtils.hexString2Bytes(data), privateKey, keySize, transformation)
    }

    /**
     * Return the bytes of RSA decryption.
     *
     * @param data           The data.
     * @param privateKey     The private key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *RSA/CBC/PKCS1Padding*.
     * @return the bytes of RSA decryption
     */
    fun decryptRSA(
        data: ByteArray?,
        privateKey: ByteArray?,
        keySize: Int,
        transformation: String
    ): ByteArray? {
        return rsaTemplate(data, privateKey, keySize, transformation, false)
    }

    /**
     * Return the bytes of RSA encryption or decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS1Padding*.
     * @param isEncrypt      True to encrypt, false otherwise.
     * @return the bytes of RSA encryption or decryption
     */
    private fun rsaTemplate(
        data: ByteArray?,
        key: ByteArray?,
        keySize: Int,
        transformation: String,
        isEncrypt: Boolean
    ): ByteArray? {
        if (data == null || data.isEmpty() || key == null || key.isEmpty()) {
            return null
        }
        try {
            val rsaKey: Key?
            val keyFactory = KeyFactory.getInstance("RSA")
            if (isEncrypt) {
                val keySpec = X509EncodedKeySpec(key)
                rsaKey = keyFactory.generatePublic(keySpec)
            } else {
                val keySpec = PKCS8EncodedKeySpec(key)
                rsaKey = keyFactory.generatePrivate(keySpec)
            }
            if (rsaKey == null) return null
            val cipher = Cipher.getInstance(transformation)
            cipher.init(if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, rsaKey)
            val len = data.size
            var maxLen = keySize / 8
            if (isEncrypt) {
                val lowerTrans = transformation.lowercase(Locale.getDefault())
                if (lowerTrans.endsWith("pkcs1padding")) {
                    maxLen -= 11
                }
            }
            val count = len / maxLen
            if (count > 0) {
                var ret = ByteArray(0)
                var buff = ByteArray(maxLen)
                var index = 0
                for (i in 0..<count) {
                    System.arraycopy(data, index, buff, 0, maxLen)
                    ret = joins(ret, cipher.doFinal(buff))
                    index += maxLen
                }
                if (index != len) {
                    val restLen = len - index
                    buff = ByteArray(restLen)
                    System.arraycopy(data, index, buff, 0, restLen)
                    ret = joins(ret, cipher.doFinal(buff))
                }
                return ret
            } else {
                return cipher.doFinal(data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Return the bytes of RC4 encryption/decryption.
     *
     * @param data The data.
     * @param key  The key.
     */
    fun rc4(data: ByteArray?, key: ByteArray?): ByteArray? {
        if (data == null || data.isEmpty() || key == null) return null
        require(!(key.size < 1 || key.size > 256)) { "key must be between 1 and 256 bytes" }
        val iS = ByteArray(256)
        val iK = ByteArray(256)
        val keyLen = key.size
        for (i in 0..255) {
            iS[i] = i.toByte()
            iK[i] = key[i % keyLen]
        }
        var j = 0
        var tmp: Byte
        for (i in 0..255) {
            j = (j + iS[i] + iK[i]) and 0xFF
            tmp = iS[j]
            iS[j] = iS[i]
            iS[i] = tmp
        }

        val ret = ByteArray(data.size)
        var i = 0
        var k: Int
        var t: Int
        for (counter in data.indices) {
            i = (i + 1) and 0xFF
            j = (j + iS[i]) and 0xFF
            tmp = iS[j]
            iS[j] = iS[i]
            iS[i] = tmp
            t = (iS[i] + iS[j]) and 0xFF
            k = iS[t].toInt()
            ret[counter] = (data[counter].toInt() xor k).toByte()
        }
        return ret
    }

    fun hashTemplate(data: ByteArray?, algorithm: String): ByteArray? {
        if (data == null || data.size <= 0) return null
        try {
            val md = MessageDigest.getInstance(algorithm)
            md.update(data)
            return md.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return ByteArray(0)
        }
    }

    private fun joins(prefix: ByteArray, suffix: ByteArray): ByteArray {
        val ret = ByteArray(prefix.size + suffix.size)
        System.arraycopy(prefix, 0, ret, 0, prefix.size)
        System.arraycopy(suffix, 0, ret, prefix.size, suffix.size)
        return ret
    }

    /**
     * Return the bytes of hmac encryption.
     *
     * @param data      The data.
     * @param key       The key.
     * @param algorithm The name of hmac encryption.
     * @return the bytes of hmac encryption
     */
    private fun hmacTemplate(
        data: ByteArray?,
        key: ByteArray?,
        algorithm: String
    ): ByteArray? {
        if (data == null || data.size == 0 || key == null || key.size == 0) return null
        try {
            val secretKey = SecretKeySpec(key, algorithm)
            val mac = Mac.getInstance(algorithm)
            mac.init(secretKey)
            return mac.doFinal(data)
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
            return null
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }
    }
}