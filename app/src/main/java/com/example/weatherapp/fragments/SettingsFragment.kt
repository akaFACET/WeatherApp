package com.example.weatherapp.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.weatherapp.NightModeType
import com.example.weatherapp.PreferencesManager
import com.example.weatherapp.R
import com.example.weatherapp.UnitsType
import com.example.weatherapp.viewModels.SettingsViewModel
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var preferencesManager: PreferencesManager
    private var nigthModeChooseItem = 0
    private var unitsTypeChooseItem = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("err", "CreateSettingsFragment")
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())

        val savedNightMode = preferencesManager.getSavedNightModeValue()
        nigthModeChooseItem = NightModeType.fromValue(savedNightMode).ordinal
        nigthModSelector_tv.text = NightModeType.fromValue(savedNightMode).title

        val savedUnitsType = preferencesManager.getSavedUnitsValue()
        unitsTypeChooseItem = UnitsType.fromValue(savedUnitsType!!).ordinal
        unitsTypeSelector_tv.text = UnitsType.fromValue(savedUnitsType).title

        nigthModSelector_tv.setOnClickListener{
            showNightModeAlertDialog()
        }

        unitsTypeSelector_tv.setOnClickListener {
            showUnitsTypeAlertDialog()
        }

    }

    private fun showNightModeAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        alertDialog.setTitle("Ночной режим")

        val items = arrayOf(
            NightModeType.MODE_NIGHT_NO.title,
            NightModeType.MODE_NIGHT_YES.title,
            NightModeType.getDefaultMode().title
            )

        alertDialog.setSingleChoiceItems(
            items,
            nigthModeChooseItem,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    val nightMode = NightModeType.fromCustomOrdinal(which)

                    nigthModeChooseItem = nightMode.ordinal

                    nigthModSelector_tv.text = nightMode.title

                    preferencesManager.saveNightModeValue(nightMode.value)

                    AppCompatDelegate.setDefaultNightMode(nightMode.value)

                    dialog!!.cancel()
                }
            })
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }

    private fun showUnitsTypeAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        alertDialog.setTitle("Единицы измерения")

        val items = arrayOf(
            UnitsType.METRIC_UNITS.title,
            UnitsType.IMPERIAL_UNITS.title,
            UnitsType.ABSOLUTE_UNITS.title
        )

        alertDialog.setSingleChoiceItems(
            items,
            unitsTypeChooseItem,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    val unitsType = UnitsType.fromCustomOrdinal(which)

                    unitsTypeChooseItem = unitsType.ordinal

                    unitsTypeSelector_tv.text = unitsType.title

                    preferencesManager.saveUnitsValue(unitsType.value)

                    dialog!!.cancel()
                }
            })
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }


    override fun onDetach() {
        super.onDetach()
        Log.e("err", "DetachSettingsFragment")
    }

}

