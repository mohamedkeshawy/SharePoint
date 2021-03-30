package com.example.sharepoint.settingfragment

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.sharepoint.R
import com.example.sharepoint.addpostfragment.AddPostViewModel
import com.example.sharepoint.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {

    lateinit var dataStore  : DataStore<Preferences>


    fun userProfile(context : Context, imageView: ImageView, textViewName : TextView){
        dataStore = context.createDataStore(name = Constants.DATA_STORE_USER_NAME_KEY)
        viewModelScope.launch {
            textViewName.text = showName(Constants.NAME_KEY)
            Picasso.get().load(showImage(Constants.IMAGE_KEY)).into(imageView)
        }
    }

    var firebaseAuth  = FirebaseAuth.getInstance()
    fun userLogout( view : View){
        firebaseAuth.signOut()
        Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_logInFragment)
    }

    suspend fun showName( key : String ): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference  = dataStore.data.first()
        return preference[dataStoreKey]
    }

    suspend fun showImage( key : String ): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference  = dataStore.data.first()
        return preference[dataStoreKey]
    }
}