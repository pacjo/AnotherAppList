package nodomain.pacjo.wear.anotherapplist.presentation

import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.google.android.horologist.compose.material.ToggleChip
import com.google.android.horologist.compose.material.ToggleChipToggleControl
import nodomain.pacjo.wear.anotherapplist.R
import nodomain.pacjo.wear.anotherapplist.tile.LauncherTileService
import nodomain.pacjo.wear.anotherapplist.utils.AppInfo
import nodomain.pacjo.wear.anotherapplist.utils.getBoolToDataStore
import nodomain.pacjo.wear.anotherapplist.utils.getFavoriteApp
import nodomain.pacjo.wear.anotherapplist.utils.getLaunchableApps
import nodomain.pacjo.wear.anotherapplist.utils.saveBoolToDataStore
import nodomain.pacjo.wear.anotherapplist.utils.saveFavoriteApp

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "configuration")

class SettingsActivity : ComponentActivity() {
    companion object {
        // here we store the key under which we'd like to save the selected app
        var currentAppKey: String? = null
    }

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
                        DynamicWearScaffold(
                            context = context,
                            roundContent = {
                                items(appKeys) { key ->
                                    AppChip(key, appKeys, packageManager, context.dataStore, context, navController)
                                }
                            },
                            rectangularContent = {
                                items(appKeys) { key ->
                                    AppChip(key, appKeys, packageManager, context.dataStore, context, navController)
                                }
                                item {
                                    SimpleToggleChip(
                                        text = stringResource(R.string.use_custom_list),
                                        value = getBoolToDataStore(dataStore, "force_custom_list") ?: false
                                    ) {
                                        saveBoolToDataStore(dataStore, "force_custom_list", it)
                                        LauncherTileService.forceTileUpdate(context)
                                    }
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
                                SimpleActionChip(context.getString(R.string.remove_selection)) {
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
                                SimpleActionChip(context.getString(R.string.remove_selection)) {
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
            text = stringResource(R.string.info_screen_text),
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
fun SimpleActionChip(text: String, onClick: () -> Unit) {
    Chip(
        label = text,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun SimpleToggleChip(text: String, value: Boolean, onClick: (Boolean) -> Unit) {
    var checked by remember { mutableStateOf(value) }

    ToggleChip(
        label = text,
        checked = checked,
        toggleControl = ToggleChipToggleControl.Switch,
        onCheckedChanged = {
            checked = !checked
            onClick(checked)
        },
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

fun selectionChangeHelper(context: Context, navController: NavController, data: AppInfo?) {
    // save new selection to dataStore
    saveFavoriteApp(context.dataStore, SettingsActivity.currentAppKey!!, data)

    // reset key and go back
    SettingsActivity.currentAppKey = null
    navController.popBackStack()

    // update tile state
    // stolen from: https://github.com/yschimke/rememberwear/blob/9071a47b9b8b753b457fe02a208efbbddf2d9849/wear/src/main/kotlin/com/google/wear/soyted/tile/RememberWearTileProviderService.kt#L93
    LauncherTileService.forceTileUpdate(context)
}

// courtesy of Phind-34B, since ChatGPT and Gemini were both useless
@OptIn(ExperimentalHorologistApi::class)
@Composable
fun AppChip(
    key: String,
    appKeys: List<String>,
    packageManager: PackageManager,
    dataStore: DataStore<Preferences>,
    context: Context,
    navController: NavController
) {
    val app = getFavoriteApp(packageManager, dataStore, key)

    Chip(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
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
            SettingsActivity.currentAppKey = key
            navController.navigate("info_screen")
        }
    )
}