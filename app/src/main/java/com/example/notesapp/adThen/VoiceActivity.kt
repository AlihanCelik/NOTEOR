package com.example.notesapp.adThen

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.*
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.notesapp.R
import kotlinx.android.synthetic.main.activity_voice.*
import java.io.File
import java.io.IOException

class VoiceActivity : AppCompatActivity(), View.OnClickListener {

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var fileName: String? = null
    private var lastProgress = 0
    private val mHandler = Handler()
    private val RECORD_AUDIO_REQUEST_CODE = 101
    private val MANAGE_EXTERNAL_STORAGE_REQUEST_CODE = 2001
    private var isPlaying = false
    private var permissionList=
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q) {
            arrayListOf(android.Manifest.permission.RECORD_AUDIO,android.Manifest.permission.READ_MEDIA_AUDIO)
        }else{
            arrayListOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.RECORD_AUDIO,android.Manifest.permission.READ_MEDIA_AUDIO)
        }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.imgBtRecord -> {
                prepareRecording()
            }

            R.id.imgBtStop -> {
                prepareStop()
                stopRecording()
            }

            R.id.imgViewPlay -> {
                if (!isPlaying && fileName != null) {
                    isPlaying = true
                    startPlaying()
                } else {
                    isPlaying = false
                    stopPlaying()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissionToRecordAudio()
        }
        requestManageExternalStoragePermission()
        backButton.setOnClickListener {
            finish()
        }

        imgBtRecord.setOnClickListener(this)
        imgBtStop.setOnClickListener(this)
        imgViewPlay.setOnClickListener(this)
    }

    private fun prepareStop() {
        TransitionManager.beginDelayedTransition(llRecorder)
        imgBtRecord.visibility = View.VISIBLE
        imgBtStop.visibility = View.GONE
        llPlay.visibility = View.VISIBLE
    }

    private fun prepareRecording() {
        TransitionManager.beginDelayedTransition(llRecorder)
        imgBtRecord.visibility = View.GONE
        imgBtStop.visibility = View.VISIBLE
        llPlay.visibility = View.GONE

        Handler().postDelayed({
            startRecording()
        }, 1000)  // 1 saniye gecikme (gerektiğinde ayarla)
    }

    private fun stopPlaying() {
        try {
            mPlayer?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mPlayer = null
        // oynatma butonunu göster
        imgViewPlay.setImageResource(R.drawable.ic_play_circle)
        chronometer.stop()
    }

    private fun startRecording() {
        mRecorder = MediaRecorder()
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

        // Harici depolama durumunu kontrol et
        val externalStorageState = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED != externalStorageState) {
            Toast.makeText(this, "Harici depolama kullanılamıyor veya yazılabilir değil.", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "AndroidCodility/Audios")
        // Klasörü oluşturmak için mkdirs() kullan
        if (!file.mkdirs()) {
            // Klasör oluşturma başarısız olursa işle
            Toast.makeText(this, "Klasör oluşturulamadı.", Toast.LENGTH_SHORT).show()
            return
        }

        fileName = file.absolutePath + "/" + (System.currentTimeMillis().toString() + ".mp3")
        Log.d("filename", fileName!!)
        val audioFile = File(fileName!!)

        // Dosya zaten varsa veya oluşturulamazsa işle
        if (!audioFile.createNewFile()) {
            Toast.makeText(this, "Dosya oluşturulamadı.", Toast.LENGTH_SHORT).show()
            return
        }

        mRecorder!!.setOutputFile(fileName)

        try {
            mRecorder!!.prepare()
            mRecorder!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        lastProgress = 0
        seekBar.progress = 0
        stopPlaying()
        // imageView'i durdurma düğmesi yap ve kronometreyi başlat
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()
    }

    private fun stopRecording() {
        try {
            mRecorder?.apply {
                stop()
                release()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mRecorder = null
        }

        // Kayıt durduktan sonra dosya adını sıfırla
        fileName = null

        chronometer.stop()
        chronometer.base = SystemClock.elapsedRealtime()
        Toast.makeText(this, "Kaydedilen ses başarıyla kaydedildi.", Toast.LENGTH_SHORT).show()
    }

    private fun startPlaying() {
        mPlayer = MediaPlayer()
        try {
            mPlayer!!.setDataSource(fileName)
            mPlayer!!.prepare()
            mPlayer!!.start()
        } catch (e: IOException) {
            Log.e("LOG_TAG", "prepare() failed")
        }

        // imageView'i duraklatma düğmesi yap
        imgViewPlay.setImageResource(R.drawable.ic_pause_circle)

        seekBar.progress = lastProgress
        mPlayer!!.seekTo(lastProgress)
        seekBar.max = mPlayer!!.duration
        seekBarUpdate()
        chronometer.start()

        mPlayer!!.setOnCompletionListener {
            imgViewPlay.setImageResource(R.drawable.ic_play_circle)
            isPlaying = false
            chronometer.stop()
            chronometer.base = SystemClock.elapsedRealtime()
            mPlayer!!.seekTo(0)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (mPlayer != null && fromUser) {
                    mPlayer!!.seekTo(progress)
                    chronometer.base = SystemClock.elapsedRealtime() - mPlayer!!.currentPosition
                    lastProgress = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private var runnable: Runnable = Runnable { seekBarUpdate() }

    private fun seekBarUpdate() {
        if (mPlayer != null) {
            val mCurrentPosition = mPlayer!!.currentPosition
            seekBar.progress = mCurrentPosition
            lastProgress = mCurrentPosition
        }
        mHandler.postDelayed(runnable, 100)
    }

    private fun getPermissionToRecordAudio() {
        ActivityCompat.requestPermissions(
            this,
            permissionList.toTypedArray(),
            RECORD_AUDIO_REQUEST_CODE
        )
    }

    // requestPermissions(...) çağrısından gelen istekle geri çağrı
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {

            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {

            }else {
                Toast.makeText(this, "Bu uygulamayı kullanmak için izin vermelisiniz. Uygulama kapatılıyor.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    private fun requestManageExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, MANAGE_EXTERNAL_STORAGE_REQUEST_CODE)
            }
        }
    }
}
