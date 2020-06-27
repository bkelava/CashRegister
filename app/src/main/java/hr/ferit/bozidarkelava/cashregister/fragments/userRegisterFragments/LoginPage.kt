package hr.ferit.bozidarkelava.cashregister.fragments.userRegisterFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.database.CashRegisterDatabase
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentLoginPageBinding
import hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments.MainMenu
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.managers.PreferenceManager
import hr.ferit.bozidarkelava.cashregister.miscellaneous.*
import hr.ferit.bozidarkelava.cashregister.singleton.UserContainer
import hr.ferit.bozidarkelava.cashregister.viewModels.LogInViewModel

class LoginPage : Fragment(), MVVM {

    private lateinit var manager: Manager

    private var companyInformationDao = CashRegisterDatabase.getInstance().companyInformationDao()

    private val strValues = StringValues()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var binding: FragmentLoginPageBinding
    private lateinit var viewModel: LogInViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_page, container, false)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragment()
    }

    override fun setUpFragment() {
        setUpUI()
        setUpBinding()
    }

    override fun setUpUI() {
        binding =
            DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_login_page)
        viewModel = ViewModelProviders.of(this).get(LogInViewModel::class.java)

        viewModel.notification.observe(
            this.requireActivity(),
            androidx.lifecycle.Observer { binding.invalidateAll() })

        manager = activity as Manager
    }

    override fun setUpBinding() {
        binding.apply {
            binding.notification = viewModel

            binding.btnBack.setOnClickListener() {
                manager.openFragment(R.id.frameLoginPage, SignUpPage())
            }

            binding.btnSignIn.setOnClickListener() {
                login()
            }
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            viewModel.setLogInNotificationText(strValues.ERROR_EMAIL_PASSWORD)
        } else {
            viewModel.setLogInNotificationText(strValues.PROCESSING)
            if (email.isEmailValid()) {
                        firebaseAuth.fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(
                                OnCompleteListener { task ->
                                    if (task.result?.signInMethods!!.isEmpty()) {
                                        viewModel.setLogInNotificationText(strValues.USER_NOT_FOUND)
                                        clearFields()
                                    } else {
                                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener() { task ->
                                                if (task.isSuccessful) {
                                                    UserContainer.setEmail(email)
                                                    val preferenceManager =
                                                        PreferenceManager()
                                                    preferenceManager.saveUserEmail(email)
                                                    preferenceManager.saveUserId(firebaseAuth.currentUser.toString())
                                                    manager.openFragment(R.id.frameLoginPage, MainMenu())
                                                } else {
                                                    viewModel.setLogInNotificationText(strValues.PASSWROD_ERROR)
                                                }
                                            }
                                    }
                                })

            } else {
                viewModel.setLogInNotificationText(strValues.EMAIL_ERROR)
                clearFields()
            }

        }
    }

    private fun clearFields() {
        binding.etEmail.setText("")
        binding.etPassword.setText("")
    }
}