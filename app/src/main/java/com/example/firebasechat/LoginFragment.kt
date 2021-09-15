package com.example.firebasechat

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import java.security.AuthProvider
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


class LoginFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var callbackManager = CallbackManager.Factory.create();
        var loginButton =  view.findViewById<Button>(R.id.login_button);

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            (requireActivity() as MainActivity).updateUserUi(auth.currentUser)
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChatFragment())
        }
        var goToSignUp = view.findViewById<Button>(R.id.goToSignupBtn)
        goToSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }

        var loginBtn = view.findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            loginBtn.isEnabled=false
            loginBtn.text="Logingin"
            var username = view.findViewById<EditText>(R.id.emailEd)
            var password = view.findViewById<EditText>(R.id.passwordEd)
            var patern= Patterns.EMAIL_ADDRESS
            if(username.text.toString().isNullOrEmpty() || password.text.toString().isNullOrEmpty()){
                Toast.makeText(view.context,"Error Please fill email and password",Toast.LENGTH_LONG).show()
                username.setError("Please Enter Your username and password")
            loginBtn.isEnabled=true
                loginBtn.text="Login"
            }else if(!patern.matcher(username.text.toString()).matches()){
                Toast.makeText(view.context,"Please enter your correct email address",Toast.LENGTH_LONG).show()
                username.setError("Please enter your correct email address")
                loginBtn.isEnabled=true
                loginBtn.text="Login"
            }

            else{
            auth.signInWithEmailAndPassword(username.text.toString(), password.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    if(auth.currentUser?.isEmailVerified!!) {
                        (requireActivity() as MainActivity).updateUserUi(auth.currentUser)
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChatFragment())
                    }else{
                        (requireActivity() as MainActivity).logout()
                        Toast.makeText(view.context,"Please Verify your Email",Toast.LENGTH_LONG).show()
                        loginBtn.isEnabled=true
                        loginBtn.text="Login"
                    }
                }else{
                    Toast.makeText(context, it.exception.toString(), Toast.LENGTH_LONG).show()
                    loginBtn.isEnabled=true
                    loginBtn.text="Login"
                }
            }}
        }

    }
}