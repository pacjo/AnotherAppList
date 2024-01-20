package nodomain.pacjo.wear.anotherapplist.utils

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val activityName: String,
    val activityLabel: String,
    val activityIcon: Drawable
)

fun getLaunchableApps(packageManager: PackageManager): List<AppInfo> {
    val launchableApps = mutableListOf<AppInfo>()

    // Create an Intent with MAIN action and LAUNCHER category
    val mainIntent = Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

    // Get the list of ResolveInfo for activities with the MAIN action and LAUNCHER category
    val resolveInfoList: List<ResolveInfo> = packageManager.queryIntentActivities(mainIntent, 0)

    // Loop through the ResolveInfo list and add the package name, activity, label, and icon to the launchableApps list
    for (resolveInfo in resolveInfoList) {
        val packageName = resolveInfo.activityInfo.packageName
        val activityName = resolveInfo.activityInfo.name
        val activityLabel = resolveInfo.loadLabel(packageManager).toString()
        val activityIcon = resolveInfo.loadIcon(packageManager)

        // Remove our package from the list
        if (packageName != "nodomain.pacjo.wear.anotherapplist") {
            launchableApps.add(
                AppInfo(
                    packageName,
                    activityName,
                    activityLabel,
                    activityIcon
                )
            )
        }
    }

    // Sort the list alphabetically by activity label
    launchableApps.sortBy { it.activityLabel }

    return launchableApps
}

fun saveFavoriteApp(app: AppInfo) {
    return
}