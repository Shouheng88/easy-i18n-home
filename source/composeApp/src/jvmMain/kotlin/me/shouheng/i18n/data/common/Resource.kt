package me.shouheng.i18n.data.common

class Resource<T> private constructor(
    /** The status of current resources, read only.  */
    val status: Status?,
    /** The data of current resources, read only.  */
    val data: T?,
    /** The code, read only.  */
    val code: String?,
    /** The message of current resources, read only.  */
    val message: String?,
    /** Appendix field  */
    val udf1: Long?,
    /** Appendix field  */
    val udf2: Double?,
    /** Appendix field  */
    val udf3: Boolean?,
    /** Appendix field  */
    val udf4: String?,
    /** Appendix field  */
    val udf5: Any?
) {

    /** The throwable for resources.  */
    var throwable: Throwable? = null

    val isSuccess: Boolean
        /** Is currently success state  */
        get() = status === Status.SUCCESS

    val isFailure: Boolean
        /** Is currently failed state  */
        get() = status === Status.FAILED

    val isLoading: Boolean
        /** Is currently loading state  */
        get() = status === Status.LOADING

    val isProgress: Boolean
        /** Is currently progress state  */
        get() = status === Status.PROGRESS

    override fun toString(): String {
        return "Resources{" +
                "status=" + status +
                ", data=" + data +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", udf1=" + udf1 +
                ", udf2=" + udf2 +
                ", udf3='" + udf3 + '\'' +
                ", udf4=" + udf4 +
                ", udf5=" + udf5 +
                '}'
    }

    companion object Companion {
        fun <U> success(
            data: U?,
            udf1: Long? = null,
            udf2: Double? = null,
            udf3: Boolean? = null,
            udf4: String? = null,
            udf5: Any? = null
        ): Resource<U> = Resource(Status.SUCCESS, data, null, null, udf1, udf2, udf3, udf4, udf5)

        fun <U> failure(
            code: String?,
            message: String?,
            udf1: Long? = null,
            udf2: Double? = null,
            udf3: Boolean? = null,
            udf4: String? = null,
            udf5: Any? = null,
            throwable: Throwable? = null,
        ): Resource<U> = Resource<U>(Status.FAILED, null, code, message, udf1, udf2, udf3, udf4, udf5).apply {
            this.throwable = throwable
        }

        fun <U> failure(
            from: Resource<*>
        ): Resource<U> = failure(from.code, from.message, from.udf1, from.udf2, from.udf3, from.udf4, from.udf5, from.throwable)

        fun <U> loading(
            udf1: Long? = null,
            udf2: Double? = null,
            udf3: Boolean? = null,
            udf4: String? = null,
            udf5: Any? = null
        ): Resource<U> = Resource(Status.LOADING, null, null, null, udf1, udf2, udf3, udf4, udf5)

        fun <U> progress(
            data: U,
            udf1: Long? = null,
            udf2: Double? = null,
            udf3: Boolean? = null,
            udf4: String? = null,
            udf5: Any? = null
        ): Resource<U> = Resource(Status.PROGRESS, data, null, null, udf1, udf2, udf3, udf4, udf5)
    }
}