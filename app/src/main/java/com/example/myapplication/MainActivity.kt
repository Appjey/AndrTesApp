package com.example.myapplication

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        val idUrl = intent?.getStringExtra("film")?:"Error"
        Log.e("idUrl", idUrl)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val myWebView = WebView(this)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.domStorageEnabled = true
        myWebView.settings.setAppCacheEnabled(true)
        myWebView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        myWebView.settings.setSupportZoom(true)
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        myWebView.loadUrl(idUrl)
        binding.bLogOut.setOnClickListener{
            auth.signOut()
            val i = Intent(this, Auth::class.java)
            startActivity(i)
            this.finish()
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser == null){
            val i = Intent(this, Auth::class.java)
            startActivity(i)
            this.finish()
        }
    }

}