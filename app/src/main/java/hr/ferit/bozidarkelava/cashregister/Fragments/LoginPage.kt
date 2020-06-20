package hr.ferit.bozidarkelava.cashregister.Fragments

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import hr.ferit.bozidarkelava.cashregister.Activity.CashRegister
import hr.ferit.bozidarkelava.cashregister.R
import kotlinx.android.synthetic.main.fragment_login_page.*
import kotlinx.android.synthetic.main.fragment_login_page.view.*
import kotlinx.android.synthetic.main.fragment_login_page.view.btnExit

class LoginPage : Fragment(), Manager {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login_page, container, false)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SignUp(view)
        Exit(view);
    }

    private fun SignUp(view: View) {
        view.btnSignUp.setOnClickListener {
            openFragment()
        }
    }

    private fun Exit(view: View)
    {
        view.btnExit.setOnClickListener(){
            System.exit(0);
        }
    }

    override fun openFragment() {
        val context = activity as AppCompatActivity
        context.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation)
            .replace(R.id.frameLoginPage, SignUpPage())
            .commit()
    }
}