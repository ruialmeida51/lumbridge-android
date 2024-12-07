package com.eyther.lumbridge

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.eyther.lumbridge.extensions.platform.isDebuggable
import com.eyther.lumbridge.launcher.delegate.tools.DataStoreMigrationHelper
import com.eyther.lumbridge.launcher.delegate.tools.DebugToolsDelegate
import com.eyther.lumbridge.platform.workers.delegate.SetupWorkersDelegate
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class LumbridgeApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var debugToolsDelegate: DebugToolsDelegate

    @Inject
    lateinit var dataStoreMigrationHelper: DataStoreMigrationHelper

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var setupWorkersDelegate: SetupWorkersDelegate

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        tryStartStrictMode()
        super.onCreate()
        tryStartDebugTools()
        tryMakeDataStoreMigrations()
        trySetupWorkers()
    }

    /**
     * Tries to start the debug tools. The [DebugToolsDelegate] is only started if the application is
     * running in debug mode, and that check is done inside the [DebugToolsDelegate] itself. Here,
     * we just call the init method and let the delegate handle the rest.
     */
    private fun tryStartDebugTools() {
        MainScope().launch {
            debugToolsDelegate.init()
        }
    }

    /**
     * Strict mode is used to catch accidental disk or network operations on the main thread and bring them to our
     * attention so we can fix them. This is useful for debugging and catching issues early on.
     *
     * This is only enabled in debug builds.
     */
    private fun tryStartStrictMode() {
        if (applicationContext.isDebuggable()) {
            StrictMode.setThreadPolicy(
                ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )

            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build()
            )
        }
    }

    /**
     * Initially, we stored some data in DataStore which now needs to be migrated to Room due to
     * changing requirements. This method is used to check if there are any migrations to be made
     * and if so, to make them.
     *
     * The order of the migrations is important, as some migrations depend on others. For example,
     * the first snapshot salary migration depends on the user financials salary percentage migration.
     */
    private fun tryMakeDataStoreMigrations() {
        MainScope().launch {
            dataStoreMigrationHelper.tryMigrateMortgage()
            dataStoreMigrationHelper.tryMigrateUserFinancialsSalaryPercentage()
            dataStoreMigrationHelper.tryMigrateSnapshotSalaryAndAllocations()
        }
    }

    /**
     * Setup the workers that will run in the background.
     */
    private fun trySetupWorkers() {
        setupWorkersDelegate.setupWorkers()
    }
}
