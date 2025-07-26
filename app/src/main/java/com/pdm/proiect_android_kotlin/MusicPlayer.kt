// MusicPlayer.kt (Singleton Class)
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object MusicPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var currentSongIndex: Int = -1
    private var musicList: MutableList<Uri>? = null

    fun play(context: Context, musicUri: Uri) {
        stop()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, musicUri)
            prepare()
            start()
        }
    }

    fun play(context: Context, musicList: MutableList<Uri>, index: Int) {
        stop()
        this.musicList = musicList
        currentSongIndex = index
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, musicList[index])
            prepare()
            start()
        }
    }

    fun playNext() {
        if (mediaPlayer != null && currentSongIndex < musicList!!.size - 1) {
            currentSongIndex++
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(musicList!![currentSongIndex].toString())
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
        }
    }

    fun playPrevious() {
        if (mediaPlayer != null && currentSongIndex > 0) {
            currentSongIndex--
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(musicList!![currentSongIndex].toString())
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
        }
    }

    fun playOrPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.pause()
            } else {
                mediaPlayer!!.start()
            }
        }
    }
    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }
    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun pause() {
        mediaPlayer?.pause()
    }
    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }


    fun stop() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
    }

}

