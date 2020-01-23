package com.loconav.locodriver.di

import com.apollographql.apollo.ApolloClient
import com.loconav.locodriver.BuildConfig
import com.loconav.locodriver.Constants
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.network.HeaderInterceptor
import com.loconav.locodriver.network.HttpApiService
import com.loconav.locodriver.user.UserHttpService
import com.squareup.picasso.Picasso
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient




const val defaultSharedPfFile = "locodrive_prefs"

/**
 * it will contain providers needed for data operations
 */
val dataModule = module(override = true) {

    /**
     * provide file name for using different SharedPreference file
     * {usage} : val sharedPreferenceUtil : SharedPreferenceUtil by inject { parametersOf("locodrive_prefs") }
     */
    single{
        (fileName : String) -> SharedPreferenceUtil(fileName)
    }


    /**
     * default shared preference type
     * {usage} :  val sharedPreferenceUtil : SharedPreferenceUtil by inject()
     */
    single {
        SharedPreferenceUtil(defaultSharedPfFile)
    }

    /**
     * provides access to database
     * {usage} :  val db : AppDatabase by inject()
     */
    single{ AppDatabase(androidContext()) }

}


/**
 * it will provide all dependencies needed for network
 */
val networkModule = module {

    single {HeaderInterceptor(SharedPreferenceUtil(defaultSharedPfFile), BuildConfig.haul_secret)}


    single<OkHttpClient>{ OkHttpClient.Builder().addInterceptor(get<HeaderInterceptor>()).build() }


    /**
     * provides retrofit client
     */
    single<Retrofit>{
        Retrofit.Builder()
            .baseUrl(BuildConfig.base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>())
            .build()
    }


    /**
     * provides Http service
     * {usage} :  val httpApiService : HttpApiService by inject()
     */
    single<HttpApiService>{
        get<Retrofit>().create(HttpApiService::class.java)
    }


    single<UserHttpService>{ UserHttpService(get()) }

//TODO :  uncomment below code to get graphQL Client.

//    single<ApolloClient> {
//        ApolloClient.builder()
//            .serverUrl(BuildConfig.graphql_base_url)
//            .okHttpClient(get<OkHttpClient>())
//            .build()
//    }
}


/**
 * will resolve app level dependencies like[appContext]
 */
val appModule = module {


    /**
     * {usage}: val picasso : Picasso by inject()
     */
    single { Picasso.get() }
}


val applicationModule = listOf(dataModule, networkModule, appModule)


