package com.alexjprog.deezerforandroid.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alexjprog.deezerforandroid.domain.usecase.CheckForNewEditorialReleasesUseCase
import com.alexjprog.deezerforandroid.util.UpdatesNotificationHelper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest

class EditorialReleasesCheckWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val checkForNewEditorialReleasesUseCase: CheckForNewEditorialReleasesUseCase
) : CoroutineWorker(context, workerParameters) {

    private val updatesNotificationHelper by lazy { UpdatesNotificationHelper(context) }

    override suspend fun doWork(): Result {
        var isError = false
        checkForNewEditorialReleasesUseCase()
            .catch { isError = true }
            .collectLatest { newAlbums ->
                if (newAlbums.isNotEmpty()) {
                    updatesNotificationHelper.sendEditorialReleasesUpdateNotification(
                        newAlbums
                    )
                }
            }

        return if (isError) Result.retry() else Result.success()
    }

    companion object {
        const val UNIQUE_NAME = "editorial_releases_check_worker"
    }
}