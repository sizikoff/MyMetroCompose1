package com.amicus.mymetrocompose

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.amicus.mymetrocompose.ui.theme.MyMetroComposeTheme
import me.saket.telephoto.subsamplingimage.SubSamplingImage
import me.saket.telephoto.subsamplingimage.SubSamplingImageSource
import me.saket.telephoto.subsamplingimage.rememberSubSamplingImageState
import me.saket.telephoto.zoomable.ZoomSpec
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.zoomable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            imageZooming()
            }
        }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun imageZooming() {
    val context = LocalContext.current
    Surface {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.app_name))
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.secondary)
        , actions = {
            IconButton(onClick = {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT,"Download and support this app: https://play.google.com/store/apps/details?id=" + context.packageName)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(context, shareIntent, null)
            }) {
                Icon(imageVector = Icons.Filled.Share, contentDescription = "share")
            }
            })
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 94.dp)){
        val zoomax = rememberZoomableState(zoomSpec = ZoomSpec(maxZoomFactor = 8f))
        val imageSub = rememberSubSamplingImageState(imageSource = SubSamplingImageSource.resource(R.drawable.metro), zoomableState = zoomax)
        SubSamplingImage(
            modifier = Modifier
                .fillMaxSize()
                .zoomable(zoomax),
            state = imageSub,
            contentDescription = "test"
        )
    }
}