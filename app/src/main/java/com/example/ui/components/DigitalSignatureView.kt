package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperBorder

@Composable
fun DigitalSignatureBox(
  isReceiver: Boolean,
  timestampOrId: String,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(64.dp)
      .background(PaperBackground, RoundedCornerShape(8.dp))
      .border(
        width = 1.dp,
        color = PaperBorder,
        shape = RoundedCornerShape(8.dp)
      ),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height
      if (!isReceiver) {
        // Driver signature (Blue undulating curve)
        val sigPath = Path().apply {
          moveTo(w * 0.10f, h * 0.70f)
          quadraticBezierTo(w * 0.25f, h * 0.20f, w * 0.40f, h * 0.50f)
          quadraticBezierTo(w * 0.55f, h * 0.80f, w * 0.70f, h * 0.35f)
          quadraticBezierTo(w * 0.85f, h * 0.85f, w * 0.95f, h * 0.30f)
        }
        drawPath(
          path = sigPath,
          color = Color(0xFF2563EB),
          style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        // Underline flourish
        val underPath = Path().apply {
          moveTo(w * 0.30f, h * 0.80f)
          quadraticBezierTo(w * 0.50f, h * 0.90f, w * 0.75f, h * 0.65f)
        }
        drawPath(
          path = underPath,
          color = Color(0xFF2563EB),
          style = Stroke(width = 2.5f, cap = StrokeCap.Round)
        )
      } else {
        // Receiver signature (Green loop signature + official circular stamp)
        val sigPath = Path().apply {
          moveTo(w * 0.12f, h * 0.55f)
          quadraticBezierTo(w * 0.28f, h * 0.90f, w * 0.45f, h * 0.30f)
          quadraticBezierTo(w * 0.68f, h * 0.75f, w * 0.82f, h * 0.40f)
          quadraticBezierTo(w * 0.92f, h * 0.70f, w * 0.96f, h * 0.50f)
        }
        drawPath(
          path = sigPath,
          color = Color(0xFF059669),
          style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        // Dashed approval circle stamp
        drawCircle(
          color = Color(0xFF10B981),
          radius = 28f,
          center = Offset(w * 0.56f, h * 0.48f),
          style = Stroke(
            width = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
          )
        )
      }
    }

    if (!isReceiver) {
      Text(
        text = timestampOrId,
        color = Color(0xFF94A3B8),
        fontSize = 9.sp,
        fontFamily = FontFamily.Monospace,
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(end = 6.dp, bottom = 4.dp)
      )
    } else {
      Box(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(bottom = 4.dp)
          .background(Color(0xFFD1FAE5), RoundedCornerShape(4.dp))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = timestampOrId,
          color = Color(0xFF059669),
          fontSize = 9.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}
