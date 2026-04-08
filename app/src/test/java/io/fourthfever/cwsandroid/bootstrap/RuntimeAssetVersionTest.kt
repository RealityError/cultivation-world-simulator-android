package io.fourthfever.cwsandroid.bootstrap

import io.fourthfever.cwsandroid.BuildConfig
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RuntimeAssetVersionTest {
    @Test
    fun runtimeAssetVersionIncludesBuildSpecificEntropy() {
        assertTrue(BuildConfig.RUNTIME_ASSET_VERSION.startsWith(BuildConfig.VERSION_NAME))
        assertNotEquals(BuildConfig.VERSION_NAME, BuildConfig.RUNTIME_ASSET_VERSION)
    }
}
