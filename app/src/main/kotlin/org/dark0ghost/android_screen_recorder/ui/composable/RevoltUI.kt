package org.dark0ghost.android_screen_recorder.ui.composable

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.dark0ghost.android_screen_recorder.R
import org.dark0ghost.android_screen_recorder.states.ClickState
import org.dark0ghost.android_screen_recorder.utils.Settings.ComposeSettings.isClicked



@OptIn(ExperimentalFoundationApi::class)
@Preview(name = "MainUIPreview", group = "Overlay",
    device = "spec:shape=Normal,width=1440,height=3040,unit=px,dpi=640",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, apiLevel = 31
)
@Composable
fun RevoltUi(modifier: Modifier  = Modifier, onClick: () -> ClickState = { ClickState.NotUsed }, onLongClick: () -> Unit = { println("long tap") } ) {
    LazyColumn(
        modifier = modifier
            .wrapContentWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)

    ) {
        item(isClicked) {
                Image(
                    painter = painterResource(id = R.drawable.recording_64),
                    contentDescription = null,
                    modifier.combinedClickable(
                        onClick = { isClicked.value = onClick() },
                        onLongClick = onLongClick,
                    )
                        .fillMaxWidth()
                        .fillMaxHeight()

                )
        }
    }
}

