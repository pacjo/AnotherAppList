package nodomain.pacjo.wear.anotherapplist.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.fillMaxRectangle
import com.google.android.horologist.compose.material.Chip
import nodomain.pacjo.wear.anotherapplist.R
import nodomain.pacjo.wear.anotherapplist.utils.AppInfo
import nodomain.pacjo.wear.anotherapplist.utils.getFavoriteApp
import nodomain.pacjo.wear.anotherapplist.utils.getLaunchableApps
import nodomain.pacjo.wear.anotherapplist.utils.saveFavoriteApp

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favourite_apps")

class SettingsActivity : ComponentActivity() {
    companion object {
        // here we store the key under which we'd like to save the selected app
        var currentAppKey: String? = null
    }

    @OptIn(ExperimentalHorologistApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = this as Context

            val navController = rememberSwipeDismissableNavController()

            val appKeys: List<String> = listOf(
                "favourite_app1",
                "favourite_app2",
                "favourite_app3",
                "favourite_app4",
                "favourite_app5"
            )

            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "settings_main"
            ) {
                composable("settings_main") {
                    MaterialTheme {
                        // TODO: we really need to fix this duplication, it hurts
                        // also TODO: allow forcing custom list implementation, since we removed that tile
                        DynamicWearScaffold(
                            context = context,
                            roundContent = {
                                items(appKeys) { key ->
                                    val app = getFavoriteApp(packageManager, context.dataStore, key)

                                    Chip(
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        ),
                                        label = "App ${appKeys.indexOf(key) + 1}",
                                        secondaryLabel = app?.activityLabel ?: context.getString(R.string.tap_to_select),
                                        icon = {
                                            if (app != null) {
                                                Image(
                                                    painter = rememberDrawablePainter(app.activityIcon),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(48.dp)
                                                )
                                            }
                                        },
                                        onClick = {
                                            currentAppKey = key
                                            navController.navigate("info_screen")
                                        }
                                    )
                                }
                            },
                            rectangularContent = {
                                items(appKeys) { key ->
                                    val app = getFavoriteApp(packageManager, context.dataStore, key)

                                    Chip(
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        ),
                                        label = "App ${appKeys.indexOf(key) + 1}",
                                        secondaryLabel = app?.activityLabel ?: context.getString(R.string.tap_to_select),
                                        icon = {
                                            if (app != null) {
                                                Image(
                                                    painter = rememberDrawablePainter(app.activityIcon),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(48.dp)
                                                )
                                            }
                                        },
                                        onClick = {
                                            currentAppKey = key
                                            navController.navigate("info_screen")
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
                composable("info_screen") {
                    InfoScreen {
                        navController.navigate("app_select") {
                            // don't show this info screen again when going back
                            popUpTo("info_screen") {
                                inclusive = true
                            }
                        }
                    }
                }
                composable("app_select") {
                    val apps = getLaunchableApps(context.packageManager)

                    DynamicWearScaffold(
                        context = context,
                        roundContent = {
                            // remove selection button
                            item {
                                SpecialActionChip(context.getString(R.string.remove_selection)) {
                                    selectionChangeHelper(context, navController, null)
                                }
                            }
                            items(apps) { app ->
                                AppEntry(app, context) {
                                    selectionChangeHelper(context, navController, it)
                                }
                            }
                        },
                        rectangularContent = {
                            item {
                                SpecialActionChip(context.getString(R.string.remove_selection)) {
                                    selectionChangeHelper(context, navController, null)
                                }
                            }
                            items(apps) { app ->
                                AppEntry(app, context) {
                                    selectionChangeHelper(context, navController, it)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InfoScreen(onClick: () -> Unit) {
    Column (
        modifier = Modifier.fillMaxRectangle(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = R.string.info_screen_text.toString(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { onClick() }
        ) {
            Text("Ok")
        }
    }
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun SpecialActionChip(text: String, onClick: () -> Unit) {
    Chip(
        label = text,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

fun selectionChangeHelper(context: Context, navController: NavController, data: AppInfo?) {
    // save new selection to dataStore
    saveFavoriteApp(context.dataStore, SettingsActivity.currentAppKey!!, data)

    // reset key and go back
    SettingsActivity.currentAppKey = null
    navController.popBackStack()

    // dispatch job to update tile
    // TODO: fix tile updates after action
//    CoroutineScope(Dispatchers.IO).launch {
//        LauncherTileService().updateTileState {}
//    }
}