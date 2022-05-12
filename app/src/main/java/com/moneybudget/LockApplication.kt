package com.moneybudget

import android.app.KeyguardManager
import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


@Composable
fun LockApplication(
    mainActivity: FragmentActivity,
    onUnlock : () -> Unit = {}
){
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.ic_launcher_foreground
            ),
            contentDescription = "logo"
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold
        )

        val titleUnlock = stringResource(id = R.string.app_name)
        val subTitleUnlock = stringResource(id = R.string.unlockApplication)

        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = {
            val keyguardManager = mainActivity
                .applicationContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (!keyguardManager.isDeviceSecure) {
                onUnlock()
            } else {
                val executor = ContextCompat.getMainExecutor(mainActivity.applicationContext)
                val biometricPrompt = BiometricPrompt(mainActivity, executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult
                        ) {
                            super.onAuthenticationSucceeded(result)

                            onUnlock()
                        }
                    })

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(titleUnlock)
                    .setSubtitle(subTitleUnlock)
                    .setAllowedAuthenticators(
                        BiometricManager.Authenticators.BIOMETRIC_STRONG
                                or BiometricManager.Authenticators.BIOMETRIC_WEAK
                                or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                    .build()


                biometricPrompt.authenticate(promptInfo)
            }
        }) {
            Text(
                text = stringResource(id = R.string.unlockApplication),
                style = MaterialTheme.typography.button
            )
        }
    }
}