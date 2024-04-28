package nodomain.pacjo.wear.anotherapplist.tile

import android.content.ComponentName
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.glance.Button
import androidx.glance.ButtonColors
import androidx.glance.GlanceComposable
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.unit.ColorProvider
import androidx.glance.wear.tiles.GlanceTileService
import nodomain.pacjo.wear.anotherapplist.R
import nodomain.pacjo.wear.anotherapplist.presentation.MainActivity
import nodomain.pacjo.wear.anotherapplist.presentation.dataStore
import nodomain.pacjo.wear.anotherapplist.utils.AppInfo
import nodomain.pacjo.wear.anotherapplist.utils.getFavoriteApp

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

        // Get favorite apps
        val appKeys: List<String> = listOf(
            "favourite_app1",
            "favourite_app2",
            "favourite_app3",
            "favourite_app4",
            "favourite_app5"
        )
        val favoriteApps: MutableList<AppInfo> = mutableListOf()

        for (key in appKeys) {
            val appInfo = getFavoriteApp(packageManager, dataStore, key)

            if (appInfo != null) {
                favoriteApps.add(appInfo)
            }
        }

        // TODO: fix layout when 3 apps are selected

        Column(
            modifier = GlanceModifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (favoriteApps.isNotEmpty()) {
                Column {
                    Row (
                        modifier = GlanceModifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (app in favoriteApps.subList(0, 2)) {
                            ActionButton(app)
                        }
                    }

                    if (favoriteApps.size > 2) {
                        Row (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            for (app in favoriteApps.subList(2, favoriteApps.size)) {
                                ActionButton(app)
                            }
                        }
                    }
                }
            }

            Spacer(GlanceModifier.height(6.dp))

            Button(
                text = LocalContext.current.getString(R.string.open_app_list),
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

@Composable
fun ActionButton(app: AppInfo) {
    Image(
        provider = ImageProvider(app.activityIcon.toBitmap()),
        contentDescription = app.activityLabel,
        modifier = GlanceModifier
            .height(48.dp)
            .width(48.dp)
            .padding(6.dp)
            .clickable(actionStartActivity(ComponentName(app.packageName, app.activityName)))
    )
}