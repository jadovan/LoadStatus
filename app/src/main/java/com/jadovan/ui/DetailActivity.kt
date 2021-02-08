package com.jadovan.ui

import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jadovan.R
import com.jadovan.util.cancelNotifications
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private var downloadName = ""
    private var downloadStatus = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager = ContextCompat.getSystemService(
                this, NotificationManager::class.java) as NotificationManager
        notificationManager.cancelNotifications()

        downloadName = intent.getStringExtra("downloadName").toString()
        download_name.text = downloadName

        downloadStatus = intent.getStringExtra("downloadStatus").toString()
        download_status.text = downloadStatus

        if (downloadStatus == "Success") {
            download_status.setTextColor(getColor(R.color.colorPrimary))
        } else {
            download_status.setTextColor(Color.RED)
        }

        ok_button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}
