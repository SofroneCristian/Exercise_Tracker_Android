package com.pdm.proiect_android_kotlin

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import com.pdm.proiect_android_kotlin.models.Exercise
import com.pdm.proiect_android_kotlin.service.FirebaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            val firebaseService = FirebaseService(context)
            val exercises = firebaseService.getTodayUserExercises()
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, exercises)
            }
        }
    }

    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            exercises: List<Exercise>?
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget)

            val progress = exercises?.sumOf { it.duration.toMinutes() } ?: 0
            val goal = 120

            views.setTextViewText(R.id.textViewProgress, "Progres: $progress minute")
            views.setProgressBar(R.id.progressBar, goal, progress, false)
            views.setTextViewText(R.id.textViewGoal, "Obiectiv: $goal minute")

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

private fun String.toMinutes(): Int {
    val parts = this.split(":")
    return if (parts.size == 3) {
        parts[0].toInt() * 60 + parts[1].toInt()
    } else {
        0
    }
}
