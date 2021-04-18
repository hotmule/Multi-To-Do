package ru.hotmule.lastik

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import ru.hotmule.lastik.data.prefs.AndroidPrefs
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.feature.root.RootComponentImpl
import ru.hotmule.lastik.ui.compose.AndroidLastikTheme
import ru.hotmule.lastik.ui.compose.root.RootContent
import ru.hotmule.lastik.utils.AndroidBrowser

class MainActivity : AppCompatActivity() {

    lateinit var rootComponent: RootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AndroidLastikTheme {
                ProvideWindowInsets {

                    val prefs = PrefsStore(AndroidPrefs(this))
                    val insets = LocalWindowInsets.current.systemBars

                    rootComponent = rememberRootComponent {
                        RootComponentImpl(
                            componentContext = it,
                            storeFactory = DefaultStoreFactory,
                            httpClient = LastikHttpClient(prefs),
                            prefsStore = prefs,
                            webBrowser = AndroidBrowser(this)
                        )
                    }

                    RootContent(
                        rootComponent,
                        insets.top.toDp.dp,
                        insets.bottom.toDp.dp
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data?.let {
            rootComponent.onTokenUrlReceived(it.toString())
        }
    }

    private val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt()
}