package hr.ferit.bozidarkelava.cashregister.fragments.userRegisterFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.database.CashRegisterDatabase
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentLoginPageBinding
import hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments.CompanyRegistration
import hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments.MainMenu
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.miscellaneous.StringValues
import hr.ferit.bozidarkelava.cashregister.miscellaneous.hasPermission
import hr.ferit.bozidarkelava.cashregister.miscellaneous.isEmailValid
import hr.ferit.bozidarkelava.cashregister.miscellaneous.requestPermission
import hr.ferit.bozidarkelava.cashregister.singleton.UserContainer
import hr.ferit.bozidarkelava.cashregister.viewModels.LogInViewModel
import kotlin.system.exitProcess

class LoginPage : Fragment(), Manager, MVVM {

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
    }

    override fun setUpBinding() {
        binding.apply {
            binding.notification = viewModel

            binding.btnSignUp.setOnClickListener {
                openFragment(R.id.frameLoginPage, SignUpPage())
            }

            binding.btnExit.setOnClickListener() {
                exitProcess(0);
            }

            binding.btnLogIn.setOnClickListener() {
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
                                                    if (companyInformationDao.checkUserByEmail(
                                                            UserContainer.getEmail()
                                                        ).isNotEmpty()
                                                    ) {
                                                        openFragment(
                                                            R.id.frameLoginPage,
                                                            MainMenu()
                                                        )
                                                    } else {
                                                        openFragment(
                                                            R.id.frameLoginPage,
                                                            CompanyRegistration()
                                                        )
                                                    }
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

    override fun openFragment(layoutID: Int, fragment: Fragment) {
        val context = activity as AppCompatActivity
        context.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation)
            .replace(layoutID, fragment)
            .commit()
    }

    private fun clearFields() {
        binding.etEmail.setText("")
        binding.etPassword.setText("")
    }
}