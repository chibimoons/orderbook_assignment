package com.junyoung.ha.assignment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.junyoung.ha.assignment.ui.MainNavHost
import com.junyoung.ha.assignment.ui.theme.MyApplicationTheme

@Composable
fun MainUiRenderer() {
    MyApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val appNavController = rememberNavController()
            MainNavHost(
                navController = appNavController
            )
        }
    }
}
