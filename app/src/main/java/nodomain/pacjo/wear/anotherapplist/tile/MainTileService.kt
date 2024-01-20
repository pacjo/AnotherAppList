package nodomain.pacjo.wear.anotherapplist.tile

import android.content.ComponentName
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.ButtonColors
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
import androidx.glance.wear.tiles.GlanceTileService
import androidx.wear.compose.material.Icon
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import nodomain.pacjo.wear.anotherapplist.presentation.MainActivity
import nodomain.pacjo.wear.anotherapplist.utils.AppInfo
import nodomain.pacjo.wear.anotherapplist.utils.getLaunchableApps

class ShortcutsTileService : GlanceTileService() {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        Column(
            modifier = GlanceModifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Row {
//                Button(text = "app1", actionStartActivity(MainActivity::class.java))
//                Spacer(GlanceModifier.width(4.dp))
//                Button(text = "app2", actionStartActivity(MainActivity::class.java))
//                Spacer(GlanceModifier.width(4.dp))
//                Button(text = "app3", actionStartActivity(MainActivity::class.java))
//            }
            Spacer(GlanceModifier.height(10.dp))
            Button(
                text = "Open\napp list",
                onClick = actionStartActivity(MainActivity::class.java),
                modifier = GlanceModifier
                    .height(64.dp)
                    .fillMaxWidth(),
                colors = ButtonColors(
                    contentColor = ColorProvider(Color.White),
                    backgroundColor = ColorProvider(Color.Black)
                )
            )
        }
    }
}

@Composable
fun AppShortcut(app: AppInfo) {
    Box(
        modifier = GlanceModifier
            .clickable(actionStartActivity(ComponentName(app.packageName, app.activityName))),
        contentAlignment = Alignment.Center
    ) {
        Text(app.activityLabel)
//        Icon(
//            painter = rememberDrawablePainter(app.activityIcon),
//            contentDescription = null,       // TODO: maybe add label for accessibility
//        )
    }
}