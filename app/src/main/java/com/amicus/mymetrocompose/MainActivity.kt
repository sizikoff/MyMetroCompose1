package com.amicus.mymetrocompose

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
import com.anjlab.android.iab.v3.PurchaseInfo
import me.saket.telephoto.subsamplingimage.SubSamplingImage
import me.saket.telephoto.subsamplingimage.SubSamplingImageSource
import me.saket.telephoto.subsamplingimage.rememberSubSamplingImageState
import me.saket.telephoto.zoomable.ZoomSpec
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.zoomable

class MainActivity : ComponentActivity(),IBillingHandler{
    lateinit var bp:BillingProcessor
    val LICENSE_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoPuR7Cjeyxzo/io/ZuTjc9IRE6GdjsuM6GGcfFN6tTQFz9zTh1I8FMuet7zkr9aFTEMItD45HTP4jR+BWCt7557N6+vZ0rHqTbgirwmNxquPb6y3Jk3eL8RQBVHmZZtViHqsM9hfLLmiHmmzb1yZwFNY6mn/UW1fNY0eUV0oi81ZIDJ3qWie8XNcve0LKFLWAHN1G6bDPeYQLD2ym/RGK/YkMtsow6e9eN2K+dsa3zWZiG10ZKqIrNsY0JYzgiLBG0BqdwBaw116IaPu5ykeEW0NG4uOnVXc9ILuY9fvgkYfJf1OlgslWFTkX1tXz3lq38gA5cge8fcBL6G+/mtZFQIDAQAB"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bp = BillingProcessor(this,LICENSE_KEY,this)
        bp.initialize()
        enableEdgeToEdge()
        setContent {
            imageZooming(bp)
            }
        }

    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {
        if (productId == "pro_upgrade") {
            Toast.makeText(this,"Pro version activated",Toast.LENGTH_LONG).show()
        }
    }

    override fun onPurchaseHistoryRestored() {}

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Toast.makeText(this,"Billing error $error",Toast.LENGTH_LONG).show()
    }

    override fun onBillingInitialized() {
        if (bp.isPurchased("pro_upgrade")) {
            Toast.makeText(this,"Pro already purchased",Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        if (::bp.isInitialized) bp.release()
        super.onDestroy()
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun imageZooming(bp:BillingProcessor) {
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
                IconButton(onClick = {
                    if (!bp.isPurchased("pro_upgrade")) {
                        bp.purchase(context as Activity,"pro_upgrade")
                    }
                }) {
                    Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "share")
                }
            })
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 94.dp)){
        val zoomax = rememberZoomableState(zoomSpec = ZoomSpec(maxZoomFactor = 8f))
        val imageSub = rememberSubSamplingImageState(imageSource = SubSamplingImageSource.resource(R.drawable.metro2025), zoomableState = zoomax)
        SubSamplingImage(
            modifier = Modifier
                .fillMaxSize()
                .zoomable(zoomax),
            state = imageSub,
            contentDescription = "test"
        )
    }
}