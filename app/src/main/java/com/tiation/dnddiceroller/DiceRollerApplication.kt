package com.tiation.dnddiceroller

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the DiceRoller app.
 * Annotated with @HiltAndroidApp to trigger Hilt's code generation.
 * 
 * Built by Garrett Dillman (garrett.dillman@gmail.com, garrett@sxc.codes) 
 * and Tia (tiatheone@protonmail.com) for the DiceRoller project.
 */
@HiltAndroidApp
class DiceRollerApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize any global app components here if needed
    }
}
