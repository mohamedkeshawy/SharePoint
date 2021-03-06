package com.example.sharepoint.authenticationfragment

import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.sharepoint.R
import com.example.sharepoint.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CreateAccountViewModel : ViewModel(){

    var editName    = MutableLiveData<String>("")
    var editPass    = MutableLiveData<String>("")
    var editEmail   = MutableLiveData<String>("")
    var editPhone   = MutableLiveData<String>("")

    var firebaseAuth        = FirebaseAuth.getInstance()
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var userReference       = firebaseDatabase.getReference(Constants.REF_USER)
    var myStorage           = FirebaseStorage.getInstance().reference

    // fun create account
    fun createAccount(context : Context ,
                      view : View,
                      imageUri : Uri ,
                      name : EditText ,
                      pass : EditText ,
                      email : EditText ,
                      phone : EditText,
                      progressBar: ProgressBar){

        if(editName.value!!.trim().isEmpty()){
            name.error = "Please enter your name"
        }else if(editPass.value!!.trim().isEmpty()){
            pass.error = "Please enter your password"
        }else if( editPass.value!!.trim().length < 6){
            pass.error = "the password is not less than 6 number"
        }else if(editEmail.value!!.trim().trim().isEmpty()){
            email.error = "Please enter your email"
        }else if(editEmail.value!!.trim().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(editEmail.value).matches()){
            email.error = "Please enter right email"
        }else if(editPhone.value!!.trim().isEmpty()){
            phone.error = "Please enter your phone"
        }else if(editPhone.value!!.trim().length < 13){
            phone.error = "Please enter your phone with country code"
        }else{

            progressBar.visibility = View.VISIBLE

            // operation for firebase create account with profile photo
            firebaseAuth.createUserWithEmailAndPassword( editEmail.value!!.toString() , editPass.value!!.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    firebaseAuth.currentUser?.sendEmailVerification()
                    var userId = firebaseAuth.currentUser?.uid
                    var id = firebaseAuth.currentUser?.uid

                    var refStorage : StorageReference = myStorage.child("Photo/"+System.currentTimeMillis())
                    refStorage.putFile(imageUri).addOnCompleteListener { itSaveImage ->
                        if(itSaveImage.isSuccessful){
                            refStorage.downloadUrl.addOnSuccessListener { itImageDownload ->

                                var map = HashMap<String , String>()

                                map[Constants.CHILD_NAME_KEY]     = editName.value!!.toString()
                                map[Constants.CHILD_EMAIL_KEY]    = editEmail.value!!.toString()
                                map[Constants.CHILD_PHONE_KEY]    = editPhone.value!!.toString()
                                map[Constants.CHILD_USER_ID_KEY]  = id.toString()
                                map[Constants.CHILD_IMAGE_KEY]    = itImageDownload.toString()

                                userReference.child(userId.toString()).setValue(map)
                                Toast.makeText(context,"Account Created" , Toast.LENGTH_SHORT).show()
                                Navigation.findNavController(view).navigate(R.id.action_createAccountFragment_to_logInFragment)

                                progressBar.visibility = View.INVISIBLE
                            }

                        }else{
                            Toast.makeText(context , "Error ${itSaveImage.exception.toString()}",Toast.LENGTH_SHORT).show()
                            progressBar.visibility = View.INVISIBLE
                        }
                    }

                }else{
                    Toast.makeText(context , "Error ${it.exception.toString()}",Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }
    // fun create account
}