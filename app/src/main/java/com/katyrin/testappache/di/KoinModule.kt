package com.katyrin.testappache.di

import androidx.room.Room
import com.katyrin.testappache.bus.EventBus
import com.katyrin.testappache.model.datasource.LocalDataSource
import com.katyrin.testappache.model.datasource.LocalDataSourceImpl
import com.katyrin.testappache.model.interactor.DrawingInteractor
import com.katyrin.testappache.model.interactor.DrawingInteractorImpl
import com.katyrin.testappache.model.interactor.HomeInteractor
import com.katyrin.testappache.model.interactor.HomeInteractorImpl
import com.katyrin.testappache.model.repository.LocalRepository
import com.katyrin.testappache.model.repository.LocalRepositoryImpl
import com.katyrin.testappache.model.storage.ContentDataBase
import com.katyrin.testappache.model.uri.UriGenerator
import com.katyrin.testappache.model.uri.UriGeneratorImpl
import com.katyrin.testappache.viewmodel.DrawingViewModel
import com.katyrin.testappache.viewmodel.HomeViewModel
import org.koin.dsl.module

val application = module {
    single { Room.databaseBuilder(get(), ContentDataBase::class.java, "ContentDB").build() }
    single { get<ContentDataBase>().getImageDao() }
    single<LocalDataSource> { LocalDataSourceImpl(get()) }
    single<LocalRepository> { LocalRepositoryImpl(localDataSource = get()) }
    single<HomeInteractor> { HomeInteractorImpl(localRepository = get()) }
    single<DrawingInteractor> {
        DrawingInteractorImpl(localRepository = get(), uriGenerator = get())
    }
}

val uriGeneratorModule = module {
    single<UriGenerator> { UriGeneratorImpl(get()) }
}

val homeModule = module {
    factory { HomeViewModel(homeInteractor = get()) }
}

val drawingModule = module {
    factory { DrawingViewModel(drawingInteractor = get()) }
}

val eventBus = module {
    single { EventBus() }
}