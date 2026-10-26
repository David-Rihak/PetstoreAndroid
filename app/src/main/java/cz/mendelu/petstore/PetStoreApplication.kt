package cz.mendelu.petstore

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @HiltAndroidApp generates the application-level dependency container.
 *
 * Whether the app talks to the real backend or serves canned data is decided by
 * BuildConfig.USE_MOCK_DATA (see app/build.gradle.kts) rather than by a hand-edited
 * constant, so debug and release builds differ without anyone editing source.
 */
@HiltAndroidApp
class PetStoreApplication : Application()
