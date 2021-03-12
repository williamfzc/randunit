package com.williamfzc.randunit_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun a(a: ExampleJava) {}

    private fun b() {}

    companion object {
        fun c() {
            println("okok")
        }
        private fun d() {
            println("yes")
        }

        // this method was designed for making sure these crashes can be found normally

        // this crash should be thrown in cases
//        fun broken() {
//            throw RuntimeException("crash!")
//        }
    }

//    fun importUnknownClass() {
//        Class.forName("someclass.unknown")
//    }
}