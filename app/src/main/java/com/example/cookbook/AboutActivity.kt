package com.example.cookbook

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AboutActivity"
        const val EXTRA_COUNT = "EXTRA_COUNT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val count = intent.getIntExtra(EXTRA_COUNT, 0)
        Log.d(TAG, "onCreate: получено количество рецептов = $count")

        findViewById<TextView>(R.id.tvAboutCount).text = getString(R.string.about_count, count)

        val version = getVersionName()
        Log.d(TAG, "Версия приложения: $version")
        findViewById<TextView>(R.id.tvVersion).text = getString(R.string.about_version, version)

        findViewById<Button>(R.id.btnAboutBack).setOnClickListener {
            Log.i(TAG, "Нажата кнопка Назад")
            finish()
        }
    }

    private fun getVersionName(): String {
        return try {
            packageManager.getPackageInfo(packageName, 0).versionName ?: "1.0"
        } catch (e: Exception) {
            Log.e(TAG, "Не удалось получить версию: ${e.message}")
            "1.0"
        }
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
}