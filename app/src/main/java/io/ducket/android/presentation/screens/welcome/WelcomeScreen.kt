package io.ducket.android.presentation.screens.welcome

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.google.accompanist.pager.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.ducket.android.R
import io.ducket.android.presentation.components.AppPrimaryButton
import io.ducket.android.presentation.components.AppSecondaryButton
import io.ducket.android.presentation.navigation.AuthNavGraph
import io.ducket.android.presentation.screens.destinations.SignInScreenDestination
import io.ducket.android.presentation.screens.destinations.SignUpScreenDestination
import io.ducket.android.presentation.ui.theme.DucketAndroidTheme
import io.ducket.android.presentation.ui.theme.SpaceMedium
import io.ducket.android.presentation.ui.theme.SpaceSmall
import io.ducket.android.presentation.ui.theme.caption
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalPagerApi::class
)
@AuthNavGraph(start = true)
@Destination
@Composable
fun WelcomeScreen(
    navigator: DestinationsNavigator,
) {
    val scope = rememberCoroutineScope()
    val pager = rememberPagerState(initialPage = 0)
    var pageTimer by remember { mutableStateOf(5) }

    val changePage: () -> Unit = {
        scope.launch {
            if (pager.currentPage + 1 in 0 until pager.pageCount) {
                pager.animateScrollToPage(pager.currentPage + 1)
            } else {
                pager.animateScrollToPage(0)
            }
        }
    }

    LaunchedEffect(key1 = pageTimer) {
        while (pageTimer > 0) {
            delay(1000)
            pageTimer--
        }
        changePage()
        pageTimer = 5
    }

    WelcomeScreenContent(
        pager = pager,
        onLoginClick = {
            navigator.navigate(SignInScreenDestination)
        },
        onGetStartedClick = {
            navigator.navigate(SignUpScreenDestination)
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreenContent(
    pager: PagerState,
    onLoginClick: () -> Unit,
    onGetStartedClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(5f),
        ) {
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                count = 3,
                state = pager,
                verticalAlignment = Alignment.CenterVertically,
            ) { page ->
                Card(
                    elevation = 0.dp,
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                            lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }

                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                ) {
                    Image(
                        modifier = Modifier.size(250.dp),
                        alpha = 0.3f,
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colors.caption
                        )
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpaceMedium)
                .weight(2f),
            verticalArrangement = Arrangement.spacedBy(SpaceMedium)
        ) {
            Crossfade(
                targetState = pager.targetPage,
                animationSpec = tween(durationMillis = 200)
            ) { page ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(SpaceSmall),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Title $page",
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.caption,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                    )
                }
            }

            HorizontalPagerIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                pagerState = pager,
                activeColor = MaterialTheme.colors.secondary,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpaceMedium)
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            AppPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.get_started_button),
                onClick = onGetStartedClick,
            )

            Spacer(modifier = Modifier.height(SpaceMedium))

            AppSecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.login_button),
                onClick = onLoginClick,
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun WelcomeScreenPreview() {
    DucketAndroidTheme {
        WelcomeScreenContent(
            pager = rememberPagerState(),
            onLoginClick = {},
            onGetStartedClick = {},
        )
    }
}

