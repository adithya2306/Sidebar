package io.sunshine0523.sidebar.ui.sidebar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.android.settingslib.spa.framework.compose.localNavController
import com.android.settingslib.spa.framework.compose.rememberDrawablePainter
import com.android.settingslib.spa.widget.preference.MainSwitchPreference
import com.android.settingslib.spa.widget.preference.SwitchPreferenceModel
import com.android.settingslib.spa.widget.scaffold.SettingsScaffold
import com.android.settingslib.spa.widget.ui.Category
import io.sunshine0523.sidebar.R
import io.sunshine0523.sidebar.bean.SidebarAppInfo

@Composable
fun SidebarSettingsPage(
    viewModel: SidebarSettingsViewModel
) {
    val navController = rememberNavController()
    val mainChecked = rememberSaveable { mutableStateOf(viewModel.getSidebarEnabled()) }

    CompositionLocalProvider(navController.localNavController()) {
        SettingsScaffold(
            title = stringResource(R.string.sidebar_label)
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                MainSwitchPreference(object : SwitchPreferenceModel {
                    override val title = stringResource(R.string.enable_sideline)
                    override val checked = mainChecked
                    override val onCheckedChange: (Boolean) -> Unit = {
                        mainChecked.value = it
                        viewModel.setSidebarEnabled(it)
                    }
                })

                if (mainChecked.value) {
                    SidebarAppList(viewModel)
                }
            }
        }
    }
}

@Composable
fun SidebarAppList(
    viewModel: SidebarSettingsViewModel
) {
    val sidebarApps by viewModel.appListFlow.collectAsState(emptyList())
    Category(
        title = stringResource(R.string.sidebar_label)
    ) {
        LazyColumn {
            items(sidebarApps) { appInfo ->
                SidebarAppListItem(
                    appInfo = appInfo,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            viewModel.addSidebarApp(appInfo)
                        } else {
                            viewModel.deleteSidebarApp(appInfo)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SidebarAppListItem(
    appInfo: SidebarAppInfo,
    onCheckedChange: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(appInfo.isSidebarApp) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(75.dp)
    ) {
        Image(
            painter = rememberDrawablePainter(appInfo.icon),
            contentDescription = appInfo.label,
            modifier = Modifier
                .size(60.dp)
                .padding(start = 8.dp)
        )
        Text(
            text = appInfo.label,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                onCheckedChange(it)
            },
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}