package com.arash.altafi.applockapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arash.altafi.applockapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val sharedPreferencesPass = SharedPreferencesPass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedPreferencesPass.sharedPrefPass(this)
        init()
    }

    private fun init() {
        binding.apply {
            smAppLock.apply {
                isChecked = sharedPreferencesPass.getLock()

                setOnCheckedChangeListener { view, isChecked ->

                    if (view.isPressed.not())
                        return@setOnCheckedChangeListener

                    if (isChecked) {
                        LockAppDialog(
                            mContext = this@MainActivity,
                            isSetting = true
                        ).apply {
                            onBackPress = {
                                setChecked(false)
                                dismiss()
                            }
                            onSetCode =
                                {
                                    if (it.isNotEmpty()) {
                                        sharedPreferencesPass.savePass(it, true)
                                    }
                                    dismiss()
                                }
                        }.show()
                    } else {
                        LockAppDialog(
                            mContext = this@MainActivity,
                            cancelable = true,
                            correctPassword = sharedPreferencesPass.getPass(),
                            isSetting = false
                        ).apply {
                            onBackPress = {
                                setChecked(true)
                                dismiss()
                            }
                            onEnterCode = {
                                if (it) {
                                    dismiss()
                                    sharedPreferencesPass.clear()
                                    setChecked(false)
                                } else {
                                    dismiss()
                                    setChecked(true)
                                }
                            }
                            onForgetPass = {
                                //do logout
                                toast("please reinstall application")
                            }
                        }.show()
                    }
                }

            }
        }

    }

}