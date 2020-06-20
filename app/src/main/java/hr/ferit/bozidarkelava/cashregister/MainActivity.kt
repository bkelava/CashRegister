package hr.ferit.bozidarkelava.cashregister

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import hr.ferit.bozidarkelava.cashregister.Activity.CashRegister
import hr.ferit.bozidarkelava.cashregister.Fragments.LoginPage
import hr.ferit.bozidarkelava.cashregister.Fragments.Manager
import hr.ferit.bozidarkelava.cashregister.Fragments.WelcomePage

class MainActivity : AppCompatActivity(), Manager {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openFragment()
    }

    override fun openFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation)
            .replace(R.id.frame, WelcomePage())
            .commit()
    }
}