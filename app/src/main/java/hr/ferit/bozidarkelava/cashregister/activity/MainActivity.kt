package hr.ferit.bozidarkelava.cashregister.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.fragments.userRegisterFragments.WelcomePage
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments.UpdateStock
import hr.ferit.bozidarkelava.cashregister.interfaces.FragmentCommunicator

class MainActivity : AppCompatActivity(), Manager, FragmentCommunicator {
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

    override fun passData(string: String) {
        val bundle = Bundle()
        bundle.putString("KEY", string)
        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentUpdateStock = UpdateStock()
        fragmentUpdateStock.arguments = bundle

        transaction.replace(R.id.frameViewStock, fragmentUpdateStock)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}