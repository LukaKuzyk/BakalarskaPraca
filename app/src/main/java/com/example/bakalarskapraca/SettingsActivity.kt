package com.example.bakalarskapraca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backToMain: ImageButton = findViewById(R.id.backToMainBtn)
        val switchTheme: Button = findViewById(R.id.theme_switch)
        val account: Button = findViewById(R.id.account_btn)

        var isNightMode:Boolean = false

        val shuffleTests: MaterialButton = findViewById(R.id.shuffleTests)

        var doShuffleTests:Boolean = loadShuffleTestsSettings()
        if (doShuffleTests) {
            shuffleTests.setIconResource(R.drawable.shuffle_icon)
        }else {
            shuffleTests.setIconResource(R.drawable.shuffle_icon_pressed)
        }

        backToMain.setOnClickListener {
            finish()
        }

        account.setOnClickListener{
            if(!User.isLogged)
                startActivity(Intent(this,LoginActivity::class.java))
            else{
                FirebaseAuth.getInstance().signOut()

                gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
                gsc = GoogleSignIn.getClient(this,gso)
                gsc.signOut()

                User.logOutUser()
                finish()
                startActivity(Intent(applicationContext,MainActivity::class.java))
            }

        }


        switchTheme.setOnClickListener {
            // Check what the current night mode setting is
            when (AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_YES -> {
                    // If it's currently set to 'yes' (night mode), change to 'no' (day mode)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    isNightMode = false
                }
                else -> {
                    // If it's currently set to 'no' (day mode) or 'follow system', change to 'yes' (night mode)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    isNightMode = true
                }
            }
            saveNightModToUserSettingsNightMode(isNightMode)
            recreate()
        }

        shuffleTests.setOnClickListener {
            if (doShuffleTests) {
                doShuffleTests = false
                shuffleTests.setIconResource(R.drawable.shuffle_icon_pressed)
            }else {
                doShuffleTests = true
                shuffleTests.setIconResource(R.drawable.shuffle_icon)
            }
            saveShuffleTestsToUserSettings(doShuffleTests)
        }

    }
    fun loadShuffleTestsSettings():Boolean {
        val sharedPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE)
        return sharedPreferences.getBoolean("ShuffleQuestions", false)
    }


    fun saveNightModToUserSettingsNightMode(isNightModeEnabled: Boolean) {
        val sharedPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("NightMode", isNightModeEnabled)

        editor.apply()
    }

    fun saveShuffleTestsToUserSettings( isShuffleQuestionsEnabled: Boolean) {
        val sharedPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("ShuffleQuestions", isShuffleQuestionsEnabled)

        editor.apply()
    }

}
