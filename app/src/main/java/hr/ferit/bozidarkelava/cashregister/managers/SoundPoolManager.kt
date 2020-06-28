package hr.ferit.bozidarkelava.cashregister.managers

import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.activity.CashRegisterApp

class SoundPoolManager {
    private lateinit var soundPool: SoundPool
    private var mLoaded = false
    private var mSoundMap: HashMap<Int, Int> = HashMap()

    fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.soundPool = SoundPool.Builder().setMaxStreams(1).build()
        } else {
            this.soundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 0)
        }
        this.soundPool.setOnLoadCompleteListener { _, _, _ -> mLoaded = true }
        mSoundMap[R.raw.juntos] = this.soundPool.load(CashRegisterApp.ApplicationContext, R.raw.juntos, 1)
    }

    fun playSound(selectedSound: Int) {
        val soundID: Int = mSoundMap[selectedSound] ?: 0
        this.soundPool.play(soundID, 1f, 1f, 1, 0, 1f)
    }
}