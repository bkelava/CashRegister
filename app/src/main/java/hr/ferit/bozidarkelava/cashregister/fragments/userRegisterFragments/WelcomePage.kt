package hr.ferit.bozidarkelava.cashregister.fragments.userRegisterFragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments.CompanyRegistration
import hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments.MainMenu
import hr.ferit.bozidarkelava.cashregister.managers.PreferenceManager

class WelcomePage : Fragment() {

    private val SPLASH_TIME_OUT: Long = 3000

    private lateinit var manager: Manager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome_page, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manager = activity as Manager

        val userEmail = PreferenceManager()
            .getUserEmail()
        val userId = PreferenceManager()
            .getUserId()

        val userCompanyName = PreferenceManager().getCompanyName()

        val handler = Handler()
        handler.postDelayed({
            kotlin.run {

                if (userEmail == "unknown" || userId == "unknown") {
                    manager.openFragment(R.id.frameWelcomePage, SignUpPage())
                }
                else if (userCompanyName == "unknown") {
                    manager.openFragment(R.id.frameWelcomePage, CompanyRegistration())
                }
                else {
                    manager.openFragment(R.id.frameWelcomePage, MainMenu())
                }
            }
        }, SPLASH_TIME_OUT)

        Log.d("EMAIL", userEmail)
        Log.d("ID", userId)
    }
}