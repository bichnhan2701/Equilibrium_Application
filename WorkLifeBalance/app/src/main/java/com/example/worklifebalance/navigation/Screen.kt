package com.example.worklifebalance.navigation

import com.example.worklifebalance.R

sealed class Screen(val route: String, val label: String = "", val icon: Int? = null) {

    object Onboarding : Screen("onboarding", "Giới thiệu")
    object Splash : Screen("splash", "Splash")

    object Dashboard : Screen("dashboard", "Tổng quan", R.drawable.home_alt_svgrepo_com)
    object DomainManagement : Screen("domainManagement", "Lĩnh vực", R.drawable.home_alt_svgrepo_com)
    object GoalManagement : Screen("goalManagement", "Mục tiêu", R.drawable.octicon_goal_16)
    object AnalyticsReports : Screen("analyticsReports", "Báo cáo", R.drawable.chart_line_up_svgrepo_com)
    object Rest : Screen("rest", "Nhắc nhở nghỉ ngơi")
    object GoalDetailManagement : Screen("goalDetailManagement/{goalId}", "Chi tiết mục tiêu"){
        fun goalDetailManagementRoute(goalId: String): String { return  "goalDetailManagement/$goalId" }
    }
    object TaskManagement : Screen("taskManagement", "Nhiệm vụ", R.drawable.task_list_svgrepo_com)
    object UserProfile : Screen("userProfile", "Hồ sơ")
    object Login : Screen("login", "Đăng nhập")

    companion object {
        val items = listOf(Dashboard, DomainManagement, GoalManagement, TaskManagement, AnalyticsReports)
    }
}