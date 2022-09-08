package com.example.myapplication.ui.home

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.example.myapplication.HelloScreen
import com.example.myapplication.MainActivity
import com.example.myapplication.MainActivity2
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val database = Firebase.database

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textHome
        //Title of the page
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.title_home)

        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    // EARLY BULLSHIT CODE
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.bBaseUPD.setOnClickListener {

            val imageListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pathToImage = dataSnapshot.getValue<String>()
                    Log.e(TAG, "pathToImage: $pathToImage")

                    binding.textHome.text = pathToImage
                    binding.imageView5.load(pathToImage) {
                        Log.e("TAG", "onDataChange: $pathToImage")
// set on-click listener for ImageView
                        crossfade(false)
                        placeholder(R.drawable.ic_launcher_background)
                        transformations(CircleCropTransformation())
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "Error message", databaseError.toException())
                }
            }
        val filmListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pathToFilm = dataSnapshot!!.getValue<String>()
                Log.e(TAG, "film: $pathToFilm")

                binding.textHome.text = pathToFilm
                binding.imageView5.setOnClickListener {

                    Log.e("TAG", "Click: $pathToFilm")
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("film", pathToFilm.toString())
                    startActivity(intent)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "Error message", databaseError.toException())
            }
        }

            val image = database.getReference("1").child("jpg")
            val film = database.getReference("1").child("film")
            image.addValueEventListener(imageListener)
            film.addValueEventListener(filmListener)

        }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}