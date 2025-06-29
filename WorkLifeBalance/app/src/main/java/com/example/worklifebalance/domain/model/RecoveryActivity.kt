package com.example.worklifebalance.domain.model

import androidx.compose.ui.graphics.Color

data class RecoveryActivity(
    val id: String,
    val name: String,
    val description: String,
    val durationInMinutes: Int,
    val color: Color,
)

val sampleRecoveryActivities = listOf(
    RecoveryActivity("1", "Hít thở sâu", "Thư giãn bằng cách hít thở sâu trong vài phút.", 5, Color(0xFF81C784)),
    RecoveryActivity("2", "Ngủ ngắn", "Chợp mắt 10-20 phút để phục hồi năng lượng.", 15, Color(0xFF64B5F6)),
    RecoveryActivity("3", "Tập thể dục nhẹ nhàng", "Vận động nhẹ như đi bộ, vươn vai.", 10, Color(0xFFFFB74D)),
    RecoveryActivity("4", "Thiền", "Ngồi thiền giúp tĩnh tâm và giảm stress.", 10, Color(0xFF9575CD)),
    RecoveryActivity("5", "Uống nước", "Bổ sung nước giúp tỉnh táo và khỏe mạnh.", 3, Color(0xFF4FC3F7)),
    RecoveryActivity("6", "Nghe nhạc thư giãn", "Thư giãn với bản nhạc yêu thích.", 7, Color(0xFFFF8A65)),
    RecoveryActivity("7", "Đọc sách", "Đọc vài trang sách để giải tỏa căng thẳng.", 10, Color(0xFFA1887F)),
    RecoveryActivity("8", "Viết nhật ký", "Ghi lại cảm xúc, suy nghĩ trong ngày.", 8, Color(0xFF90A4AE)),
    RecoveryActivity("9", "Tập yoga", "Thực hiện các động tác yoga đơn giản.", 12, Color(0xFFBA68C8)),
    RecoveryActivity("10", "Tắm rửa", "Tắm nhanh giúp làm mới tinh thần.", 10, Color(0xFF4DD0E1)),
    RecoveryActivity("11", "Ngắm cây xanh", "Thư giãn mắt bằng cách nhìn cây xanh.", 5, Color(0xFF388E3C)),
    RecoveryActivity("12", "Trò chuyện với bạn bè", "Nói chuyện với người thân hoặc bạn bè.", 10, Color(0xFFF06292)),
    RecoveryActivity("13", "Vẽ hoặc tô màu", "Thư giãn sáng tạo với bút màu.", 10, Color(0xFFFFD54F)),
    RecoveryActivity("14", "Ra ngoài trời", "Hít thở không khí trong lành ngoài trời.", 8, Color(0xFF009688)),
    RecoveryActivity("15", "Tự thưởng món ăn nhẹ", "Ăn nhẹ giúp lấy lại năng lượng.", 7, Color(0xFFD4E157))
)
