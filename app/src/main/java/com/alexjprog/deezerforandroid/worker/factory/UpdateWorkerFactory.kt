package com.alexjprog.deezerforandroid.worker.factory

import androidx.work.DelegatingWorkerFactory
import com.alexjprog.deezerforandroid.domain.usecase.CheckForNewEditorialReleasesUseCase
import com.alexjprog.deezerforandroid.domain.usecase.CheckForNewEditorialWeeklySelectionUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateWorkerFactory @Inject constructor(
    checkForNewEditorialWeeklySelectionUseCase: CheckForNewEditorialWeeklySelectionUseCase,
    checkForNewEditorialReleasesUseCase: CheckForNewEditorialReleasesUseCase
) : DelegatingWorkerFactory() {
    init {
        addFactory(
            EditorialWeeklySelectionCheckWorkerFactory(checkForNewEditorialWeeklySelectionUseCase)
        )
        addFactory(EditorialReleasesCheckWorkerFactory(checkForNewEditorialReleasesUseCase))
    }
}