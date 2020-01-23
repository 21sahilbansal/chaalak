//package com.loconav.locodriver.di
//
//import com.loconav.locodriver.network.HttpApiService
//import com.loconav.locodriver.user.UserHttpService
//import com.loconav.locodriver.user.login.NumberLoginViewModel
//import org.koin.androidx.viewmodel.ext.koin.viewModel
//import org.koin.dsl.module.module
//import org.koin.dsl.module.Module
//
//
//val userModule = module {
//
//    single<UserHttpService>{ UserHttpService(get<HttpApiService>())}
//
//    viewModel { NumberLoginViewModel(get<UserHttpService>()) }
//
//}