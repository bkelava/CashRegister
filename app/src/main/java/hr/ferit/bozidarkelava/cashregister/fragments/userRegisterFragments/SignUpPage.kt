package hr.ferit.bozidarkelava.cashregister.fragments.userRegisterFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments.MainMenu
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.miscellaneous.StringValues
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.viewModels.SignUpViewModel
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentSignUpPageBinding

class SignUpPage : Fragment(), Manager {

    private var strValues: StringValues = StringValues()

    private lateinit var binding: FragmentSignUpPageBinding
    private lateinit var viewModel: SignUpViewModel

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up_page, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFragment(view)
    }

    private fun setUpFragment(view: View)
    {
        setUpUI()
    }

    private fun setUpUI() {
        binding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_sign_up_page)
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)

        viewModel.notification.observe(this.requireActivity(), androidx.lifecycle.Observer { binding.invalidateAll() })

        setUpBinding()
    }

    private fun setUpBinding()
    {
        binding.apply {
            binding.notification=viewModel
            binding.btnBack.setOnClickListener() {
                openFragment(R.id.frameSignUpPage, LoginPage())
            }
            binding.btnCreate.setOnClickListener() {
                var email = binding.etEmail.text.toString()
                var password = binding.etPassword.text.toString()
                var repeatPassword = binding.etRepeatPassword.text.toString()

                if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                    viewModel.setText(strValues.ERROR_EMAIL_PASSWORD)
                }
                else {
                    if (password == repeatPassword) {
                        viewModel.setText(strValues.PROCESSING)
                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                            openFragment(R.id.frameSignUpPage, MainMenu())
                        }
                    }
                    else {
                        viewModel.setText(strValues.ERROR_PASSWORD_MATCH)
                    }
                }
            }
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