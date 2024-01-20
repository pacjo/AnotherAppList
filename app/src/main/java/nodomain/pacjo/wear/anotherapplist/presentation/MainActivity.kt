package nodomain.pacjo.wear.anotherapplist.presentation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.glance.Button
import androidx.glance.action.Action
import androidx.glance.action.actionStartActivity
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
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

            MaterialTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    verticalArrangement = Arrangement.Center
                ) {
//                Text(
//                    text = "Installed apps",
//                    style = MaterialTheme.typography.body1,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.fillMaxWidth()
//                )

                    LazyColumn {
                        items(apps.size) { index ->
                            AppEntry(apps[index], context)
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
                },
                onLongClick = {
                    saveFavoriteApp(app)
                    Toast.makeText(context, "marked as favorite",Toast.LENGTH_SHORT).show()
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