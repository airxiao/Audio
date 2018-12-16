package com.airxiao.audio.main

import android.app.DialogFragment
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import com.airxiao.audio.R
import com.airxiao.audio.Utils
import com.airxiao.audio.Utils.parseTime
import com.airxiao.audio.bean.FileInfo
import com.airxiao.audio.business.TaskProxy
import com.airxiao.audio.exception.BaseHandler
import com.airxiao.audio.exception.BusinessErrorCode
import com.airxiao.audio.exception.HandleMessageCode
import kotlinx.android.synthetic.main.fragment_audio.*
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AudioFragment : DialogFragment(), View.OnClickListener, AudioRecordManager.AudioStateListener {

    private var recordPath: String? = null
    private var playPath: String = ""
    private var recordState = RecordState.STATE_RECORD_IDLE
    private var playState = PlayState.STATE_PLAY_IDLE
    private var singleThreadExecutor: ExecutorService? = null
    private var mAudioManager: AudioRecordManager? = null
    private var mRecordListener: RecordListener? = null
    private var isStop: Boolean = false
    private var startRecordTime : Long = 0
    private var durationTime : Int = 0

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_VOICE_CHANGE -> tv_record_time.text = parseTime(Math.ceil((((System.currentTimeMillis() - startRecordTime)/1000).toDouble())).toInt())
                MSG_VOICE_TIME_UP -> stopRecord()
                MSG_DURATION_CHANGE -> if (!isStop) {
                    val position = AudioPlayer.getInstance().getCurrentPosition()
                    seekbar_progress.progress = position / 1000
                }
            }
            super.handleMessage(msg)
        }
    }

    private val voiceStateRunnable = Runnable {
        startRecordTime = System.currentTimeMillis()
        while (recordState == RecordState.STATE_RECORD_RUN) {
            try {
                Thread.sleep(300)
                mHandler.sendEmptyMessage(MSG_VOICE_CHANGE)
                if (System.currentTimeMillis() - startRecordTime >= 60 * 1000) {
                    mHandler.sendEmptyMessage(MSG_VOICE_TIME_UP)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    private val durationChangeRunnable = Runnable {
        while (!isStop) {
            try {
                Thread.sleep(300)
                mHandler.sendEmptyMessage(MSG_DURATION_CHANGE)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    private val longPressRunnable = Runnable {
        tv_record_time.visibility = View.VISIBLE
        iv_record.setImageResource(R.mipmap.uc_icon_audio_record_press)
        tv_state.text = getString(R.string.uc_audio_release_end)
        mAudioManager?.prepareAudio(recordPath)
    }

    enum class PlayState {
        STATE_PLAY_IDLE, //空闲状态
        STATE_PLAY_RUN, //播放状态
        STATE_PLAY_PAUSE //暂停状态
    }

    enum class RecordState {
        STATE_RECORD_IDLE, //空闲状态
        STATE_RECORD_RUN //录制状态
    }

    companion object {
        val TAG = AudioFragment::class.java.name
        val FILE_INFO = "FILE_INFO"
        val MSG_DURATION_CHANGE = 1
        val MSG_VOICE_CHANGE = 2
        val MSG_VOICE_TIME_UP = 3

        fun newInstance(fileInfo : FileInfo?): AudioFragment {
            val frag = AudioFragment()
            if (fileInfo != null) {
                val args = Bundle()
                args.putSerializable(FILE_INFO, fileInfo)
                frag.arguments = args
            }
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_audio, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val window = dialog.window
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        super.onActivityCreated(savedInstanceState)
        val lp = window.attributes
        lp.gravity = Gravity.BOTTOM
        window.attributes = lp
        window.setBackgroundDrawable(ColorDrawable(0x00000000))
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        initData()
        setListener()
    }

    private fun initData() {
        if (arguments != null && arguments.getSerializable(FILE_INFO) as FileInfo != null) {
            val fileInfo = arguments.getSerializable(FILE_INFO) as FileInfo
            if (!TextUtils.isEmpty(fileInfo?.path) && Utils.isFileExists(fileInfo?.path)) {
                setPlayView(fileInfo)
            } else {
                downloadFile(fileInfo)
            }
        } else {
            recordPath = Environment.getExternalStorageDirectory().toString() + File.separator + activity.packageName + "/Task/Audio"
            setRecordView()
        }

        mAudioManager = AudioRecordManager.getInstance()
        mAudioManager?.setOnAudioStateListener(this)
        singleThreadExecutor = Executors.newSingleThreadExecutor()
        isCancelable = false
    }

    private fun downloadFile(fileInfo: FileInfo) {
        progressBar.visibility = View.VISIBLE
        TaskProxy.getInstance().download(fileInfo, object : BaseHandler() {
            override fun handleBusiness(msg: Message) {
                progressBar.visibility = View.GONE
                if (msg.what == HandleMessageCode.HMC_SUCCESS) {
                    fileInfo!!.path = msg.obj as String?
                    setPlayView(fileInfo)
                } else {
                    if (msg.arg1 == BusinessErrorCode.BEC_COMMON_FILE_EXIST) {
                        fileInfo!!.path = msg.obj as String?
                        setPlayView(fileInfo)
                    } else {
                        Toast.makeText(activity, R.string.task_file_download_fail, Toast.LENGTH_SHORT).show()
                        iv_record.visibility = View.INVISIBLE
                        tv_cancel.visibility = View.INVISIBLE
                        tv_complete.visibility = View.VISIBLE
                        tv_state.text = getString(R.string.task_tip_load_data_fail)
                        val filePath = msg.obj as String?
                        if (Utils.isFileExists(filePath)) {
                            Utils.deleteFile(filePath)
                        }
                    }
                }
            }

        })
    }

    private fun setRecordView() {
        tv_complete.visibility = View.VISIBLE
        iv_play.visibility = if (TextUtils.isEmpty(playPath)) View.INVISIBLE else View.VISIBLE
        iv_record.visibility = if (TextUtils.isEmpty(playPath)) View.VISIBLE else View.INVISIBLE
        ig_voice.visibility = if (TextUtils.isEmpty(playPath)) View.INVISIBLE else View.VISIBLE
        tv_play_time.visibility = if (TextUtils.isEmpty(playPath)) View.INVISIBLE else View.VISIBLE
        seekbar_progress.visibility = if (TextUtils.isEmpty(playPath)) View.INVISIBLE else View.VISIBLE
        tv_cancel.visibility = if (TextUtils.isEmpty(playPath)) View.VISIBLE else View.INVISIBLE
        tv_state.text = if (TextUtils.isEmpty(playPath)) getString(R.string.uc_audio_press_record) else getString(R.string.uc_audio_play)
    }

    private fun setPlayView(fileInfo: FileInfo) {
        playPath = fileInfo.path
        tv_play_time.text = parseTime(fileInfo.duration)
        seekbar_progress.max = fileInfo.duration
        setRecordView()
    }

    private fun setListener() {
        tv_cancel.setOnClickListener(this)
        tv_complete.setOnClickListener(this)
        iv_reset.setOnClickListener(this)
        iv_play.setOnClickListener(this)
        iv_record.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    iv_record.postDelayed(longPressRunnable, 300)
                }
                MotionEvent.ACTION_UP -> handleUpByRecordState()
            }
            true
        }
    }

    private fun handleUpByRecordState() {
        iv_record.removeCallbacks(longPressRunnable)
        when (recordState) {
            RecordState.STATE_RECORD_RUN -> stopRecord()
        }
        recordState = RecordState.STATE_RECORD_IDLE
    }

    private fun stopRecord() {
        if (recordState == RecordState.STATE_RECORD_RUN) {
            if (System.currentTimeMillis() - startRecordTime < 1000) {
                Toast.makeText(activity, R.string.task_tip_record_time_limit, Toast.LENGTH_SHORT).show()
                resetView()
                return
            }
            mAudioManager?.release()
            refreshView()
        }
    }

    private fun refreshView() {
        durationTime = Math.ceil((((System.currentTimeMillis() - startRecordTime)/1000).toDouble())).toInt()
        iv_record.setImageResource(R.mipmap.uc_icon_audio_record_nor)
        tv_state.text = getString(R.string.uc_audio_play)
        iv_record.visibility = View.INVISIBLE
        iv_play.visibility = View.VISIBLE
        iv_play.setImageResource(R.drawable.selector_select_play)
        ig_voice.visibility = View.VISIBLE
        tv_play_time.visibility = View.VISIBLE

        tv_play_time.text = parseTime(durationTime)
        seekbar_progress.visibility = View.VISIBLE
        rll_reset.visibility = View.VISIBLE
        tv_record_time.visibility = View.INVISIBLE
        seekbar_progress.max = durationTime
        seekbar_progress.progress = 0
    }

    private fun resumeAudio() {
        playState = PlayState.STATE_PLAY_RUN
        AudioPlayer.getInstance().resume()
        iv_play.setImageResource(R.drawable.selector_select_pause)
        tv_state.text = getString(R.string.uc_audio_pause)
        startThred()
    }

    private fun pauseAudio() {
        isStop = true
        playState = PlayState.STATE_PLAY_PAUSE
        AudioPlayer.getInstance().pause()
        iv_play.setImageResource(R.drawable.selector_select_play)
        tv_state.text = getString(R.string.uc_audio_play)
    }

    private fun playAudio() {
        AudioPlayer.getInstance().play(playPath, object : AudioPlayer.AudioPlayStateListener {
            override fun onStart() {
                playState = PlayState.STATE_PLAY_RUN
                iv_play.setImageResource(R.drawable.selector_select_pause)
                tv_state.text = getString(R.string.uc_audio_pause)
            }

            override fun onPrepare() {}

            override fun onFinish() {
                playState = PlayState.STATE_PLAY_IDLE
                iv_play.setImageResource(R.drawable.selector_select_play)
                tv_state.text = getString(R.string.uc_audio_play)
                isStop = true
                seekbar_progress.progress = 0
            }

            override fun onError() {
                playState = PlayState.STATE_PLAY_IDLE
            }
        })

        startThred()
    }

    private fun startThred() {
        isStop = false
        singleThreadExecutor?.execute(durationChangeRunnable)
    }

    private fun resetView() {
        tv_play_time.text = ""
        tv_record_time.text = ""
        ig_voice.visibility = View.INVISIBLE
        tv_record_time.visibility = View.INVISIBLE
        seekbar_progress.visibility = View.INVISIBLE
        rll_reset.visibility = View.INVISIBLE
        iv_play.visibility = View.INVISIBLE
        iv_record.visibility = View.VISIBLE
        tv_state.text = getString(R.string.uc_audio_press_record)
        playPath = ""
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_cancel -> {
                dismiss()
                if (playState != PlayState.STATE_PLAY_IDLE) {
                    AudioPlayer.getInstance().stop()
                }
            }
            R.id.tv_complete -> {
                dismiss()
                if (playState != PlayState.STATE_PLAY_IDLE) {
                    AudioPlayer.getInstance().stop()
                }
                if (mRecordListener != null && !TextUtils.isEmpty(playPath)) {
                    mRecordListener!!.onAudioRecorded(playPath, durationTime)
                }
            }
            R.id.iv_reset -> {
                if (playState != PlayState.STATE_PLAY_IDLE) {
                    AudioPlayer.getInstance().stop()
                }
                if (!TextUtils.isEmpty(playPath)) {
                    Utils.deleteFile(playPath)
                }
                resetView()
            }
            R.id.iv_play -> {
                when (playState) {
                    PlayState.STATE_PLAY_IDLE -> playAudio()
                    PlayState.STATE_PLAY_RUN -> pauseAudio()
                    PlayState.STATE_PLAY_PAUSE -> resumeAudio()
                }
            }
        }
    }

    override fun wellPrepared(path: String) {
        playPath = path
        recordState = RecordState.STATE_RECORD_RUN
        Thread(voiceStateRunnable).start()
    }

    public fun setOnRecordListener(listener: RecordListener) {
        this.mRecordListener = listener
    }

    interface RecordListener {
        fun onAudioRecorded(path: String, audioDuration: Int)
    }

}