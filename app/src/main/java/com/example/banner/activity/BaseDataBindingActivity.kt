package com.example.banner.activity

import android.os.Bundle
import android.util.ArrayMap
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseDataBindingActivity<T : ViewDataBinding> : AppCompatActivity() {
    private var binding: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
    }

    private fun initDataBinding() {
        val config = dataBindingConfig()
        binding = DataBindingUtil.setContentView<T>(this, config.layoutId).apply {
            lifecycleOwner = this@BaseDataBindingActivity
            val variableIdList = config.variableIdList
            if (!variableIdList.isNullOrEmpty()) {
                for (entry in variableIdList) {
                    setVariable(entry.key, entry.value)
                }
            }
        }
    }

    abstract fun dataBindingConfig(): DataBindingConfig

    protected fun getBinding() = binding!!

    override fun onDestroy() {
        super.onDestroy()
        binding?.unbind()
        binding = null
    }
}

data class DataBindingConfig(
    val layoutId: Int,
    val variableIdList: ArrayMap<Int, Any> = ArrayMap()
) {

    fun addVariable(variableId: Int, value: Any): DataBindingConfig {
        variableIdList[variableId] = value
        return this
    }
}