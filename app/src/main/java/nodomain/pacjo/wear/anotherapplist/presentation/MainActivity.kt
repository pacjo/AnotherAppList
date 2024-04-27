package nodomain.pacjo.wear.anotherapplist.presentation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
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
import nodomain.pacjo.wear.anotherapplist.utils.saveFavoriteApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = this as Context
            val apps = getLaunchableApps(context.packageManager)

            val isRoundScreen = context.resources.configuration.isScreenRound

            val listState =
                if (isRoundScreen)
                    rememberScalingLazyListState()
                else
                    rememberLazyListState()

            MaterialTheme {
                Scaffold(
                    timeText = {
                        TimeText(
                            modifier =
                                if (isRoundScreen)
                                    Modifier.scrollAway(listState as ScalingLazyListState)
                                else
                                    Modifier.scrollAway(listState as LazyListState),
                        )
                    },
                    vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
                    positionIndicator = {
                        if (isRoundScreen) {
                            PositionIndicator(scalingLazyListState = rememberScalingLazyListState())
                        } else {
                            PositionIndicator(lazyListState = rememberLazyListState())
                        }
                    }
                ) {
                    if (isRoundScreen) {
                        ScalingLazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState as ScalingLazyListState,
                            contentPadding = PaddingValues(0.dp),
                            autoCentering = AutoCenteringParams(0, 1)
                        ) {
                            items(apps) { app ->
                                AppEntry(app, context)
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState as LazyListState,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            items(apps) { app ->
                                AppEntry(app, context)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppEntry(app: AppInfo, context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    val intent = Intent().apply {
                        component = ComponentName(app.packageName, app.activityName)
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }
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