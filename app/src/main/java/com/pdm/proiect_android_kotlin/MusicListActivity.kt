package com.pdm.proiect_android_kotlin

import MusicPlayer
import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MusicListActivity : AppCompatActivity() {

    private lateinit var musicListAdapter: ArrayAdapter<String>
    private val musicList = mutableListOf<String>()
    private val musicUriList = mutableListOf<Uri>()
    private lateinit var seekBar: SeekBar
    private lateinit var previousButton: Button
    private lateinit var playPauseButton: Button
    private lateinit var nextButton: Button

    private var currentSongIndex: Int = -1
    private val handler = android.os.Handler()
    private val updateSeekBar = object : Runnable {
        override fun run() {
            if (MusicPlayer.isPlaying()) {
                val currentPosition = MusicPlayer.getCurrentPosition()
                seekBar.progress = currentPosition
            }
            handler.postDelayed(this, 1000) // update seekbar
        }
    }

    companion object {
        private const val REQUEST_READ_PERMISSION = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)

        val category = intent.getStringExtra("CATEGORY") ?: run {
            return
        }
        supportActionBar?.title = category

        musicListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, musicList)
        findViewById<ListView>(R.id.music_list_view).apply {
            adapter = musicListAdapter
            setOnItemClickListener { _, _, position, _ ->
                playMusic(position)
            }
        }

        seekBar = findViewById(R.id.seek_bar)
        previousButton = findViewById(R.id.previous_button)
        playPauseButton = findViewById(R.id.play_pause_button)
        nextButton = findViewById(R.id.next_button)

        previousButton.setOnClickListener { playPreviousMusic() }
        playPauseButton.setOnClickListener { playOrPauseMusic() }
        nextButton.setOnClickListener { playNextMusic() }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        checkPermissionsAndLoadMusic(category)
        handler.post(updateSeekBar)
    }

    private fun checkPermissionsAndLoadMusic(category: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_PERMISSION
            )
        } else {
            loadMusicByCategory(category)
        }
    }

    private fun loadMusicByCategory(category: String) {
        val genre = getGenreFromCategory(category)
        if (genre.isEmpty()) {
            return
        }

        val genreId = getGenreId(genre)
        if (genreId == -1L) {
            return
        }

        val uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId)

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("%/storage/1B03-2C18/Music/%")

        val cursor = contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val data = it.getString(dataColumn)
                val contentUri =
                    ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                musicList.add("$title - $artist")
                musicUriList.add(contentUri)
            }

            musicListAdapter.notifyDataSetChanged()
        }
    }

    private fun getGenreFromCategory(category: String): String {
        return when (category) {
            "Hip-Hop Music" -> "Hip-Hop Music"
            "Pop Music" -> "Pop Music"
            "Rock Music" -> "Rock Music"
            "Electronic Music" -> "Electronic Music"
            "Chill Music" -> "Chill Music"
            "RnB Music" -> "RnB Music"
            else -> ""
        }
    }


    private fun getGenreId(genre: String): Long {
        val uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Genres._ID)
        val selection = "${MediaStore.Audio.Genres.NAME}=?"
        val selectionArgs = arrayOf(genre)
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.use {
            val columnIndex = it.getColumnIndex(MediaStore.Audio.Genres._ID)
            if (columnIndex != -1 && it.moveToFirst()) {
                return it.getLong(columnIndex)
            }
        }
        return -1L
    }

    private fun playMusic(position: Int) {
        currentSongIndex = position
        MusicPlayer.play(this, musicUriList[position])
        seekBar.max = MusicPlayer.getDuration()
    }

    private fun playOrPauseMusic() {
        MusicPlayer.playOrPause()
        updateUI()
    }


    private fun playNextMusic() {
        currentSongIndex++
        if (currentSongIndex < musicList.size) {
            MusicPlayer.play(this, musicUriList[currentSongIndex])
        } else {
            currentSongIndex = 0
            MusicPlayer.play(this, musicUriList[currentSongIndex])
        }
        updateUI()
    }

    private fun playPreviousMusic() {
        currentSongIndex--
        if (currentSongIndex >= 0) {
            MusicPlayer.play(this, musicUriList[currentSongIndex])
        } else {
            currentSongIndex = musicList.size - 1
            MusicPlayer.play(this, musicUriList[currentSongIndex])
        }
        updateUI()
    }

    private fun updateUI() {
        val isPlaying = MusicPlayer.isPlaying()
        playPauseButton.text = if (isPlaying) "Pause" else "Play"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val category = intent.getStringExtra("CATEGORY") ?: return
            loadMusicByCategory(category)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateSeekBar)
    }

}


