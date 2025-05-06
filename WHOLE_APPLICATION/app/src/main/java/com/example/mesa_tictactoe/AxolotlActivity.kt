package com.example.mesa_tictactoe

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import androidx.core.net.toUri

class AxolotlActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var factTextView: TextView
    private lateinit var foxImageView: ImageView
    private lateinit var loadingSpinner: ProgressBar
    private lateinit var videoView: VideoView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_axolotl)

        titleTextView = findViewById(R.id.titleTextView)
        factTextView = findViewById(R.id.factTextView)
        foxImageView = findViewById(R.id.foxImageView)
        loadingSpinner = findViewById(R.id.loadingSpinner)
        videoView = findViewById(R.id.videoView)
        backButton = findViewById(R.id.backButton)

        setupVideo()

        showLoadingSpinner()
        fetchRandomFact()
        fetchRandomFox()

        backButton.setOnClickListener {
            finish()
        }

        factTextView.setOnClickListener {
            showLoadingSpinner()
            fetchRandomFact()
            fetchRandomFox()
        }

        val openNotesButton = findViewById<Button>(R.id.openNotesButton)
        openNotesButton.setOnClickListener {
            startActivity(Intent(this, NotesActivity::class.java))
        }
    }

    // The video will play on a regular phone
    private fun setupVideo() {
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.axel_mesa}")
        videoView.setVideoURI(videoUri)
        videoView.start()

        // This makes the video loop forever
        videoView.setOnCompletionListener {
            videoView.start()
        }
    }

    private fun fetchRandomFact() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://uselessfacts.jsph.pl/random.json?language=en")
            .build()

        client.newCall(request).enqueue(object : Callback {
            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    factTextView.text = "Failed to load fact!"
                    hideLoadingSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonString = response.body?.string()
                    val jsonObject = JSONObject(jsonString ?: "")
                    val factText = jsonObject.getString("text")

                    runOnUiThread {
                        factTextView.text = factText
                        animateFadeIn(factTextView)
                        hideLoadingSpinner()
                    }
                } else {
                    runOnUiThread {
                        factTextView.text = "Failed to load fact!"
                        hideLoadingSpinner()
                    }
                }
            }
        })
    }

    private fun fetchRandomFox() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://randomfox.ca/floof/")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    hideLoadingSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonString = response.body?.string()
                    val jsonObject = JSONObject(jsonString ?: "")
                    val foxUrl = jsonObject.getString("image")

                    runOnUiThread {
                        Glide.with(this@AxolotlActivity)
                            .load(foxUrl)
                            .into(foxImageView)
                        animateFadeIn(foxImageView)
                        hideLoadingSpinner()
                    }
                } else {
                    runOnUiThread {
                        hideLoadingSpinner()
                    }
                }
            }
        })
    }

    private fun showLoadingSpinner() {
        loadingSpinner.visibility = View.VISIBLE
        factTextView.visibility = View.GONE
        foxImageView.visibility = View.GONE
    }

    private fun hideLoadingSpinner() {
        loadingSpinner.visibility = View.GONE
        factTextView.visibility = View.VISIBLE
        foxImageView.visibility = View.VISIBLE
    }

    private fun animateFadeIn(view: View) {
        view.alpha = 0f
        view.animate()
            .alpha(1f)
            .setDuration(500)
            .start()
    }
}