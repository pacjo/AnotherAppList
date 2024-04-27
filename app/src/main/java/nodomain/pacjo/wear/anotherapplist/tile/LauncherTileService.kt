package nodomain.pacjo.wear.anotherapplist.tile

import android.content.ComponentName
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.ButtonColors
import androidx.glance.GlanceComposable
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.unit.ColorProvider
import androidx.glance.wear.tiles.GlanceTileService
import nodomain.pacjo.wear.anotherapplist.presentation.MainActivity

class LauncherTileService : GlanceTileService() {

    @Composable
    @GlanceComposable
    override fun Content() {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL

        val clickAction = when {
            manufacturer.equals("OPPO", ignoreCase = true) && model.contains("watch", ignoreCase = true) ->
                actionStartActivity(
                    ComponentName("com.heytap.wearable.launcher", "com.heytap.wearable.launcher.Launcher")
                )
            else -> actionStartActivity(MainActivity::class.java)       // default to custom implementation
        }

        Column(
            modifier = GlanceModifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                text = "Open\napp list",
                onClick = clickAction,
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