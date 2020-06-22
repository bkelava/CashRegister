package hr.ferit.bozidarkelava.cashregister.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.fragments.userRegisterFragments.WelcomePage
import hr.ferit.bozidarkelava.cashregister.R

class MainActivity : AppCompatActivity(),
    Manager {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openFragment(R.id.frame, WelcomePage()
        )
    }

    override fun openFragment(layoutID: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_animation,
                R.anim.exit_animation
            )
            .replace(layoutID, fragment)
            .commit()
    }
}