package com.example.myappcompose.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object TaskList : Screen("tasklist")
    object Drafts : Screen("drafts")
    object TaskForm : Screen("taskform?taskId={taskId}") {
        fun createRoute(taskId: String? = null) = if (taskId != null) {
            "taskform?taskId=$taskId"
        } else {
            "taskform"
        }
    }
}
