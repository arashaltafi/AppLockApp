package com.arash.altafi.applockapp

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.arash.altafi.applockapp.databinding.DialogLockAppBinding
import java.util.concurrent.Executor

class LockAppDialog(
    private val mContext: Context,
    private val isSetting: Boolean,
    private val cancelable: Boolean = false,
    private val correctPassword: String = "",
    var onSetCode: ((String) -> Unit)? = null,
    var onEnterCode: ((Boolean) -> Unit)? = null,
    var onForgetPass: (() -> Unit)? = null,
    var onBackPress: (() -> Unit)? = null
) : BaseDialog<DialogLockAppBinding>(mContext, R.style.Theme_AppLockApp), View.OnClickListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DialogLockAppBinding
        get() = DialogLockAppBinding::inflate

    companion object {
        private const val mCodeLength = 4
    }

    private var isRepeating = false
        set(value) {
            binding?.apply {
                if (value)
                    tvTitle.text = mContext.getString(R.string.please_enter_your_password_again)
                else
                    tvTitle.text = mContext.getString(R.string.please_enter_your_password)
            }

            field = value
        }

    private var firstCode = ""
    private var code = ""
        set(value) {
            field = value
            level = code.length
        }

    private var level = 0
        set(value) {
            field = value

            binding?.apply {
                when (value) {
                    0 -> {
                        ivCodeOne.clear()
                        ivCodeTwo.clear()
                        ivCodeThree.clear()
                        ivCodeFour.clear()
                    }
                    1 -> {
                        ivCodeOne.setImageResource(R.drawable.ic_star)
                        ivCodeTwo.clear()
                        ivCodeThree.clear()
                        ivCodeFour.clear()
                    }
                    2 -> {
                        ivCodeOne.setImageResource(R.drawable.ic_star)
                        ivCodeTwo.setImageResource(R.drawable.ic_star)
                        ivCodeThree.clear()
                        ivCodeFour.clear()
                    }
                    3 -> {
                        ivCodeOne.setImageResource(R.drawable.ic_star)
                        ivCodeTwo.setImageResource(R.drawable.ic_star)
                        ivCodeThree.setImageResource(R.drawable.ic_star)
                        ivCodeFour.clear()
                    }
                    4 -> {
                        ivCodeOne.setImageResource(R.drawable.ic_star)
                        ivCodeTwo.setImageResource(R.drawable.ic_star)
                        ivCodeThree.setImageResource(R.drawable.ic_star)
                        ivCodeFour.setImageResource(R.drawable.ic_star)
                        handleEnteredCode()
                    }
                }
            }
        }

    override fun viewHandler(view: View, savedInstanceState: Bundle?) {
        setCancelable(false)
        setupView()

        binding?.apply {
            btnForgetPass.setOnClickListener(this@LockAppDialog)
            btnFinger.setOnClickListener(this@LockAppDialog)
            btnDelete.setOnClickListener(this@LockAppDialog)
            btnCancel.setOnClickListener(this@LockAppDialog)
            btnZero.setOnClickListener(this@LockAppDialog)
            btnOne.setOnClickListener(this@LockAppDialog)
            btnTwo.setOnClickListener(this@LockAppDialog)
            btnThree.setOnClickListener(this@LockAppDialog)
            btnFour.setOnClickListener(this@LockAppDialog)
            btnFive.setOnClickListener(this@LockAppDialog)
            btnSix.setOnClickListener(this@LockAppDialog)
            btnSeven.setOnClickListener(this@LockAppDialog)
            btnEight.setOnClickListener(this@LockAppDialog)
            btnNine.setOnClickListener(this@LockAppDialog)
        }
    }

    private fun setCode(codeNum: Int) {
        if (code.length < mCodeLength) {
            code += codeNum.toString()
        }
    }

    private fun deleteCode() {
        if (code.isNotEmpty()) {
            code = code.dropLast(1)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnFinger -> handleFingerprint()
            R.id.btnForgetPass -> onForgetPass?.invoke()
            R.id.btnDelete -> deleteCode()
            R.id.btnCancel -> onBackPress?.invoke()
            R.id.btnZero -> setCode(0)
            R.id.btnOne -> setCode(1)
            R.id.btnTwo -> setCode(2)
            R.id.btnThree -> setCode(3)
            R.id.btnFour -> setCode(4)
            R.id.btnFive -> setCode(5)
            R.id.btnSix -> setCode(6)
            R.id.btnSeven -> setCode(7)
            R.id.btnEight -> setCode(8)
            R.id.btnNine -> setCode(9)
        }
    }

    private fun handleFingerprint() {
        if (isSetting.not()) {
            val executor = ContextCompat.getMainExecutor(context)
            val biometricManager = BiometricManager.from(context)
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> authUser(executor)
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> binding?.btnFinger?.toGone()
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> binding?.btnFinger?.toGone()
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> context.toast(context.getString(R.string.enable_fingerprint))
            }
        } else binding?.btnFinger?.toGone()

    }

    private fun authUser(executor: Executor) {

        //Here you create the authentication dialog by specifying its properties.
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(mContext.getString(R.string.authenticate))
//            .setSubtitle("هویت خود را تأیید کنید")
//            .setDescription("برای ادامه احراز هویت لازم است !!")
            .setNegativeButtonText(mContext.getString(R.string.cancel_btn))
//            .setConfirmationRequired(true)
            .setDeviceCredentialAllowed(false)
            .build()

        val biometricPrompt = getReadyBiometricPrompt(executor)

        //You authenticate the dialog by passing promptInfo, which holds the dialog properties, to biometricPrompt so you can see the dialog.
        biometricPrompt.authenticate(promptInfo)

    }

    private fun getReadyBiometricPrompt(executor: Executor): BiometricPrompt {
        val authenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                onEnterCode?.invoke(true)
                //When the user authenticates successfully, onAuthenticationSucceeded is triggered.
//                    showStatusTextView("Authentication Success")
            }

        }

        return if (mContext.isCastable<FragmentActivity>()) {
            BiometricPrompt(mContext.cast<FragmentActivity>()!!, executor, authenticationCallback)
        } else {
            val fragmentActivity = mContext.getActivity().cast<FragmentActivity>()!!
            BiometricPrompt(fragmentActivity, executor, authenticationCallback)
        }

    }

    private fun handleEnteredCode() {
        if (validationCheck().not()) return

        when {
            isSetting && isRepeating.not() -> {
                level = 0
                firstCode = code
                code = ""
                isRepeating = true
            }
            isSetting && isRepeating -> {
                onSetCode?.invoke(code)
            }
            isSetting.not() -> {
                onEnterCode?.invoke(true)
            }
        }

    }

    private fun validationCheck(): Boolean = when {
        level < mCodeLength -> {
            binding?.apply {
                tvTitle.text = mContext.getString(R.string.least_4_number)

                setIndicatorErrorStyle()
            }
            false
        }
        isSetting.not() && code != correctPassword -> {
            binding?.apply {
                tvTitle.text = mContext.getString(R.string.invalid_password)
                setIndicatorErrorStyle()
            }
            code = ""
            false
        }
        isRepeating && firstCode != code -> {
            binding?.apply {
                tvTitle.text = mContext.getString(R.string.invalid_pass_match)
                setIndicatorErrorStyle()
            }
            code = ""
            false
        }
        else -> true
    }

    private fun setupView() {
        binding?.apply {
            if (isSetting) {
                tvTitle.text = mContext.getString(R.string.please_enter_the_password)
                btnForgetPass.toHide()
            } else {
                if (cancelable.not())
                    btnCancel.toHide()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setIndicatorErrorStyle() {
        binding?.apply {
            val errorColorState = ColorStateList.valueOf(R.color.colorRed)
            ivCodeOne.backgroundTintList = errorColorState
            ivCodeTwo.backgroundTintList = errorColorState
            ivCodeThree.backgroundTintList = errorColorState
            ivCodeFour.backgroundTintList = errorColorState

            llIndicator.shakeError(
                object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        setIndicatorDefaultStyle()
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}

                }
            )

        }
    }

    private fun setIndicatorDefaultStyle() {
        binding?.apply {
            val defaultColorState: ColorStateList? = null
            ivCodeOne.backgroundTintList = defaultColorState
            ivCodeTwo.backgroundTintList = defaultColorState
            ivCodeThree.backgroundTintList = defaultColorState
            ivCodeFour.backgroundTintList = defaultColorState
        }
    }

    override fun show() {
        super.show()
        clearView()
    }

    private fun clearView() {
        firstCode = ""
        code = ""
        handleFingerprint()
        level = 0
        isRepeating = false
    }

    override fun onBackPressed() {
        onBackPress?.invoke()
    }

}