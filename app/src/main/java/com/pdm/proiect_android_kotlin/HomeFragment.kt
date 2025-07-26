package com.pdm.proiect_android_kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pdm.proiect_android_kotlin.service.FirebaseService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class HomeFragment : Fragment() {

    private lateinit var welcomeMessage: TextView
    private lateinit var todaysDate: TextView
    private lateinit var exercisesRecyclerView: RecyclerView
    private lateinit var noExercisesMessage: TextView
    private lateinit var exercisesAdapter: ExercisesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        welcomeMessage = view.findViewById(R.id.welcomeMessage)
        todaysDate = view.findViewById(R.id.todaysDate)
        exercisesRecyclerView = view.findViewById(R.id.exercisesRecyclerView)
        noExercisesMessage = view.findViewById(R.id.noExercisesMessage)

        exercisesAdapter = ExercisesAdapter(fromHomeFragment = true) { }

        exercisesRecyclerView.layoutManager = LinearLayoutManager(context)
        exercisesRecyclerView.adapter = exercisesAdapter


        val today = SimpleDateFormat("dd-MM-yyyy").format(Date())
        todaysDate.text = today

        fetchTodayExercises()

        return view
    }

    private fun fetchTodayExercises() {
        val firebaseService = FirebaseService(requireContext())
        lifecycleScope.launch {
            val exercises = firebaseService.getTodayUserExercises()
            if (exercises.isNotEmpty()) {
                noExercisesMessage.visibility = View.GONE
                exercisesRecyclerView.visibility = View.VISIBLE
                exercisesAdapter.submitList(exercises)
            } else {
                exercisesRecyclerView.visibility = View.GONE
                noExercisesMessage.visibility = View.VISIBLE
            }
        }
    }
}
