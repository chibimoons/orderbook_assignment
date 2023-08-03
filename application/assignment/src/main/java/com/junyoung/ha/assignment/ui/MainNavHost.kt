package com.junyoung.ha.assignment.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.junyoung.ha.assignment.navigation.MainDirections
import com.junyoung.ha.assignment.ui.home.HomeUiRenderer

@Composable
fun MainNavHost(
    navController: NavHostController,
) {
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = MainDirections.route
    ) {
        composable(MainDirections.route) {
            HomeUiRenderer(hiltViewModel())
        }
    }
}