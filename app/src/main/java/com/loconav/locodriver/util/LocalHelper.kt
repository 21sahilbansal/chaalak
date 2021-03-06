package com.loconav.locodriver.util

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.language.LanguageDataClass
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

object LocaleHelper : KoinComponent {

    private val sharedPreferenceUtil: SharedPreferenceUtil by inject()

    private val languageArray: Array<LanguageDataClass> by inject()

    val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context, Locale.getDefault().language)
        return setLocale(context, lang)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context): Int {
        val persistedData = getPersistedData(context, Locale.getDefault().language)
        return if (persistedData == "hi") {
            1
        } else {
            0
        }
    }

    private fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(context, language)
    }

    fun changeLanguage(context: Context, languageString: String): Context {
        return setLocale(context, languageString)
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String {
        return sharedPreferenceUtil.getData(SELECTED_LANGUAGE, defaultLanguage)
    }

    private fun persist(context: Context, language: String) {
        sharedPreferenceUtil.saveData(SELECTED_LANGUAGE, language)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    fun toggleBetweenHiAndEn(context: Context) {
        changeLanguage(context, languageArray[1 - getLanguage(context)].shortProperty)
    }
}