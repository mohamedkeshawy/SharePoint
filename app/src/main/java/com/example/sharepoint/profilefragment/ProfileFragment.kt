package com.example.sharepoint.profilefragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.sharepoint.R
import com.example.sharepoint.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding    : FragmentProfileBinding
    val profileViewModel    : ProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner  = this
        binding.profileModel    = profileViewModel

        profileViewModel.userProfile(requireActivity() ,
                                    view ,
                                    binding.viewImageProfileId ,
                                    binding.textViewNameMyProfileId ,
                                    binding.textViewEmailMyProfileId ,
                                    binding.textViewPhoneMyProfileId)


        binding.textViewLogoutId.setOnClickListener {
            var alert = AlertDialog.Builder(requireActivity())
            alert.setTitle("are you need logout")
            alert.setMessage("click yes will go login page")
            alert.setPositiveButton("yes"){dialog,which->

                profileViewModel.userLogout( view)
            }
            alert.setNegativeButton("no",null)
            alert.create().show()
        }
    }
}