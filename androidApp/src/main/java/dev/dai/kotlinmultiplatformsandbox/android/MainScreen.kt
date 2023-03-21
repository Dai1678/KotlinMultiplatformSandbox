package dev.dai.kotlinmultiplatformsandbox.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.dai.kotlinmultiplatformsandbox.android.component.LaunchItem
import dev.dai.kotlinmultiplatformsandbox.entity.RocketLaunch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val refreshState =
        rememberPullRefreshState(
            refreshing = uiState.isRefreshing,
            onRefresh = { viewModel.displayLaunches(true) }
        )
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title)) }
            )
        },
        scaffoldState = scaffoldState
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .pullRefresh(refreshState)
        ) {
            MainContent(launches = uiState.launches)

            PullRefreshIndicator(
                uiState.isRefreshing,
                refreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }

    uiState.event?.let {
        LaunchedEffect(uiState) {
            when (it) {
                is MainEvent.Error -> scaffoldState.snackbarHostState.showSnackbar(
                    message = it.throwable.message ?: "Error"
                )
            }
            viewModel.consumeEvent()
        }
    }
}

@Composable
private fun MainContent(
    launches: List<RocketLaunch>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (launches.isEmpty()) {
            item {
                Text(
                    text = "No Data",
                    modifier = Modifier
                        .fillParentMaxSize()
                        .wrapContentSize()
                )
            }
        } else {
            items(launches) {
                LaunchItem(
                    missionName = it.missionName,
                    launchSuccess = it.launchSuccess,
                    launchYear = it.launchYear,
                    details = it.details
                )
            }
        }
    }
}
