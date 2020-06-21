package hr.ferit.bozidarkelava.cashregister.fragments.userRegisterFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments.MainMenu
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.miscellaneous.StringValues
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.viewModels.SignUpViewModel
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentSignUpPageBinding
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.miscellaneous.isEmailValid
import kotlin.collections.HashMap

class SignUpPage : Fragment(), Manager, MVVM {

    private var strValues: StringValues = StringValues()

    private lateinit var binding: FragmentSignUpPageBinding
    private lateinit var viewModel: SignUpViewModel

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var firebaseReference: DatabaseReference

    private var userID: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up_page, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragment()
    }

    override fun setUpFragment()
    {
        setUpUI()
        setUpBinding()
    }

    override fun setUpUI() {
        binding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_sign_up_page)
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
    }

    override fun setUpBinding()
    {
        binding.apply {
            binding.notification=viewModel
            binding.btnBack.setOnClickListener() {
                openFragment(R.id.frameSignUpPage, LoginPage())
            }

            binding.btnCreate.setOnClickListener() {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val repeatPassword = binding.etRepeatPassword.text.toString()

                if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() && !email.isEmailValid()) {
                    viewModel.setSignUpNotificationText(strValues.ERROR_EMAIL_PASSWORD)
                }
                else {
                    if (password.length <=6)
                    {
                        viewModel.setSignUpNotificationText(strValues.PASSWORD_SHORT)
                    }
                    else {
                        if (password == repeatPassword) {
                            viewModel.setSignUpNotificationText(strValues.PROCESSING)
                            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                                firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(
                                    OnCompleteListener {task ->
                                        if (task.result?.signInMethods!!.isEmpty()) {
                                            userID = firebaseAuth.currentUser!!.uid
                                            Log.d("KEY", userID)
                                            firebaseReference = FirebaseDatabase.getInstance().reference.child(strValues.USERS).child(userID)

                                            var user = HashMap<String,Any>()
                                            user["email"]=email
                                            user["userID"]=userID

                                            firebaseReference.updateChildren(user).addOnCompleteListener() {
                                                openFragment(R.id.frameSignUpPage, MainMenu())
                                            }
                                        }
                                        else {
                                            viewModel.setSignUpNotificationText(strValues.USER_ALREADY_EXIST)
                                            clearFields()

                                        }
                                    })
                            }
                        }
                        else {
                            viewModel.setSignUpNotificationText(strValues.ERROR_PASSWORD_MATCH)
                        }
                    }
                }
            }
        }
        viewModel.notification.observe(this.requireActivity(), androidx.lifecycle.Observer { binding.invalidateAll() })
    }

    private fun clearFields() {
        binding.etEmail.setText("")
        binding.etPassword.setText("")
        binding.etRepeatPassword.setText("")
    }

    override fun openFragment(layoutID: Int, fragment: Fragment) {
        val context = activity as AppCompatActivity
        context.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation)
            .replace(layoutID, fragment)
            .commit()
    }
}