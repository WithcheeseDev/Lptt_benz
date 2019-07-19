package com.withcheese.lptt_benz.util

import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.fragment.*

class LocationUtils {
    private lateinit var listLocationID: List<String>
    private val hashLocationID = HashMap<String, String>()
    private val hashMainLocationID = HashMap<String, String>()
    private val hashLocationName = HashMap<String, String>()
    private val hashMainLocationName = HashMap<String, String>()
    private val hashLocationCode = HashMap<String, String>()
    private val hashEntLocationFragment = HashMap<String, Fragment>()
    private val hashExitLocationFragment = HashMap<String, Fragment>()
    private lateinit var hashMainProductionLine: HashMap<String, String>
    private lateinit var hashEntranceProductionLine: HashMap<String, String>
    private lateinit var hashExitProductionLine: HashMap<String, String>
    // RETURN LIST'S LOCATION .
    fun listLocationID(): List<String> {
        listLocationID = listOf(
                "WA-1", "WA-2", "WA-3", "WA-4",
                "03-R", "02-R", "EL-1", "EL-2",
                "SW-1", "SW-2", "FL-1", "FL-2",
                "KA", "FP", "PAVOCA")
        return listLocationID
    }

    fun hashMainProductionLine(): HashMap<String, String> {
        hashMainProductionLine = hashMapOf(
                "WA-1" to "Wheel Alignment L1", "WA-2" to "Wheel Alignment L2",
                "WA-3" to "Wheel Alignment L3", "WA-4" to "Wheel Alignment L4",
                "03-R" to "Mech/EE Rect.", "02-R" to "Paint Rect.",
                "EL-1" to "End of Line L1", "EL-2" to "End of Line L2",
                "SW-1" to "Shower Test L1", "SW-2" to "Shower Test L2",
                "FL-1" to "Finish Line L1", "FL-2" to "Finish Line L2"
        )
        return hashMainProductionLine
    }

    fun hashEntranceProductionLine(): HashMap<String, String> {
        hashEntranceProductionLine = hashMapOf(
                "WA-1" to "WA-Entrance L1", "WA-2" to "WA-Entrance L2", "WA-3" to "WA-Entrance L3", "WA-4" to "WA-Entrance L4",
                "03-R" to "Mech/EE Rect.", "02-R" to "Paint Rect.",
                "EL-1" to "End of Line-Entrance L1", "EL-2" to "End of Line-Entrance L2",
                "SW-1" to "Shower Test-Entrance L1", "SW-2" to "Shower Test-Entrance L2",
                "FL-1" to "Finish Line-Entrance L1", "FL-2" to "Finish Line-Entrance L2"
        )
        return hashEntranceProductionLine
    }

    fun hashExitProductionLine(): HashMap<String, String> {
        hashExitProductionLine = hashMapOf(
                "WA-1" to "WA-Exit L1", "WA-2" to "WA-Exit L2", "WA-3" to "WA-Exit L3", "WA-4" to "WA-Exit L4",
                "03-R" to "Mech/EE Rect.", "02-R" to "Paint Rect.",
                "EL-1" to "End of Line-Exit L1", "EL-2" to "End of Line-Exit L2",
                "SW-1" to "Shower Test-Exit L1", "SW-2" to "Shower Test-Exit L2",
                "FL-1" to "Finish Line-Exit L1", "FL-2" to "Finish Line-Exit L2"
        )
        return hashExitProductionLine
    }

    // RETURN COLLECTION OF ENTRANCE LOCATION FRAGMENT
    fun hashEntranceLocationFragment(): HashMap<String, Fragment> {
        hashEntLocationFragment["WA-1"] = WAEntranceStateFragment.newInstance()
        hashEntLocationFragment["WA-2"] = WAEntranceStateFragment.newInstance()
        hashEntLocationFragment["WA-3"] = WAEntranceStateFragment.newInstance()
        hashEntLocationFragment["WA-4"] = WAEntranceStateFragment.newInstance()
        hashEntLocationFragment["03-R"] = RectificationFragment.newInstance()
        hashEntLocationFragment["02-R"] = RectificationFragment.newInstance()
        hashEntLocationFragment["EL-1"] = EOLEntranceFragment.newInstance()
        hashEntLocationFragment["EL-2"] = EOLEntranceFragment.newInstance()
        hashEntLocationFragment["SW-1"] = SWEntranceFragment.newInstance()
        hashEntLocationFragment["SW-2"] = SWEntranceFragment.newInstance()
        hashEntLocationFragment["FL-1"] = FLEntFragment.newInstance()
        hashEntLocationFragment["FL-2"] = FLEntFragment.newInstance()
        return hashEntLocationFragment
    }

    // RETURN COLLECTION OF EXIT LOCATION FRAGMENT .
    fun hashExitLocationFragment(): HashMap<String, Fragment> {
        hashExitLocationFragment["WA-1"] = WAExitStateFragment.newInstance()
        hashExitLocationFragment["WA-2"] = WAExitStateFragment.newInstance()
        hashExitLocationFragment["WA-3"] = WAExitStateFragment.newInstance()
        hashExitLocationFragment["WA-4"] = WAExitStateFragment.newInstance()
        hashExitLocationFragment["03-R"] = RectificationFragment.newInstance()
        hashExitLocationFragment["02-R"] = RectificationFragment.newInstance()
        hashExitLocationFragment["EL-1"] = EOLExitFragment.newInstance()
        hashExitLocationFragment["EL-2"] = EOLExitFragment.newInstance()
        hashExitLocationFragment["SW-1"] = SWExitFragment.newInstance()
        hashExitLocationFragment["SW-2"] = SWExitFragment.newInstance()
        hashExitLocationFragment["FL-1"] = FLExitFragment.newInstance()
        hashExitLocationFragment["FL-2"] = FLExitFragment.newInstance()
        return hashExitLocationFragment
    }

    // RETURN COLLECTION OF LOCATION'S ID .
    fun hashLocationID(): HashMap<String, String> {
        hashLocationID["WA-1"] = "WA-1"
        hashLocationID["WA-2"] = "WA-2"
        hashLocationID["WA-3"] = "WA-3"
        hashLocationID["WA-4"] = "WA-4"
        hashLocationID["03-R"] = "03-R"
        hashLocationID["02-R"] = "02-R"
        hashLocationID["EL-1"] = "EL-1"
        hashLocationID["EL-2"] = "EL-2"
        hashLocationID["SW-1"] = "SW-1"
        hashLocationID["SW-2"] = "SW-2"
        hashLocationID["FL-1"] = "FL-1"
        hashLocationID["FL-2"] = "FL-2"
        hashLocationID["KA"] = "KA"
        hashLocationID["FP"] = "FP"
        hashLocationID["PAVOCA"] = "PAVOCA"
        return hashLocationID
    }

    // RETURN COLLECTION OF MAIN LOCATION'S ID .
    fun hashMainLocationID(): HashMap<String, String> {
        hashMainLocationID["WA-1"] = "WA"
        hashMainLocationID["WA-2"] = "WA"
        hashMainLocationID["WA-3"] = "WA"
        hashMainLocationID["WA-4"] = "WA"
        hashMainLocationID["03-R"] = "RT"
        hashMainLocationID["02-R"] = "RT"
        hashMainLocationID["EL-1"] = "EL"
        hashMainLocationID["EL-2"] = "EL"
        hashMainLocationID["SW-1"] = "SW"
        hashMainLocationID["SW-2"] = "SW"
        hashMainLocationID["FL-1"] = "FL"
        hashMainLocationID["FL-2"] = "FL"
        hashMainLocationID["KA"] = "KA"
        hashMainLocationID["FP"] = "FP"
        hashMainLocationID["PAVOCA"] = "PAVOCA"
        return hashMainLocationID
    }

    // RETURN COLLECTION OF LOCATION'S NAME .
    fun hashLocationName(): HashMap<String, String> {
        hashLocationName["WA-1"] = "Wheel Alignment"
        hashLocationName["WA-2"] = "Wheel Alignment"
        hashLocationName["WA-3"] = "Wheel Alignment"
        hashLocationName["WA-4"] = "Wheel Alignment"
        hashLocationName["03-R"] = "Mech/EE rect"
        hashLocationName["02-R"] = "Paint rect"
        hashLocationName["EL-1"] = "End of Line"
        hashLocationName["EL-2"] = "End of Line"
        hashLocationName["SW-1"] = "Shower test"
        hashLocationName["SW-2"] = "Shower test"
        hashLocationName["FL-1"] = "Finish Line"
        hashLocationName["FL-2"] = "Finish Line"
        hashLocationName["KA"] = "Keeping Area"
        hashLocationName["FP"] = "Finish Parking"
        hashLocationName["PAVOCA"] = "PAVOCA"
        return hashLocationName
    }

    fun hashMainLocationName(): HashMap<String, String> {
        hashMainLocationName["WA-1"] = "Wheel Alignment"
        hashMainLocationName["WA-2"] = "Wheel Alignment"
        hashMainLocationName["WA-3"] = "Wheel Alignment"
        hashMainLocationName["WA-4"] = "Wheel Alignment"
        hashMainLocationName["03-R"] = "Rectification"
        hashMainLocationName["02-R"] = "Rectification"
        hashMainLocationName["EL-1"] = "End of Line"
        hashMainLocationName["EL-2"] = "End of Line"
        hashMainLocationName["SW-1"] = "Shower test"
        hashMainLocationName["SW-2"] = "Shower test"
        hashMainLocationName["FL-1"] = "Finish Line"
        hashMainLocationName["FL-2"] = "Finish Line"
        hashMainLocationName["KA"] = "Keeping Area"
        hashMainLocationName["FP"] = "Finish Parking"
        hashMainLocationName["PAVOCA"] = "PAVOCA"
        return hashMainLocationName
    }

    // RETURN COLLECTION OF LOCATION'S CODE .
    fun hashLocationCode(): HashMap<String, String> {
        hashLocationCode["WA-1"] = "01:01"
        hashLocationCode["WA-2"] = "01:02"
        hashLocationCode["WA-3"] = "01:03"
        hashLocationCode["WA-4"] = "01:04"
        hashLocationCode["03-R"] = "02:01"
        hashLocationCode["02-R"] = "02:02"
        hashLocationCode["EL-1"] = "03:01"
        hashLocationCode["EL-2"] = "03:02"
        hashLocationCode["SW-1"] = "04:01"
        hashLocationCode["SW-2"] = "04:02"
        hashLocationCode["FL-1"] = "05:01"
        hashLocationCode["FL-2"] = "05:02"
        return hashLocationCode
    }



    companion object {

        private var instance: LocationUtils? = null

        fun instance(): LocationUtils {
            if (instance == null)
                instance = LocationUtils()
            return instance as LocationUtils
        }
    }
}
