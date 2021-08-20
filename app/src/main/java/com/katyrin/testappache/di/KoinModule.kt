package com.katyrin.testappache.di

import com.katyrin.testappache.model.datasource.LocalDataSource
import com.katyrin.testappache.model.datasource.LocalDataSourceImpl
import com.katyrin.testappache.model.repository.LocalRepository
import com.katyrin.testappache.model.repository.LocalRepositoryImpl
import com.katyrin.testappache.viewmodel.HomeViewModel
import org.koin.dsl.module

val application = module {
    single<LocalDataSource> { LocalDataSourceImpl() }
    single<LocalRepository> { LocalRepositoryImpl(localDataSource = get()) }
}

val homeModule = module {
    factory { HomeViewModel(localRepository = get()) }
}