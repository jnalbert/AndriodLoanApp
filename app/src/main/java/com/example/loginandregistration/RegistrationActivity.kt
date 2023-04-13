package com.example.loginandregistration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.loginandregistration.databinding.ActivityRegistrationBinding


class RegistrationActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // retreving information from the intent
        var intentUsername = intent.getStringExtra(LoginActivity.EXTRA_USERNAME) ?: ""
        var intentPassword = intent.getStringExtra(LoginActivity.EXTRA_PASSWORD) ?: ""

        // setting the value of the fields based on the passed in data
        binding.editTextRegistrationUsername.setText(intentUsername)
        binding.editTextRegistrationPassword.setText(intentPassword)

        // register an account and send back the username and password to login activity to prefill those fields
       binding.buttonRegistrationRegister.setOnClickListener {
           // the apply lambda can be used to call the functions inside it on the object that apply is called on

           if (true) {
               val user = BackendlessUser()
               user.setProperty("username", binding.editTextRegistrationUsername.text.toString())
                user.setProperty("name", binding.editTextRegistrationName.text.toString())
               user.setProperty("email", binding.editTextRegistrationEmail.text.toString())
               user.password =  binding.editTextRegistrationPassword.text.toString()

               Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser?> {
                   override fun handleResponse(registeredUser: BackendlessUser?) {
                       Log.d("RegistrationActivity", "user registered: ${registeredUser?.objectId} username: ${registeredUser?.getProperty("username")})")

                       val resultIntent = Intent().apply {
                           putExtra(LoginActivity.EXTRA_USERNAME, binding.editTextRegistrationUsername.text.toString())
                           putExtra(LoginActivity.EXTRA_PASSWORD, binding.editTextRegistrationPassword.text.toString())

                       }
                       setResult(Activity.RESULT_OK, resultIntent)
                          finish()
                   }

                   override fun handleFault(fault: BackendlessFault) {
                       // an error has occurred, the error code can be retrieved with fault.getCode()
                          Log.d("RegistrationActivity", "error: ${fault.message}")
                   }
               })
           } else {
               Toast.makeText(this, "Please enter valid information", Toast.LENGTH_SHORT).show()
           }

       }
    }

        private fun areFieldsValid(): Boolean {
            val utilFunctions = RegistrationUtil()
            if (utilFunctions.validateUsername(binding.editTextRegistrationUsername.text.toString())
                && utilFunctions.validateName(binding.editTextRegistrationName.text.toString())
                && utilFunctions.validatePassword(binding.editTextRegistrationPassword.text.toString(), binding.editTextRegistrationConfirmPassword.text.toString())
                && utilFunctions.validateEmail(binding.editTextRegistrationEmail.text.toString())) {
                    return true
            }
            return false
        }
}