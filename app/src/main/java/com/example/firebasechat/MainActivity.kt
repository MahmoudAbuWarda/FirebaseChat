package com.example.firebasechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
lateinit var toolbar: Toolbar
lateinit var logoutBtn:Button
    lateinit var userDisplayImage:CircleImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.visibility=View.GONE
        var navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController= navHostFragment.findNavController()
         logoutBtn= toolbar.findViewById<Button>(R.id.logoutBtn)
         logoutBtn.setOnClickListener {logout()
            var userDisplayNameTV=findViewById<TextView>(R.id.userDisplayNameTv)

           navController.navigate(R.id.loginFragment)

        }

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()|| super.onSupportNavigateUp()
    }
    fun updateUserUi(firebaseUser: FirebaseUser?){

        var userPhoto=toolbar.findViewById<CircleImageView>(R.id.userPhoto)
        var userDisplayNameTV=findViewById<TextView>(R.id.userDisplayNameTv)
        userDisplayNameTV.text=firebaseUser?.displayName.toString()
        Glide.with(this).load(firebaseUser?.photoUrl.toString()).into(userPhoto)
        Log.d("ttt",firebaseUser?.photoUrl.toString())
        toolbar.visibility=View.VISIBLE

        toolbar.findViewById<Button>(R.id.logoutBtn).visibility=View.VISIBLE
//        toolbar.findViewById<CircleImageView>(R.id.userPhoto).visibility=View.VISIBLE
//        toolbar.findViewById<TextView>(R.id.userDisplayNameTv).visibility=View.VISIBLE

    }
    fun logout(){


            FirebaseAuth.getInstance().signOut()
        toolbar.visibility=View.GONE
//            var useDisplaynameTv=toolbar.findViewById<TextView>(R.id.userDisplayNameTv)
//            useDisplaynameTv.text=""
//        useDisplaynameTv.visibility=View.GONE
//            userDisplayImage=toolbar.findViewById<CircleImageView>(R.id.userPhoto)
//        userDisplayImage.visibility=View.GONE
//            logoutBtn.visibility= View.INVISIBLE
        navController.navigate(R.id.loginFragment)

    }
}