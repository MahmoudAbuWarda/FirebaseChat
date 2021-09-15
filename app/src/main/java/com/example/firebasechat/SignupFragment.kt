package com.example.firebasechat

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.protobuf.Empty
import java.util.*


class SignupFragment : Fragment() {
    lateinit var firebaseStorage: FirebaseStorage
lateinit var auth:FirebaseAuth
    var phtotUrl:String="https://firebasestorage.googleapis.com/v0/b/fir-chat-cb706.appspot.com/o/images%2FuserImages%2Fprofileimage.jpg?alt=media&token=45b6e561-aca6-4483-bcaf-be912630da9e"
    lateinit var myImageView:ImageView
    lateinit var file:StorageReference
     var imageUri: Uri=phtotUrl.toUri()
    var addedPhoto=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myImageView=view.findViewById<ImageView>(R.id.GalleryPhotoImageView)

        auth = FirebaseAuth.getInstance()

        auth.addAuthStateListener {
            if (auth.currentUser != null && !auth.currentUser?.isEmailVerified!!) {
                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        (requireActivity() as MainActivity).logout()
                        findNavController().navigate(R.id.loginFragment)
                    }

            }
        }


        var signUpBtn = view.findViewById<Button>(R.id.signupBtn)
            var loadPhotoBtn=view.findViewById<Button>(R.id.loadPhotoBtn)
        signUpBtn.setOnClickListener {
            signUpBtn.isEnabled=false
            signUpBtn.text="SigningUp"
            var nickname = view.findViewById<EditText>(R.id.nicknameEd)
            var email = view.findViewById<EditText>(R.id.emailEd)
            var password = view.findViewById<EditText>(R.id.passwordEd)
            var confirmpassword=view.findViewById<EditText>(R.id.confirmPasswordEd)
            var patern=Patterns.EMAIL_ADDRESS
            if(nickname.text.toString().isNullOrEmpty()){
                Toast.makeText(view.context,"Error Please fill nickname ",Toast.LENGTH_LONG).show()
                nickname.setError("Error, Please fill nickname")
                signUpBtn.isEnabled=true
                signUpBtn.text="SignUp"

            }else if( email.text.toString().isNullOrEmpty()){
                Toast.makeText(view.context,"Error Please fill email",Toast.LENGTH_LONG).show()
                email.setError("Error, Please fill email")
                signUpBtn.isEnabled=true
                signUpBtn.text="SignUp"

            }else if( password.text.toString().isNullOrEmpty()){
                Toast.makeText(view.context,"Error Please fill password",Toast.LENGTH_LONG).show()

                password.setError("Error, Please fill password")
                signUpBtn.isEnabled=true
                signUpBtn.text="SignUp"

            }
//            else if( confirmpassword.text.toString().isNullOrEmpty()) {
//                Toast.makeText(view.context,"Error Please fill nickname and email and password and confirm password",Toast.LENGTH_LONG).show()
//            nickname.error=""
//            }
            else if(!password.text.toString().equals(confirmpassword.text.toString()))
            {
              //  Log.d("ttt","password:${password.text.toString()} confrimpassword:${confirmpassword.text.toString()}")
                Toast.makeText(view.context,"Confirm password is not match ,Please Confirm your password",Toast.LENGTH_LONG).show()
                password.setError("Error, Confirm password not match")
                signUpBtn.isEnabled=true
                signUpBtn.text="SignUp"
              // confirmpassword.error="Error, Confirm password not match"
            }else if(!patern.matcher(email.text.toString()).matches()){
                Toast.makeText(view.context,"Please enter your correct email address",Toast.LENGTH_LONG).show()
         //    email.error="Error,not match Email Format"

            email.setError("Error,not match Email Format")
                signUpBtn.isEnabled=true
                signUpBtn.text="SignUp"


            }
            else{
                if(addedPhoto&& file!=null ){
                file.putFile(imageUri!!).addOnSuccessListener {
                    file.downloadUrl.addOnSuccessListener {
                        phtotUrl=it.toString()

                    }
                    file.downloadUrl.addOnFailureListener {
                        Toast.makeText(activity?.applicationContext!!,it.toString(),Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(activity?.applicationContext!!,it.toString(),Toast.LENGTH_LONG).show()

                }}
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "user created successfully", Toast.LENGTH_LONG).show()
                    var request = UserProfileChangeRequest.Builder()
                        .setDisplayName(nickname.text.toString())
                        .setPhotoUri(phtotUrl.toUri())
                        .build()
                    auth.currentUser?.updateProfile(request)?.addOnSuccessListener {
                        (requireActivity() as MainActivity).updateUserUi(auth.currentUser)

                    }
                } else {
                    Toast.makeText(
                        context,
                        "Cannot create user: " + it.exception,
                        Toast.LENGTH_LONG


                    ).show()
                    signUpBtn.isEnabled=true
                    signUpBtn.text="SignUp"
                }}
            }

        }

            loadPhotoBtn.setOnClickListener {
                val gallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery,222)
            }
    }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==222&&resultCode == Activity.RESULT_OK){
            if(data!=null) {
                imageUri = data?.data!!

                Glide.with(activity?.applicationContext!!).load(imageUri).into(myImageView)
                firebaseStorage = FirebaseStorage.getInstance()
                file = firebaseStorage.reference.child("images").child("userImages")
                    .child(UUID.randomUUID().toString())
                addedPhoto=true
            }
        }
    }

}