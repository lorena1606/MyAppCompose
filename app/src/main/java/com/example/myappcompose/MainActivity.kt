package com.example.myappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.myappcompose.domain.usecase.auth.GetCurrentUserUseCase
import com.example.myappcompose.domain.usecase.auth.LogoutUserUseCase
import com.example.myappcompose.ui.navigation.NavGraph
import com.example.myappcompose.ui.theme.MyAppComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @Inject
    lateinit var logoutUserUseCase: LogoutUserUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        getCurrentUserUseCase = getCurrentUserUseCase,
                        logoutUserUseCase = logoutUserUseCase
                    )
                }
            }
        }
    }
}
