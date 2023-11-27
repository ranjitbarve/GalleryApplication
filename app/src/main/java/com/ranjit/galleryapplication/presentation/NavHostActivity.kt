package com.ranjit.galleryapplication.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ranjit.galleryapplication.R
import dagger.hilt.android.AndroidEntryPoint

/*Key points:

@AndroidEntryPoint: An annotation for Hilt to enable dependency injection in the Android class (NavHostActivity).*/
@AndroidEntryPoint
class NavHostActivity : AppCompatActivity() {

    // This class represents the main activity of the application, annotated with @AndroidEntryPoint for Hilt support.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_host)

        // Set the content view to the activity_nav_host layout.

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // This function is called when an item in the options menu is selected.

        when (item.itemId) {
            android.R.id.home -> {
                // If the selected item is the home/up button in the action bar, handle the action.

                onBackPressed() // Navigate back when the home/up button is pressed.
                return true // Indicate that the action has been handled.
            }
        }
        return super.onOptionsItemSelected(item)
        // If the selected item is not handled here, pass the event to the superclass.
    }
}
