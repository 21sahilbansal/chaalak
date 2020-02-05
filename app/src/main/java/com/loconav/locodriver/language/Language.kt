package com.loconav.locodriver.language

import com.loconav.locodriver.Constants

data class LanguageDataClass(val shortProperty: String, val longProperty: String)

private val englishLanguageMap = LanguageDataClass(
    Constants.LanguageProperty.ENGLISH_SHORT_PROPERTY,
    Constants.LanguageProperty.ENGLISH_LONG_PROPERTY
)
private val hindiLanguageMap = LanguageDataClass(
    Constants.LanguageProperty.HINDI_SHORT_PROPERTY,
    Constants.LanguageProperty.HINDI_LONG_PROPERTY
)
var languageHashMap: HashMap<Int, LanguageDataClass> = hashMapOf(
    LanguageType.English.num to englishLanguageMap,
    LanguageType.Hindi.num to hindiLanguageMap
)