package com.tuusuario.trainapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tuusuario.trainapp.di.AppContainer
import com.tuusuario.trainapp.ui.navigation.AppNavHost
import com.tuusuario.trainapp.ui.theme.Theme

class MainActivity : ComponentActivity() {
    private val container: AppContainer by lazy { (application as TrainApp).container() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Theme {
                AppNavHost()
            }
        }
    }
}