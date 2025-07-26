package com.pdm.proiect_android_kotlin


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class MusicCategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set onClickListeners for each category button
        view.findViewById<Button>(R.id.button_chill_music).setOnClickListener {
            openMusicList("Chill Music")
        }
        view.findViewById<Button>(R.id.button_electronic_music).setOnClickListener {
            openMusicList("Electronic Music")
        }
        view.findViewById<Button>(R.id.button_rnb_music).setOnClickListener {
            openMusicList("RnB Music")
        }
        view.findViewById<Button>(R.id.button_rock_music).setOnClickListener {
            openMusicList("Rock Music")
        }
        view.findViewById<Button>(R.id.button_pop_music).setOnClickListener {
            openMusicList("Pop Music")
        }
        view.findViewById<Button>(R.id.button_rap_music).setOnClickListener {
            openMusicList("Hip-Hop Music")
        }
    }

    private fun openMusicList(category: String) {
        val intent = Intent(requireContext(), MusicListActivity::class.java).apply {
            putExtra("CATEGORY", category)
        }
        startActivity(intent)
    }
}
