package hr.ferit.bozidarkelava.cashregister.fragments.userRegisterFragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.R

class WelcomePage : Fragment(),
    Manager {

    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome_page, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = Handler()
        handler.postDelayed({
            kotlin.run {
                openFragment(R.id.frameWelcomePage,
                    LoginPage()
                )
            }
        }, SPLASH_TIME_OUT)
    }

    override fun openFragment(layoutID: Int, fragment: Fragment) {
        val context = activity as AppCompatActivity
        context.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation)
            .replace(layoutID, fragment)
            .commit()
    }
}