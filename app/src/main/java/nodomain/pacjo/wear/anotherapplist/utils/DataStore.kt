package nodomain.pacjo.wear.anotherapplist.utils

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun saveBoolToDataStore(dataStore: DataStore<Preferences>, key: String, value: Boolean?) {
    CoroutineScope(Dispatchers.IO).launch {
        dataStore.edit { settings ->
            if (value == null) {
                settings.remove(booleanPreferencesKey(key))
            } else {
                settings[booleanPreferencesKey(key)] = value
            }
        }
    }
}

fun getBoolToDataStore(dataStore: DataStore<Preferences>, key: String): Boolean? {
    return runBlocking {
        dataStore.data.first()[booleanPreferencesKey(key)]
    }
}

fun saveFavoriteApp(dataStore: DataStore<Preferences>, key: String, app: AppInfo?) {
    CoroutineScope(Dispatchers.IO).launch {
        dataStore.edit { settings ->
            if (app == null) {
                settings.remove(stringPreferencesKey(key))
            } else {
                settings[stringPreferencesKey(key)] = app.activityName
            }
        }
    }
}

fun getFavoriteApp(packageManager: PackageManager, dataStore: DataStore<Preferences>, key: String): AppInfo? {
    var activityName: String?
    runBlocking {
        activityName = dataStore.data.first()[stringPreferencesKey(key)]
    }

    return activityName?.let { getAppInfoFromActivityName(packageManager, it) }
}
fun findResolveInfoByActivityName(packageManager: PackageManager, activityName: String): ResolveInfo? {
    val mainIntent = Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

    val resolveInfoList: List<ResolveInfo> = packageManager.queryIntentActivities(mainIntent, 0)
    return resolveInfoList.find { it.activityInfo.name == activityName }
}

fun resolveInfoToAppInfo(resolveInfo: ResolveInfo, packageManager: PackageManager): AppInfo {
    val packageName = resolveInfo.activityInfo.packageName
    val activityName = resolveInfo.activityInfo.name
    val activityLabel = resolveInfo.loadLabel(packageManager).toString()
    val activityIcon = resolveInfo.loadIcon(packageManager)

    return AppInfo(
        packageName = packageName,
        activityName = activityName,
        activityLabel = activityLabel,
        activityIcon = activityIcon
    )
}

fun getAppInfoFromActivityName(packageManager: PackageManager, activityName: String): AppInfo? {
    val resolveInfo = findResolveInfoByActivityName(packageManager, activityName)

    return resolveInfo?.let { resolveInfoToAppInfo(it, packageManager) }
}