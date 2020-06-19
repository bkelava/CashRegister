package hr.ferit.bozidarkelava.cashregister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.ferit.bozidarkelava.cashregister.Fragments.LoginPage
import hr.ferit.bozidarkelava.cashregister.Fragments.WelcomePage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.frame, WelcomePage()).commit()
    }
}