package me.shouheng.i18n

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import easy_i18n.composeapp.generated.resources.*
import me.shouheng.i18n.data.Event
import me.shouheng.i18n.data.Router
import me.shouheng.i18n.data.Router.ROUTE_I18N
import me.shouheng.i18n.data.Router.ROUTE_INFO
import me.shouheng.i18n.data.Router.ROUTE_PREMIUM
import me.shouheng.i18n.data.Router.ROUTE_SETTINGS
import me.shouheng.i18n.data.UIConst.LEFT_TOOLBAR_MAX_WIDTH
import me.shouheng.i18n.data.common.HomeTabItem
import me.shouheng.i18n.manager.AppManger
import me.shouheng.i18n.manager.MessageManager
import me.shouheng.i18n.view.I18nPage
import me.shouheng.i18n.view.InfoPage
import me.shouheng.i18n.view.SettingPage
import me.shouheng.i18n.vm.I18nViewModel
import me.shouheng.i18n.vm.InfoViewModel
import me.shouheng.i18n.widget.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun App() {
    val vm: I18nViewModel = viewModel()
    val vmInfo: InfoViewModel = viewModel()

    var appTheme by remember { mutableStateOf(AppManger.getTheme()) }
    val colorScheme = appTheme.getColorSchema()
    val snackBarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    val stateHolder = rememberSaveableStateHolder()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: Router.ROUTE_HOME
    val showAppUpgradeDialog by vmInfo.showAppUpgradeDialog.collectAsState()
    var showSessionExpireDialog by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }
    var premiumDialogMessage by remember { mutableStateOf<String?>(null) }

    val tabs = listOf(
        HomeTabItem(Res.string.tab_i18n_name, Icons.Default.Dataset, ROUTE_I18N, { I18nPage(vm) }),
        HomeTabItem(Res.string.tab_setting_name, Icons.Default.Settings, ROUTE_SETTINGS, { SettingPage() }),
        HomeTabItem(Res.string.tab_info_name, Icons.Default.Info, ROUTE_INFO, { InfoPage(vmInfo) }),
    )

    // 初始化
    LaunchedEffect(Unit) {
        MessageManager.message.collect { snackBarHostState.showMessage(it) }
    }
    // 监听事件
    LaunchedEffect(Unit) {
        MessageManager.event.collect {
            when (it?.name) {
                Event.EVENT_NAME_TO_PAGE -> {
                    val path = it.data as? String ?: return@collect
                    navController.navigate(path) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
                Event.EVENT_THEME_CHANGED -> {
                    appTheme = AppManger.getTheme()
                }
                Event.EVENT_SESSION_EXPIRED -> {
                    showSessionExpireDialog = true
                }
                Event.EVENT_PREMIUM_REQUIRE -> {
                    premiumDialogMessage = it.data as? String
                }
            }
        }
    }

    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { CustomSnackbarHost(snackBarHostState) },
            containerColor = colorScheme.background
        ) { paddingValues ->
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(colorScheme.surface)
                        .verticalScroll(rememberScrollState())
                        .widthIn(max = LEFT_TOOLBAR_MAX_WIDTH.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_logo),
                        modifier = Modifier.size(80.dp).padding(top = 30.dp),
                        contentDescription = null
                    )
                    Text(stringResource(Res.string.app_name), fontSize = 13.sp)

                    NavigationRail(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 30.dp),
                        contentColor = colorScheme.primary
                    ) {
                        tabs.forEach { item ->
                            val colors = HomeTabItem.colors(item.route, colorScheme)
                            NavigationRailItem(
                                icon = { Icon(item.icon, contentDescription = null) },
                                label = { Text(stringResource(item.title), textAlign = TextAlign.Center) },
                                selected = currentRoute == item.route,
                                onClick = {
                                    if (currentRoute != item.route) {
                                        navController.navigate(item.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.padding(bottom = 15.dp),
                                colors = colors
                            )
                        }
                    }
                }
                VerticalDivider(modifier = Modifier.width(0.5.dp).fillMaxHeight())
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(paddingValues)
                ) {
                    NavHost(navController, startDestination = Router.ROUTE_HOME) {
                        tabs.forEach { tab ->
                            composable(tab.route) {
                                stateHolder.SaveableStateProvider(tab.route, tab.content)
                            }
                        }
                        composable(ROUTE_PREMIUM) {
                        }
                    }
                }
            }
        }
    }

    // 升级对话框
    if (showAppUpgradeDialog) {
        UpgradeDialog(vmInfo)
    }
    // 会话过期对话框
    if (showSessionExpireDialog) {
        TokenExpireDialog({ showLoginDialog = true }, { showSessionExpireDialog = false })
    }
    // 会员
    if (premiumDialogMessage != null) {
        PremiumRequireDialog(
            message = premiumDialogMessage ?: "",
            onClose = { premiumDialogMessage = null }
        )
    }
}