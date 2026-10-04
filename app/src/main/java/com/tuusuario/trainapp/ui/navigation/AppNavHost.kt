package com.tuusuario.trainapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavBackStackEntry
import com.tuusuario.trainapp.ui.routine.RoutineListScreen
import com.tuusuario.trainapp.ui.routine.RoutineEditorScreen
import com.tuusuario.trainapp.ui.routine.DayEditorScreen
import com.tuusuario.trainapp.ui.routine.BlockEditorScreen
import com.tuusuario.trainapp.ui.routine.ExercisePickerScreen
import com.tuusuario.trainapp.ui.workout.WorkoutSetupScreen
import com.tuusuario.trainapp.ui.workout.WorkoutExecutionScreen
import com.tuusuario.trainapp.ui.history.HistoryScreen
import com.tuusuario.trainapp.ui.history.WorkoutDetailScreen
import com.tuusuario.trainapp.ui.stats.StatsScreen
import com.tuusuario.trainapp.ui.body.BodyScreen
import com.tuusuario.trainapp.ui.settings.SettingsScreen
import com.tuusuario.trainapp.ui.screen.HomeScreen

// Navigation routes
object NavRoutes {
    const val HOME = "home"
    const val ROUTINE_LIST = "routines"
    const val ROUTINE_EDITOR = "routine_editor/{routineId}"
    const val DAY_EDITOR = "day_editor/{routineId}/{dayId}"
    const val BLOCK_EDITOR = "block_editor/{routineId}/{dayId}/{blockId}"
    const val EXERCISE_PICKER = "exercise_picker/{routineId}/{dayId}/{blockId}"
    const val WORKOUT_SETUP = "workout_setup"
    const val WORKOUT_EXECUTION = "workout_execution/{workoutId}"
    const val HISTORY = "history"
    const val WORKOUT_DETAIL = "workout_detail/{workoutId}"
    const val STATS = "stats"
    const val BODY = "body"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavHost(
    startDestination: String = NavRoutes.HOME
) {
    val navController = rememberNavController()
    val bottomBarSelection = remember { mutableStateOf(0) }
    
    androidx.navigation.compose.NavHost(navController, startDestination) {
        // Home / Dashboard
        composable(NavRoutes.HOME) {
            HomeScreen(onNavigateToRoutines = { navController.navigate(NavRoutes.ROUTINE_LIST) },
                       onNavigateToWorkout = { navController.navigate(NavRoutes.WORKOUT_SETUP) })
        }
        
        // Routines Graph
        navigation(startDestination = NavRoutes.ROUTINE_LIST, route = "routines_graph") {
            composable(NavRoutes.ROUTINE_LIST) {
                RoutineListScreen(
                    onCreateRoutine = { navController.navigate(NavRoutes.ROUTINE_EDITOR.replace("{routineId}", "new")) },
                    onEditRoutine = { routineId -> navController.navigate(NavRoutes.ROUTINE_EDITOR.replace("{routineId}", routineId.toString())) },
                    onStartWorkout = { navController.navigate(NavRoutes.WORKOUT_SETUP) }
                )
            }
            composable(
                route = NavRoutes.ROUTINE_EDITOR,
                arguments = listOf(navArgument("routineId") { type = NavType.StringType })
            ) { backStackEntry ->
                val routineId = backStackEntry.arguments?.getString("routineId") ?: ""
                RoutineEditorScreen(
                    routineId = routineId,
                    onNavigateToDayEditor = { dayId -> navController.navigate(NavRoutes.DAY_EDITOR.replace("{routineId}", routineId).replace("{dayId}", dayId.toString())) },
                    onNavigateUp = { navController.popBackStack() }
                )
            }
            composable(
                route = NavRoutes.DAY_EDITOR,
                arguments = listOf(
                    navArgument("routineId") { type = NavType.StringType },
                    navArgument("dayId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val routineId = backStackEntry.arguments?.getString("routineId") ?: ""
                val dayId = backStackEntry.arguments?.getLong("dayId") ?: 0L
                DayEditorScreen(
                    routineId = routineId,
                    dayId = dayId,
                    onNavigateToBlockEditor = { blockId -> navController.navigate(NavRoutes.BLOCK_EDITOR.replace("{routineId}", routineId).replace("{dayId}", dayId.toString()).replace("{blockId}", blockId.toString())) },
                    onNavigateUp = { navController.popBackStack() }
                )
            }
            composable(
                route = NavRoutes.BLOCK_EDITOR,
                arguments = listOf(
                    navArgument("routineId") { type = NavType.StringType },
                    navArgument("dayId") { type = NavType.LongType },
                    navArgument("blockId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val routineId = backStackEntry.arguments?.getString("routineId") ?: ""
                val dayId = backStackEntry.arguments?.getLong("dayId") ?: 0L
                val blockId = backStackEntry.arguments?.getLong("blockId") ?: 0L
                BlockEditorScreen(
                    routineId = routineId,
                    dayId = dayId,
                    blockId = blockId,
                    onNavigateToExercisePicker = { navController.navigate(NavRoutes.EXERCISE_PICKER.replace("{routineId}", routineId).replace("{dayId}", dayId.toString()).replace("{blockId}", blockId.toString())) },
                    onNavigateUp = { navController.popBackStack() }
                )
            }
            composable(
                route = NavRoutes.EXERCISE_PICKER,
                arguments = listOf(
                    navArgument("routineId") { type = NavType.StringType },
                    navArgument("dayId") { type = NavType.LongType },
                    navArgument("blockId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val routineId = backStackEntry.arguments?.getString("routineId") ?: ""
                val dayId = backStackEntry.arguments?.getLong("dayId") ?: 0L
                val blockId = backStackEntry.arguments?.getLong("blockId") ?: 0L
                ExercisePickerScreen(
                    routineId = routineId,
                    dayId = dayId,
                    blockId = blockId,
                    onExerciseSelected = { navController.popBackStack() },
                    onNavigateUp = { navController.popBackStack() }
                )
            }
        }
        
        // Workout Graph
        navigation(startDestination = NavRoutes.WORKOUT_SETUP, route = "workout_graph") {
            composable(NavRoutes.WORKOUT_SETUP) {
                WorkoutSetupScreen(
                    onStartWorkout = { workoutId -> navController.navigate(NavRoutes.WORKOUT_EXECUTION.replace("{workoutId}", workoutId.toString())) },
                    onNavigateUp = { navController.popBackStack() }
                )
            }
            composable(
                route = NavRoutes.WORKOUT_EXECUTION,
                arguments = listOf(navArgument("workoutId") { type = NavType.LongType })
            ) { backStackEntry ->
                val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: 0L
                WorkoutExecutionScreen(
                    workoutId = workoutId,
                    onFinish = { navController.popBackStack(NavRoutes.HOME, inclusive = true) },
                    onNavigateUp = { navController.popBackStack() }
                )
            }
        }
        
        // History Graph
        navigation(startDestination = NavRoutes.HISTORY, route = "history_graph") {
            composable(NavRoutes.HISTORY) {
                HistoryScreen(
                    onWorkoutClick = { workoutId -> navController.navigate(NavRoutes.WORKOUT_DETAIL.replace("{workoutId}", workoutId.toString())) },
                    onNavigateUp = { navController.popBackStack() }
                )
            }
            composable(
                route = NavRoutes.WORKOUT_DETAIL,
                arguments = listOf(navArgument("workoutId") { type = NavType.LongType })
            ) { backStackEntry ->
                val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: 0L
                WorkoutDetailScreen(
                    workoutId = workoutId,
                    onNavigateUp = { navController.popBackStack() }
                )
            }
        }
        
        // Stats
        composable(NavRoutes.STATS) {
            StatsScreen(onNavigateUp = { navController.popBackStack() })
        }
        
        // Body
        composable(NavRoutes.BODY) {
            BodyScreen(onNavigateUp = { navController.popBackStack() })
        }
        
        // Settings
        composable(NavRoutes.SETTINGS) {
            SettingsScreen(onNavigateUp = { navController.popBackStack() })
        }
    }
}