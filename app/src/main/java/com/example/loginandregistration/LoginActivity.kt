package com.example.loginandregistration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.loginandregistration.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LoginActivity"
        // the values we send in companion objects are called extras
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
    }

    val startRegistrationForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            binding.editTextLoginUsername.setText(intent?.getStringExtra(LoginActivity.EXTRA_USERNAME))
            binding.editTextLoginPassword.setText(intent?.getStringExtra(LoginActivity.EXTRA_PASSWORD))
        }
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize backendless
        Backendless.initApp( this, Constants.APP_ID, Constants.API_KEY);

        // login with backendless
        binding.buttonLoginLogin.setOnClickListener {
            Backendless.UserService.login(
                binding.editTextLoginUsername.text.toString(),
                binding.editTextLoginPassword.text.toString(),
                object : AsyncCallback<BackendlessUser?> {
                    override fun handleResponse(user: BackendlessUser?) {
                        // user has been logged in
                        Log.d(TAG, "handleResponse: ${user?.getProperty("username")} has logged in")

                        val resultIntent = Intent(this@LoginActivity, LoanListActivity::class.java).apply {
                           putExtra(LoanListActivity.USERID, user!!.objectId)
                       }
                          startActivity(resultIntent)
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        // login failed, to get the error code call fault.getCode()
                        Log.d(TAG, "handleFault: ${fault.message}")
                    }
                })
        }

        // retreving information from the intent
        var intentUsername = intent.getStringExtra(LoginActivity.EXTRA_USERNAME) ?: ""
        var intentPassword = intent.getStringExtra(LoginActivity.EXTRA_PASSWORD) ?: ""

        // setting the value of the fields based on the passed in data
        binding.editTextLoginUsername.setText(intentUsername)
        binding.editTextLoginPassword.setText(intentPassword)

        binding.textViewLoginSignup.setOnClickListener {
            // 1. create new new intent with the current activity
            // and the activity to be opened
            val registrationIntent = Intent(this, RegistrationActivity::class.java)
            // 2. optionally add information to send with the intent
            registrationIntent.putExtra(EXTRA_USERNAME, binding.editTextLoginUsername.text.toString())
            registrationIntent.putExtra(EXTRA_PASSWORD, binding.editTextLoginPassword.text.toString())
            // 3a. start the activity
//            startActivity(registrationIntent)
            // 3b. start the activity for result using the variable for the register for result contract above
            startRegistrationForResult.launch(registrationIntent)
        }
    }


}