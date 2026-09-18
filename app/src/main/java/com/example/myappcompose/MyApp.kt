package com.example.myappcompose

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class MiApp : Application() {
    override fun onCreate() {
        super.onCreate()
// FirebaseApp.getInstance() no lanza excepción si ya fue inicializada
        val app = FirebaseApp.getInstance()
        Log.d("MiApp", "Firebase inicializado: ${app.name}")
    }
}