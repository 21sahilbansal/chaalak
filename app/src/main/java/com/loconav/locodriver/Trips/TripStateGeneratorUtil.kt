package com.loconav.locodriver.Trips

import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DRIVER_CTA_CURRENT_STATE_LABEL
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DRIVER_CTA_LABEL_ACTIVITY_END
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DRIVER_CTA_LABEL_ACTIVITY_START
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DRIVER_CTA_LABEL_CHECKPOINT_ENTRY
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DRIVER_CTA_LABEL_CHECKPOINT_EXIT
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DRIVER_CTA_LABEL_TRIP_END
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DRIVER_CTA_LABEL_TRIP_START
import com.loconav.locodriver.Trips.model.DriverCtaTemplateResponse
import com.loconav.locodriver.Trips.model.TripData
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

object TripStateGeneratorUtil : KoinComponent {
    val sharedPreferenceUtil: SharedPreferenceUtil by inject()

    var tripStateList: ArrayList<String> = ArrayList()

    var currentStateOfList: String?=null

    var nextState: String? = null

    var currentCta:String?=null



    fun setStateList(trip: TripData?) {
        trip?.let {
            addStartTripStateToList()
            it.tripSource?.let {
                addEntryEventToList(it.polygonDetail?.checkPointCity.toString())
                if (it.chekPointActivity != null) {
                    it.chekPointActivity?.checkPointActivityName?.let {
                        addCheckPointActivityToTripStateList(it)
                    }
                }
            }
            if (it.checkPointsList.isNullOrEmpty() || it.checkPointsList?.size!! <= 2) {
                it.tripDestination?.polygonDetail?.checkPointCity?.cityName?.let {
                    addLeavingForDestinationStateToList(it)

                }
                it.tripDestination?.polygonDetail?.checkPointCity?.cityName?.let {
                    addEntryEventToList(it)
                }

                if (it.tripDestination?.chekPointActivity != null) {
                    it.tripDestination?.chekPointActivity!!.checkPointActivityName?.let {
                        addCheckPointActivityToTripStateList(it)
                    }
                }
                addEndTripStateToList()

            } else {
                val index = 1
                if (!it.checkPointsList.isNullOrEmpty()) {
                    for (item in it.checkPointsList!!) {
                        val currentCheckPoint = it.checkPointsList!![index]
                        if (index == it.checkPointsList!!.size) {
                            it.tripDestination?.chekPointActivity!!.checkPointActivityName?.let {
                                addCheckPointActivityToTripStateList(it)
                            }
                            break
                        }
                        currentCheckPoint.checkPointPosition?.let {
                            addLeavingCheckpointStateToList(currentCheckPoint.checkPointPosition.toString())
                        }

                        addEntryEventToList(currentCheckPoint.checkPointPosition.toString())

                        if (currentCheckPoint.chekPointActivity != null) {
                            currentCheckPoint.chekPointActivity?.checkPointActivityName?.let {
                                addCheckPointActivityToTripStateList(it)
                            }
                        }
                        it.checkPointsList!![index + 1].checkPointPosition?.let {
                            addLeavingCheckpointStateToList(it.toString())
                        }

                    }
                }

                it.tripDestination?.polygonDetail?.checkPointCity?.cityName?.let {
                    addEntryEventToList(it)
                }

                if (it.tripDestination?.chekPointActivity != null) {
                    it.tripDestination?.chekPointActivity!!.checkPointActivityName?.let {
                        addCheckPointActivityToTripStateList(it)
                    }
                }
                addEndTripStateToList()
            }
        }

    }

    private fun setCurrentState(currentState: String){
        currentStateOfList=currentState
    }

    private fun addLeavingCheckpointStateToList(event: String) {
        tripStateList.add(String.format(sharedPreferenceUtil.getData(
            DRIVER_CTA_LABEL_CHECKPOINT_EXIT,""), event))
    }

    private fun addEndTripStateToList() {
        tripStateList.add(sharedPreferenceUtil.getData(DRIVER_CTA_LABEL_TRIP_END,""))
    }

    private fun addStartTripStateToList() {
        tripStateList.add(sharedPreferenceUtil.getData(DRIVER_CTA_LABEL_TRIP_START,""))
    }

    private fun addCheckPointActivityToTripStateList(checkpointActivityName: String) {
        tripStateList.add(String.format(sharedPreferenceUtil.getData(DRIVER_CTA_LABEL_ACTIVITY_START,""), checkpointActivityName))
        tripStateList.add(String.format(sharedPreferenceUtil.getData(DRIVER_CTA_LABEL_ACTIVITY_END,""), checkpointActivityName))
    }

    private fun addEntryEventToList(event: String) {
        tripStateList.add(String.format(sharedPreferenceUtil.getData(DRIVER_CTA_LABEL_CHECKPOINT_ENTRY,""), event))
    }

    private fun addLeavingForDestinationStateToList(event: String) {
        tripStateList.add(String.format(sharedPreferenceUtil.getData(DRIVER_CTA_LABEL_CHECKPOINT_EXIT,""), event))
    }

    fun getCurrentState(): String? {
        return if (!tripStateList.isNullOrEmpty() && currentStateOfList == null) {
            currentStateOfList = tripStateList[0]
            sharedPreferenceUtil.saveData(DRIVER_CTA_CURRENT_STATE_LABEL, currentStateOfList!!)
            currentStateOfList
        } else if (currentStateOfList != null) {
            sharedPreferenceUtil.saveData(DRIVER_CTA_CURRENT_STATE_LABEL, currentStateOfList!!)
            currentStateOfList
        } else {
            null
        }
    }

    fun getNextState(currentState: String): String? {
        if (tripStateList.contains(currentState)) {
            val currentStateIndex = tripStateList.indexOf(currentState)
            if (currentStateIndex + 1 < tripStateList.size) {
                nextState = tripStateList[currentStateIndex + 1]
                currentStateOfList = nextState!!
            }
        }
        return nextState
    }

    fun setCurrentState(){

    }

    fun setDriverCtaTemplates(driverCtaTemplateResponse: DriverCtaTemplateResponse) {
        driverCtaTemplateResponse.tripStart?.let { tripStartLabel ->
            setLabelsToSharedPref(DRIVER_CTA_LABEL_TRIP_START, tripStartLabel)
        }
        driverCtaTemplateResponse.checkpointEntry?.let { checkointEntryLabel ->
            setLabelsToSharedPref(DRIVER_CTA_LABEL_CHECKPOINT_ENTRY, checkointEntryLabel)
        }
        driverCtaTemplateResponse.activityStart?.let { activityStartLabel ->
            setLabelsToSharedPref(DRIVER_CTA_LABEL_ACTIVITY_START, activityStartLabel)
        }
        driverCtaTemplateResponse.activityEnd?.let { activityEndLabel ->
            setLabelsToSharedPref(DRIVER_CTA_LABEL_ACTIVITY_END, activityEndLabel)
        }
        driverCtaTemplateResponse.checkpointExit?.let { checkpointExitLabel ->
            setLabelsToSharedPref(DRIVER_CTA_LABEL_CHECKPOINT_EXIT, checkpointExitLabel)
        }
        driverCtaTemplateResponse.tripEnd?.let { tripEndLabel ->
            setLabelsToSharedPref(DRIVER_CTA_LABEL_TRIP_END, tripEndLabel)
        }
    }

    private fun setLabelsToSharedPref(labelTitle: String, labelTemplate: String) {
        if (doesTemplateRequiresFiltering(labelTemplate)) {
            val filteredString = formatLabel(labelTemplate)
            sharedPreferenceUtil.saveData(labelTitle, filteredString)
        } else {
            sharedPreferenceUtil.saveData(labelTitle, labelTemplate)
        }
    }

    private fun formatLabel(label: String): String {
        val requiredString = label.substring(label.indexOf("("), label.indexOf(")") + 1)
        return label.replace(requiredString, "%s")
    }

    private fun doesTemplateRequiresFiltering(template: String): Boolean {
        return template.contains("(") && template.contains(")")
    }

}