package com.airxiao.audio

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.airxiao.audio.main.AudioFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textview.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val fragment = AudioFragment.newInstance(null)
                fragment.show(fragmentManager, AudioFragment.TAG)
                fragment.setOnRecordListener(object : AudioFragment.RecordListener {
                    override fun onAudioRecorded(path: String, audioDuration: Int) {

                    }

                })
            }

        })
    }
}
