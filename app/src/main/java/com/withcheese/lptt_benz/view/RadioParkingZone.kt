package com.withcheese.lptt_benz.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import butterknife.BindView
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup
import com.inthecheesefactory.thecheeselibrary.view.state.BundleSavedState
import com.withcheese.lptt_benz.R
import java.util.*

/**
 * Created by nuuneoi on 11/16/2014.
 */
class RadioParkingZone : BaseCustomViewGroup {

    @BindView(R.id.rdFirstParkingZone)
    lateinit var rdFirstParkingZone: RadioGroup
    @BindView(R.id.rdSecondParkingZone)
    lateinit var rdSecondParkingZone: RadioGroup
    @BindView(R.id.rdFirstParkingLot)
    lateinit var rdFirstParkingLot: RadioButton
    @BindView(R.id.rdSecondParkingLot)
    lateinit var rdSecondParkingLot: RadioButton
    @BindView(R.id.rdThirdParkingLot)
    lateinit var rdThirdParkingLot: RadioButton
    @BindView(R.id.rdForthParkingLot)
    lateinit var rdForthParkingLot: RadioButton
    @BindView(R.id.rdFifthParkingLot)
    lateinit var rdFifthParkingLot: RadioButton
    @BindView(R.id.rdSixthParkingLot)
    lateinit var rdSixthParkingLot: RadioButton

    lateinit var areaTitle: String

    val parkingAreaTag: List<String>
        get() {
            val ParkingZoneTag = ArrayList<String>()
            ParkingZoneTag.add(rdFirstParkingZone.tag.toString())
            ParkingZoneTag.add(rdSecondParkingZone.tag.toString())
            return ParkingZoneTag
        }
    /*
    public int getChildCount(RadioGroup FirstParkingZone, RadioGroup SecondParkingzone) {
        return super.getChildCount();
    }
    */

    val parkingLot: List<RadioButton>
        get() {
            val parkingLots = ArrayList<RadioButton>()
            parkingLots.add(rdFirstParkingLot)
            parkingLots.add(rdSecondParkingLot)
            parkingLots.add(rdThirdParkingLot)
            parkingLots.add(rdForthParkingLot)
            parkingLots.add(rdFifthParkingLot)
            parkingLots.add(rdSixthParkingLot)
            return parkingLots
        }

    val parkingZone: List<RadioGroup>
        get() {
            val parkingZones = ArrayList<RadioGroup>()
            parkingZones.add(rdFirstParkingZone)
            parkingZones.add(rdSecondParkingZone)
            return parkingZones
        }

    val checkedButton: Boolean
        get() = rdFirstParkingZone.checkedRadioButtonId != -1

    constructor(context: Context) : super(context) {
        initInflate()
        initInstances()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, defStyleAttr, 0)
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, defStyleAttr, defStyleRes)
    }

    private fun initInflate() {
        View.inflate(context, R.layout.rdgroup_parking_zone, this)
    }

    private fun initInstances() {
        // findViewById here
        /*--First Parking Zone--*/
        rdFirstParkingZone = findViewById(R.id.rdFirstParkingZone)
        rdFirstParkingLot = rdFirstParkingZone.findViewById(R.id.rdFirstParkingLot)
        rdThirdParkingLot = rdFirstParkingZone.findViewById(R.id.rdThirdParkingLot)
        rdFifthParkingLot = rdFirstParkingZone.findViewById(R.id.rdFifthParkingLot)
        /*--Second Parking Zone--*/
        rdSecondParkingZone = findViewById(R.id.rdSecondParkingZone)
        rdSixthParkingLot = rdSecondParkingZone.findViewById(R.id.rdSixthParkingLot)
        rdForthParkingLot = rdSecondParkingZone.findViewById(R.id.rdForthParkingLot)
        rdSecondParkingLot = rdSecondParkingZone.findViewById(R.id.rdSecondParkingLot)
    }

    private fun initWithAttrs(attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()

// Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return BundleSavedState(superState)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as BundleSavedState
        super.onRestoreInstanceState(ss.superState)

        val bundle = ss.bundle
        // Restore State from bundle here
    }

    fun setParkingLotTitles(title: String) {
        val FirstTitle = title + "1"
        val SecondTitle = title + "2"
        val ThirdTitle = title + "3"
        val ForthTitle = title + "4"
        val FifthTitle = title + "5"
        val SixthTitle = title + "6"
        rdFirstParkingLot.text = FirstTitle
        rdSecondParkingLot.text = SecondTitle
        rdThirdParkingLot.text = ThirdTitle
        rdForthParkingLot.text = ForthTitle
        rdFifthParkingLot.text = FifthTitle
        rdSixthParkingLot.text = SixthTitle
    }

    fun setParkingAreaTag(areaTag: String) {
        val firstZoneTag = "First$areaTag"
        val secondZoneTag = "Second$areaTag"
        rdFirstParkingZone.tag = firstZoneTag
        rdSecondParkingZone.tag = secondZoneTag
    }

    fun clearChecked() {
        rdFirstParkingZone.clearCheck()
        rdSecondParkingZone.clearCheck()
    }

    fun clearFirstZone(f: Boolean) {
        if (f)
            rdFirstParkingZone.clearCheck()
        else
            rdSecondParkingZone.clearCheck()
    }

    fun isFirstZoneChecked(isChecked: Boolean): RadioGroup {
        return if (isChecked) {
            rdFirstParkingZone
        } else
            rdSecondParkingZone
    }

    fun isSecondZoneChecked(isChecked: Boolean): RadioGroup {
        return if (isChecked) {
            rdSecondParkingZone
        } else
            rdFirstParkingZone
    }

    fun clearOtherZoneChecked(selectedParkingZone: RadioGroup) {
        selectedParkingZone.clearCheck()
    }

    private fun clearFirstParkingZoneChecked() {
        rdFirstParkingZone.clearCheck()
    }

    private fun clearSecondParkingZoneChecked() {
        rdSecondParkingZone.clearCheck()
    }

    override fun getId(): Int {
        return super.getId()
    }
}
