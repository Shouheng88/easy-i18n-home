package me.shouheng.i18n.vm

import kotlinx.coroutines.flow.MutableStateFlow
import me.shouheng.i18n.data.common.Resource

/** 设置翻译器时共享的 ViewModel */
class TranslatorSetterShareViewModel {

    /** 触发保存的事件 */
    var shouldSave = MutableStateFlow(false)

    /** 保存结果 */
    var saveResult = MutableStateFlow<Resource<Unit>?>(null)

    /** 触发保存操作 */
    fun triggerSave() {
        shouldSave.value = true
    }

    /** 保存结果 */
    fun onSaved(success: Boolean) {
        saveResult.value = if (success) Resource.success(Unit) else Resource.failure("", "")
        shouldSave.value = false
    }
}