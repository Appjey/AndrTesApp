package com.example.myapplication.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Auth
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    val database = Firebase.database
    val user = Firebase.auth.currentUser
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[NotificationsViewModel::class.java]

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //Title of the page
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.title_notifications)

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        binding.bLogOut2.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(context, Auth::class.java)
            startActivity(intent)
            activity?.onBackPressed()
        }



        val userName = database.getReference("Users").child(user!!.uid).child("name")
        Log.e("TAG", "Username: $userName")
        userName.get().addOnSuccessListener {

            if (it.value != "" && it.value != null) {

                binding.tvUserName.text = it.value.toString()
            }
            else{
                binding.setName.visibility = View.VISIBLE
                binding.bSetUpPsswd.visibility = View.VISIBLE

                binding.bSetUpPsswd.setOnClickListener(){
                    val name = binding.setName.text.toString()
                    databaseReference.child(user.uid).child("name").setValue(name)
                    //TODO add refresh this page
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}