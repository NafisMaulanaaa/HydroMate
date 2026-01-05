package com.example.testhydromate.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.data.model.DailyChartData

enum class ChartType { BAR, LINE }

@Composable
fun HydrationChart(
    data: List<DailyChartData>,
    type: ChartType,
    isPercentage: Boolean, // True jika chart %, False jika chart Volume (mL)
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableStateOf(data.lastIndex) }

    val activeColor = PrimaryBlue
    val inactiveColor = Color(0xFFE3F2FD) // Light Blue
    val pathColor = PrimaryBlue

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val barWidth = size.width / (data.size * 2f)
                    val stepX = size.width / data.size
                    val index = (offset.x / stepX).toInt().coerceIn(0, data.lastIndex)
                    selectedIndex = index
                }
            }
    ) {
        val w = size.width
        val h = size.height
        val barWidth = (w / data.size) * 0.4f
        val stepX = w / data.size

        val maxValue = if (isPercentage) 100f else data.maxOfOrNull { it.totalAmount.toFloat() }?.coerceAtLeast(1000f) ?: 2000f

        // --- DRAW CHART ---
        if (type == ChartType.BAR) {
            data.forEachIndexed { index, item ->
                val value = if (isPercentage) item.completionPercent else item.totalAmount.toFloat()
                val drawValue = value.coerceAtMost(maxValue)

                val barHeight = (drawValue / maxValue) * (h * 0.7f) // Pakai 70% tinggi canvas
                val x = (stepX * index) + (stepX / 2) - (barWidth / 2)
                val y = h - barHeight - 40f // 40f padding bawah untuk label text

                drawRoundRect(
                    color = if (index == selectedIndex) activeColor else inactiveColor,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(8f, 8f)
                )
            }
        } else {
            // LINE CHART (Bezier)
            val path = Path()
            data.forEachIndexed { index, item ->
                val value = if (isPercentage) item.completionPercent else item.totalAmount.toFloat()
                val drawValue = value.coerceAtMost(maxValue)

                val x = (stepX * index) + (stepX / 2)
                val y = h - ((drawValue / maxValue) * (h * 0.7f)) - 40f

                if (index == 0) path.moveTo(x, y)
                else {
                    // Membuat kurva halus
                    val prevX = (stepX * (index - 1)) + (stepX / 2)
                    val prevVal = if (isPercentage) data[index-1].completionPercent else data[index-1].totalAmount.toFloat()
                    val prevY = h - ((prevVal.coerceAtMost(maxValue) / maxValue) * (h * 0.7f)) - 40f

                    val controlX1 = prevX + (stepX / 2)
                    val controlY1 = prevY
                    val controlX2 = x - (stepX / 2)
                    val controlY2 = y

                    path.cubicTo(controlX1, controlY1, controlX2, controlY2, x, y)
                }

                // Draw point circle
                drawCircle(
                    color = activeColor,
                    radius = 4.dp.toPx(),
                    center = Offset(x, y)
                )
            }
            drawPath(
                path = path,
                color = pathColor,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // --- DRAW LABELS (X-AXIS) ---
        val textPaint = Paint().apply {
            color = android.graphics.Color.GRAY
            textSize = 12.sp.toPx()
            textAlign = Paint.Align.CENTER
        }

        data.forEachIndexed { index, item ->
            val x = (stepX * index) + (stepX / 2)
            val y = h - 5f
            drawContext.canvas.nativeCanvas.drawText(item.dayName, x, y, textPaint)
        }

        // --- DRAW BUBBLE TOOLTIP (Hanya di selected index) ---
        if (selectedIndex in data.indices) {
            val item = data[selectedIndex]
            val value = if (isPercentage) item.completionPercent else item.totalAmount.toFloat()
            val drawValue = value.coerceAtMost(maxValue)

            val x = (stepX * selectedIndex) + (stepX / 2)
            val yPoint = h - ((drawValue / maxValue) * (h * 0.7f)) - 40f

            // Text Tooltip
            val labelText = if (isPercentage) "${item.completionPercent.toInt()}%" else "${item.totalAmount} mL"

            // Setup Paint Tooltip
            val tooltipPaint = Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 12.sp.toPx()
                textAlign = Paint.Align.CENTER
                isFakeBoldText = true
            }

            val textBounds = android.graphics.Rect()
            tooltipPaint.getTextBounds(labelText, 0, labelText.length, textBounds)
            val textW = textBounds.width() + 40f
            val textH = textBounds.height() + 30f

            // Posisi Bubble (di atas point)
            val bubbleY = yPoint - 20f
            val bubbleRect = androidx.compose.ui.geometry.Rect(
                left = x - (textW / 2),
                top = bubbleY - textH,
                right = x + (textW / 2),
                bottom = bubbleY
            )

            val bubblePath = Path().apply {
                addRoundRect(androidx.compose.ui.geometry.RoundRect(bubbleRect, CornerRadius(16f, 16f)))
                // Segitiga kecil di bawah bubble
                moveTo(x - 10f, bubbleY)
                lineTo(x, bubbleY + 15f)
                lineTo(x + 10f, bubbleY)
                close()
            }

            // Draw Shadow & Shape
            drawPath(bubblePath, color = activeColor)

            // Draw Text inside
            drawContext.canvas.nativeCanvas.drawText(
                labelText,
                x,
                bubbleY - (textH/2) + (textBounds.height()/2),
                tooltipPaint
            )
        }
    }
}