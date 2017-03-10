package pl.wolftech.restmock.util

import android.app.Application
import io.appflate.restmock.RESTMockServerStarter
import io.appflate.restmock.android.AndroidLocalFileParser
import io.appflate.restmock.android.AndroidLogger
import org.robolectric.DefaultTestLifecycle
import org.robolectric.annotation.Config
import org.robolectric.manifest.AndroidManifest
import pl.wolftech.restmock.RestMockApplication
import java.lang.reflect.Method

open class AndroidSampleTestLifecycle : DefaultTestLifecycle() {

    override fun createApplication(method: Method, appManifest: AndroidManifest, config: Config): Application {
        val app = super.createApplication(method, appManifest, config) as RestMockApplication
        RESTMockServerStarter.startSync(AndroidLocalFileParser(app), AndroidLogger())
        return app
    }
}