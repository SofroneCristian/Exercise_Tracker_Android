package com.pdm.proiect_android_kotlin

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pdm.proiect_android_kotlin.models.Exercise
import com.pdm.proiect_android_kotlin.service.ExerciseService
import com.pdm.proiect_android_kotlin.service.FirebaseService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddExercisesFragment : Fragment() {

    private lateinit var searchExercises: EditText
    private lateinit var searchButton: Button
    private lateinit var exerciseDuration: TimePicker
    private lateinit var saveButton: Button
    private var selectedExercise: Exercise? = null

    private lateinit var exerciseService: ExerciseService
    private lateinit var firebaseService: FirebaseService
    private lateinit var exercisesAdapter: ExercisesAdapter
    private lateinit var exercisesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_exercises, container, false)
        initViews(view)
        configTimePicker()
        setupRecyclerView(view)
        setupListeners()

        return view
    }

    private fun setupRecyclerView(view: View) {
        exercisesRecyclerView = view.findViewById(R.id.exercisesRecyclerView)
        exercisesAdapter = ExercisesAdapter(fromHomeFragment = false) { exercise -> onExerciseSelected(exercise) }
        exercisesRecyclerView.layoutManager = LinearLayoutManager(context)
        exercisesRecyclerView.adapter = exercisesAdapter
    }

    private fun configTimePicker() {
        exerciseDuration.setIs24HourView(true)
        exerciseDuration.hour = 0
        exerciseDuration.minute = 30
    }

    private fun initViews(view: View) {
        searchExercises = view.findViewById(R.id.searchExercises)
        searchButton = view.findViewById(R.id.searchButton)
        exerciseDuration = view.findViewById(R.id.exerciseDuration)
        saveButton = view.findViewById(R.id.saveButton)
        exerciseService = ExerciseService(requireContext())
        firebaseService = FirebaseService(requireContext())
    }

    private fun setupListeners() {
        searchButton.setOnClickListener { onSearchClicked() }
        saveButton.setOnClickListener { onSaveClicked() }
    }

    private fun onSearchClicked() {
        val query = searchExercises.text.toString()
        lifecycleScope.launch {
            val exercises = exerciseService.getExercisesByName(query)
            exercises?.let {
                exercisesAdapter.submitList(it)
            }
        }
        searchExercises.setText("")
    }

    private fun onExerciseSelected(exercise: Exercise) {
        selectedExercise = exercise
    }

    private fun onSaveClicked() {
        val exercise = selectedExercise ?: return

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        exercise.date = dateFormat.format(Date())

        val hours = exerciseDuration.hour
        val minutes = exerciseDuration.minute
        val seconds = 0

        exercise.duration = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        lifecycleScope.launch {
            val success = firebaseService.addExercise(exercise)
            if (success) {
                Toast.makeText(context, "Exercise saved successfully", Toast.LENGTH_SHORT).show()
                updateWidgetData()
            } else {
                Toast.makeText(context, "Failed to save exercise", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateWidgetData() {
        lifecycleScope.launch {
            val exercises = firebaseService.getTodayUserExercises()
            val appWidgetManager = AppWidgetManager.getInstance(requireContext())
            val widgetComponentName = ComponentName(requireContext(), ExerciseWidgetProvider::class.java)
            val widgetIds = appWidgetManager.getAppWidgetIds(widgetComponentName)

            for (appWidgetId in widgetIds) {
                ExerciseWidgetProvider.updateAppWidget(requireContext(), appWidgetManager, appWidgetId, exercises)
            }
        }
    }
}
