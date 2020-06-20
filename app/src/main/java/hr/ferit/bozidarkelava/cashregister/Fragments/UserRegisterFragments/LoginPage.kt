package hr.ferit.bozidarkelava.cashregister.Fragments.UserRegisterFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import hr.ferit.bozidarkelava.cashregister.Interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.R
import kotlinx.android.synthetic.main.fragment_login_page.view.*
import kotlinx.android.synthetic.main.fragment_login_page.view.btnExit

class LoginPage : Fragment(),
    Manager {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login_page, container, false)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUp(view)
        exit(view);
    }

    private fun signUp(view: View) {
        view.btnSignUp.setOnClickListener {
            openFragment(R.id.frameLoginPage,
                SignUpPage()
            )
        }
    }

    private fun exit(view: View)
    {
        view.btnExit.setOnClickListener(){
            System.exit(0);
        }
    }

    override fun openFragment(layoutID: Int, fragment: Fragment) {
        val context = activity as AppCompatActivity
        context.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation)
            .replace(layoutID, fragment)
            .commit()
    }
}