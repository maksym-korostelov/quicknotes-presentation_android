package com.example.quicknotes.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
)

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
) {
    val pages = listOf(
        OnboardingPage(
            icon = Icons.Default.List,
            title = "Welcome to QuickNotes",
            subtitle = "Capture your ideas and keep them organized in one place. Create notes, add categories, and find anything quickly with search.",
        ),
        OnboardingPage(
            icon = Icons.Default.Folder,
            title = "Organize with categories",
            subtitle = "Group your notes by Work, Personal, Ideas, or any category you like. Tap a category to see only those notes.",
        ),
        OnboardingPage(
            icon = Icons.Default.Search,
            title = "Search in an instant",
            subtitle = "Use the Search tab to find notes by title or content. Your notes are always one tap away.",
        ),
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(bottom = 32.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                onClick = onComplete,
                modifier = Modifier.padding(8.dp),
            ) {
                Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = true,
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = pages[page].icon,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = pages[page].title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = pages[page].subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pages.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(8.dp)
                        .background(
                            color = if (pagerState.currentPage == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = CircleShape,
                        ),
                )
            }
        }

        Button(
            onClick = {
                if (pagerState.currentPage < pages.size - 1) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onComplete()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp),
        ) {
            Text(
                if (pagerState.currentPage < pages.size - 1) "Next" else "Get Started",
            )
        }
    }
}
