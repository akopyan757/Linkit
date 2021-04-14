package com.akopyan757.linkit_domain.usecase.folder

import com.akopyan757.linkit_domain.entity.FolderEntity
import com.akopyan757.linkit_domain.repository.ILocalFolderDataSource
import com.akopyan757.linkit_domain.usecase.ObservableUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class ListenFoldersUseCase(
    private val localDataSource: ILocalFolderDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): ObservableUseCase<List<FolderEntity>>(schedulerProvider, compositeDisposable) {

    override fun launch() = localDataSource.listenFolders()
}