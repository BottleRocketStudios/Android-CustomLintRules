package com.bottlerocketstudios.customlintrules

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bottlerocketstudios.customlintrules.databinding.ActivityMainBinding

/** Adding some unicode to validate the lint rule picks it up: € 👍 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isAdmin = false

        updateGreeting(isAdmin = isAdmin)
        updateFarewell(isAdmin = isAdmin)
        binding.apiKey.text = "Api Key: " + getString(R.string.api_key)
    }


    /**
     * Bidirectional unicode characters interacting with a block comment effectively removes
     * the isAdmin check and executes the body the of if block as if it wasn't there, bypassing
     * the gate setup by isAdmin and executing code that should only be run when the user is an admin.
     */
    private fun updateGreeting(isAdmin: Boolean) {
        binding.greetingMessage.text = "Hello, regular user!"
        /*‮ } ⁦if (isAdmin)⁩ ⁦* begin admins only */
            binding.greetingMessage.text = "Hello, admin!"
            Log.d(TAG, "[login] admin block execution")
        /** end admins only ‮ { ⁦*/
        Log.d(TAG, "[login] standard user execution")
        sayНello()
    }

    private fun updateFarewell(isAdmin: Boolean) {
        binding.farewellMessage.text = "Goodbye, regular user!"
        /** begin admins only */ if (isAdmin) {
            binding.farewellMessage.text = "Goodbye, admin!"
            Log.d(TAG, "[loginWithoutBidiSpoofing] admin block execution")
        /** end admins only */ }
        Log.d(TAG, "[loginWithoutBidiSpoofing] standard user execution")
    }

    /** Assume this function was added originally and is expected to be executed. */
    private fun sayHello() {
        Toast.makeText(this, "Hello!", Toast.LENGTH_LONG).show()
        Log.d(TAG, "[sayHello] Expected function execution using latin H")
        sayНello()
    }

    /**
     * Assume this function was added by a bad actor with malicious intent. In a large file,
     * this could be added somewhere in the same class far from the similarly named ascii version to
     * avoid detection. The bad actor could modify 1+ call site(s) to use this malicious function
     * instead of the original function.
     */
    private fun sayНello() {
        Toast.makeText(this, "Hello, malicious!", Toast.LENGTH_LONG).show()
        Log.d(TAG, "[sayНello] Potentially malicious function execution using cyrillic en") // https://en.wikipedia.org/wiki/En_(Cyrillic)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
