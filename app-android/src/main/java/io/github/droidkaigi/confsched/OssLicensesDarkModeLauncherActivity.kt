package io.github.droidkaigi.confsched

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 *  This activity temporarily enforces dark mode on another activity to maintain design consistency.
 */
@AndroidEntryPoint
class OssLicensesDarkModeLauncherActivity : ComponentActivity() {

    private val ossLicenseLauncher = registerForActivityResult(StartActivityForResult()) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        ossLicenseLauncher.launch(Intent(this, OssLicensesMenuActivity::class.java))
    }
}
