package nodomain.pacjo.wear.anotherapplist.presentation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListScope
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import nodomain.pacjo.wear.anotherapplist.utils.AppInfo
import nodomain.pacjo.wear.anotherapplist.utils.getLaunchableApps

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = this as Context

            val apps = getLaunchableApps(context.packageManager)

            MaterialTheme {
                // there has to be a way to remove duplication here
                DynamicWearScaffold(
                    context = context,
                    roundContent = {
                        items(apps) { app ->
                            AppEntry(app, context)
                        }
                    },
                    rectangularContent = {
                        items(apps) { app ->
                            AppEntry(app, context)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DynamicWearScaffold(
    context: Context,
    roundContent: ScalingLazyListScope.() -> Unit,
    rectangularContent: LazyListScope.() -> Unit
) {
    val isRoundScreen = context.resources.configuration.isScreenRound

    if (isRoundScreen) {
        RoundListScaffold(roundContent)
    } else {
        RectangularListScaffold(rectangularContent)
    }
}

@Composable
fun RectangularListScaffold(content: LazyListScope.() -> Unit) {
    val listState = rememberLazyListState()

    // TODO: fix broken timeText

    Scaffold(
        timeText = { TimeText(modifier = Modifier.scrollAway(listState)) },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(listState) }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(0.dp)
        ) {
            content()
        }
    }
}

@Composable
fun RoundListScaffold(content: ScalingLazyListScope.() -> Unit) {
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText(modifier = Modifier.scrollAway(listState)) },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(listState) }
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(0.dp),
            autoCentering = AutoCenteringParams(0, 1)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppEntry(
    app: AppInfo,
    context: Context,
    longClickCallback: (AppInfo) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    val intent = Intent().apply {
                        component = ComponentName(app.packageName, app.activityName)
                    }
                    context.startActivity(intent)
                },
                onLongClick = { longClickCallback(app) }
            )
    ) {
        // Display app icon if available
        Image(
            painter = rememberDrawablePainter(app.activityIcon),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .padding(
                    start = 8.dp,
                    top = 4.dp,
                    end = 4.dp,
                    bottom = 4.dp
                )
                .align(Alignment.CenterVertically)
        )

        // Display app friendly name
        Text(
            text = app.activityLabel,
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                ),
            style = MaterialTheme.typography.body1
        )
    }
}