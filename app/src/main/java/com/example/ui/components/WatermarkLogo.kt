package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.MucoindoBlue
import com.example.ui.theme.MucoindoOrange

const val MUCOINDO_LOGO_URL =
  "https://lh3.googleusercontent.com/aida-public/AB6AXuDSC6lNnzQLQFwUtucq_1b1VfGzL10vou0uU7j8pyQ6b6eUS6ls2KcoRTt5XZnGbumKW1c_oC7LS83v2qMSidlw7PqqxyFk3aS4jFiBSb4RcCe1q-CeZeahzdAvJUSB1eyJGu79UsfkGEQH6n6EWE0RxW-r3MH6AAvYlnZr2uxwG2J4_j6UoolSyeW5MVcGEdAqa8t8vtPPd5Eh9iUarbVRjpkxfZ9-ceJXMrMdfqS7oJeyfcqSDzRzrFAaPHJKL9KhWQ"

const val MUCOINDO_HEADER_LOGO_URL =
  "https://lh3.googleusercontent.com/aida-public/AB6AXuBM7otczNGF7HbNvL6SWiXtGNDvszzVv7w0p6WwQRBtbqdsFsFVpGQbcfT5LMT_n6DMKbWpZ6_gPaVjOpyGpKV9aMS2vRBHS74MLIu_FpIChxtU-hu1o09eACblN5REB_U1QUZPrLPScc3JvfO-BM__rk2bLCmkzg6zPbNGhVmwOFvSvdPrqsqj-WsonqQFBTeq-TtprctjWJPpRHFtHMRjvUtu_63_yGuC89F8ECdzvUjrAM5-OVy49ZFQsfx2kU9FGg"

@Composable
fun MucoindoLogo(
  modifier: Modifier = Modifier,
  tintColor: Color? = null
) {
  SubcomposeAsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(MUCOINDO_LOGO_URL)
      .crossfade(true)
      .build(),
    contentDescription = "Logo PT Mucoindo Prakasa",
    modifier = modifier,
    contentScale = ContentScale.Fit,
    loading = {
      MucoindoVectorLogo(
        modifier = Modifier.matchParentSize(),
        customBlue = tintColor ?: MucoindoBlue,
        customOrange = tintColor ?: MucoindoOrange
      )
    },
    error = {
      MucoindoVectorLogo(
        modifier = Modifier.matchParentSize(),
        customBlue = tintColor ?: MucoindoBlue,
        customOrange = tintColor ?: MucoindoOrange
      )
    }
  )
}

@Composable
fun MucoindoVectorLogo(
  modifier: Modifier = Modifier,
  customBlue: Color = MucoindoBlue,
  customOrange: Color = MucoindoOrange
) {
  Canvas(modifier = modifier) {
    drawMucoindoBrand(size.width, size.height, customBlue, customOrange)
  }
}

private fun DrawScope.drawMucoindoBrand(
  w: Float,
  h: Float,
  blueColor: Color,
  orangeColor: Color
) {
  // Geometric stylized "M" with right orange square accurately modeled from Image 10.png
  // Left slanted block
  val leftPath = Path().apply {
    moveTo(0f, h * 0.55f)
    lineTo(w * 0.22f, h * 0.15f)
    lineTo(w * 0.42f, h * 0.15f)
    lineTo(w * 0.20f, h * 0.90f)
    lineTo(0f, h * 0.90f)
    close()
  }
  drawPath(leftPath, blueColor)

  // Center/Right chevron shape of M
  val centerRightPath = Path().apply {
    moveTo(w * 0.23f, h * 0.90f)
    lineTo(w * 0.44f, h * 0.50f)
    lineTo(w * 0.62f, h * 0.15f)
    lineTo(w * 0.77f, h * 0.15f)
    lineTo(w * 0.77f, h * 0.50f)
    lineTo(w * 0.42f, h * 0.90f)
    close()
  }
  drawPath(centerRightPath, blueColor)

  // Orange accent block in bottom right quadrant
  val sqLeft = w * 0.72f
  val sqTop = h * 0.50f
  val sqSize = w * 0.24f
  val orangePath = Path().apply {
    moveTo(sqLeft, sqTop)
    lineTo(sqLeft + sqSize, sqTop)
    lineTo(sqLeft + sqSize, sqTop + sqSize * 0.7f)
    lineTo(sqLeft + sqSize * 0.7f, sqTop + sqSize)
    lineTo(sqLeft, sqTop + sqSize)
    close()
  }
  drawPath(orangePath, orangeColor)
}

@Composable
fun WatermarkOverlay(
  scale: Float,
  opacity: Float,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .size(240.dp)
        .graphicsLayer(
          scaleX = scale,
          scaleY = scale,
          alpha = opacity
        )
    ) {
      MucoindoLogo(
        modifier = Modifier.matchParentSize(),
        tintColor = Color.DarkGray
      )
    }
  }
}
