package io.sunshine0523.sidebar.ui.sidebar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.android.settingslib.spa.framework.theme.SettingsTheme

/**
 * @author KindBrave
 * @since 2023/10/21
 */
class SidebarSettingsActivity : ComponentActivity() {
    private lateinit var viewModel: SidebarSettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = SidebarSettingsViewModel(application)

        setContent {
            SettingsTheme {
                SidebarSettingsPage(viewModel = viewModel)
            }
        }
    }
}