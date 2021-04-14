package com.akopyan757.linkit_domain.usecase.folder

import com.akopyan757.linkit_domain.entity.FolderEntity
import com.akopyan757.linkit_domain.repository.ILocalFolderDataSource
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class GetAllFoldersUseCase(
    private val localDataSource: ILocalFolderDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): SingleUseCase<List<FolderEntity>>(schedulerProvider, compositeDisposable) {

    override fun launch() = Single.fromCallable { localDataSource.getAllFolders() }
}