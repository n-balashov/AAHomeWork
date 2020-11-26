package com.aacademy.homework.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.aacademy.homework.R.id
import com.aacademy.homework.R.layout
import com.aacademy.homework.data.local.model.Movie
import com.aacademy.homework.ui.fragments.FragmentMoviesDetails
import com.aacademy.homework.ui.fragments.FragmentMoviesList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .add(id.flContainer, FragmentMoviesList.newInstance(), FRAGMENT_TAG)
                .commit()
    }

    fun openMovieDetail(movie: Movie) {
        supportFragmentManager
            .beginTransaction()
            .add(id.flContainer, FragmentMoviesDetails.newInstance(movie), FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    companion object {

        private const val FRAGMENT_TAG = "FragmentTag"
    }
}