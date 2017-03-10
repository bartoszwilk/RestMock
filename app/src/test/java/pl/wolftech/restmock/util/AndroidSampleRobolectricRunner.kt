package pl.wolftech.restmock.util

import org.junit.runners.model.InitializationError
import org.robolectric.RobolectricTestRunner
import org.robolectric.TestLifecycle

open class AndroidSampleRobolectricRunner @Throws(InitializationError::class)
constructor(klass: Class<*>) : RobolectricTestRunner(klass) {

    public override fun getTestLifecycleClass(): Class<out TestLifecycle<*>> {
        return AndroidSampleTestLifecycle::class.java
    }
}