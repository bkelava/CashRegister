package hr.ferit.bozidarkelava.cashregister.Fragments.UserRegisterFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.bozidarkelava.cashregister.Activity.CashRegister
import hr.ferit.bozidarkelava.cashregister.Fragments.UserRegisterFragments.LoginPage
import hr.ferit.bozidarkelava.cashregister.Interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.ViewModels.SignUpViewModel
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentSignUpPageBinding
import kotlinx.android.synthetic.main.fragment_sign_up_page.view.*

class SignUpPage : Fragment(),
    Manager {

    private lateinit var binding: FragmentSignUpPageBinding
    private lateinit var viewModel: SignUpViewModel

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up_page, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUI()

        Back(view);
    }

    private fun setUpUI() {
        binding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_sign_up_page)
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
    }

    private fun Back(view: View) {
        view.btnBack.setOnClickListener() {
            openFragment(R.id.frameSignUpPage,
                LoginPage()
            )
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