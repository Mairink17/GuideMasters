package br.com.fiap.db1.guidemasters.funcionalidades


import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.db1.guidemasters.R
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
@Composable
fun AnimacaoMatch(
    onMatchAnimationComplete: () -> Unit
) {
    var isAnimating by remember { mutableStateOf(true) }

    val density = LocalDensity.current.density
    val animDistance = (200.dp * density).value
    val animDuration = 500

    val transition = updateTransition(isAnimating, label = "logoGuideMasters")
    val offset by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = animDuration)
        }, label = ""
    ) {
        if (it) animDistance else 0f
    }

    val rotation by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = animDuration)
        }, label = ""
    ) {
        if (it) 360f else 0f
    }

    val centerTransition = updateTransition(isAnimating, label = "centerTransition")
    val centerX by centerTransition.animateFloat(
        transitionSpec = {
            tween(durationMillis = animDuration)
        }, label = ""
    ) {
        if (it) 0f else 0f
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.offset { IntOffset(centerX.roundToInt(), 0) }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_school_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .offset { IntOffset(offset.roundToInt(), 0) }
                        .graphicsLayer(rotationY = rotation)
                )

                Image(
                    painter = painterResource(id = R.drawable.baseline_school_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .offset { IntOffset(-offset.roundToInt(), 0) }
                        .graphicsLayer(rotationY = rotation)
                )
            }

            if (!isAnimating) {
                Text(stringResource(id = R.string.novo_match), fontSize = 24.sp)
            }
        }

        LaunchedEffect(Unit) {
            delay(animDuration.toLong())
            isAnimating = false
            onMatchAnimationComplete()
        }

    }
}
