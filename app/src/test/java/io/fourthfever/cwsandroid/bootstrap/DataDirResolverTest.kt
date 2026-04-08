package io.fourthfever.cwsandroid.bootstrap

import java.io.File
import kotlin.io.path.createTempDirectory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class DataDirResolverTest {
    @Test
    fun usesPrimaryDataDirWhenItIsWritable() {
        val filesDir = createTempDirectory("cws-files").toFile()

        val resolved =
            DataDirResolver().resolve(filesDir)

        assertEquals(File(filesDir, "data"), resolved)
    }

    @Test
    fun fallsBackToRecoveryDirWhenPrimaryDataDirIsNotWritable() {
        val filesDir = createTempDirectory("cws-files").toFile()

        val resolved =
            DataDirResolver(
                writabilityProbe = { dir -> dir.name != "data" },
            ).resolve(filesDir)

        assertEquals(File(filesDir, "data_v2"), resolved)
    }

    @Test
    fun fallsBackWhenPrimarySaveSubdirectoryIsBroken() {
        val filesDir = createTempDirectory("cws-files").toFile()
        val primaryDataDir = File(filesDir, "data")
        primaryDataDir.mkdirs()
        File(primaryDataDir, "saves").writeText("broken")

        val resolved =
            DataDirResolver().resolve(filesDir)

        assertEquals(File(filesDir, "data_v2"), resolved)
    }

    @Test
    fun throwsWhenNoWritableDataDirIsAvailable() {
        val filesDir = createTempDirectory("cws-files").toFile()

        assertThrows(IllegalStateException::class.java) {
            DataDirResolver(
                writabilityProbe = { false },
            ).resolve(filesDir)
        }
    }
}
