package com.loconav.locodriver.user.profile

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.loconav.locodriver.AppUtils
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.SHARED_PREFERENCE.Companion.DRIVER_ID
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragment
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.driver.model.Driver
import com.loconav.locodriver.language.LanguageDialogFragment
import com.loconav.locodriver.user.UserHttpService
import com.loconav.locodriver.util.TimeUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_view_profile.*
import org.koin.android.ext.android.inject
import java.util.*


class ViewProfileFragment : BaseFragment() {

    val picasso : Picasso by inject()

    val userHttpService : UserHttpService by inject()

    val sharedPreferenceUtil : SharedPreferenceUtil by inject()

    var viewProfileViewModel: ViewProfileViewModel? = null

    private val LANGUAGE_DIALOG_TAG = "Language_Dialog"


    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        val actionBar = (activity as AppCompatActivity).supportActionBar as ActionBar
        actionBar.let {
            it.title = Constants.ViewProfileScreen.VIEW_PROFILE_TOOLBAR_TITLE
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        viewProfileViewModel = ViewModelProviders.of(this).get(ViewProfileViewModel::class.java)


        button_logout.setOnClickListener {
            AppUtils.logout()
        }

        tv_change_language.setOnClickListener {
            LanguageDialogFragment().show(childFragmentManager, LANGUAGE_DIALOG_TAG)
        }

        progressBar.visibility = VISIBLE

        viewProfileViewModel?.getDriverData(sharedPreferenceUtil.getData(DRIVER_ID, 0L))?.observe(this, Observer{dataWrapper ->
            dataWrapper.data?.let {userDataResponse ->
                sharedPreferenceUtil.saveData(Constants.SHARED_PREFERENCE.AUTH_TOKEN, userDataResponse.authenticationToken?:"")
                sharedPreferenceUtil.saveData(DRIVER_ID, userDataResponse.id?:0L)
                sharedPreferenceUtil.saveData(Constants.SHARED_PREFERENCE.PHOTO_LINK, userDataResponse.profilePicture?:"")
                setData(userDataResponse)
                progressBar.visibility = GONE
            } ?: run{
                progressBar.visibility = View.GONE
                Toast.makeText(context, dataWrapper.throwable?.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setData(driver: Driver) {
        driver.profilePicture?.let {
            picasso.load(it).into(iv_profile_picture)
        } ?: run { iv_profile_picture.setImageResource(R.drawable.ic_user_placeholder) }

        tv_driver_name.text = driver.name

        driver.transporterName?.let {
            tv_transporter_name.text = it
        } ?: run { tv_transporter_name.text = getString(R.string.no_transporter_text) }

        driver.currentMonthlyIncome?.let {
            tv_current_salary.text = String.format(getString(R.string.rupee),it)
        } ?: run { tv_current_salary.text = getString(R.string.no_monthly_income_text) }

        driver.currentAddressAttributes?.let {
            tv_address.text = String.format("%s,%s,%s,%s,%s"
                ,it.houseNumber
                ,it.addressLine1
                ,it.addressLine2
                ,it.addressLine3
                ,it.city)
        }?:run { tv_address.text = getString(R.string.no_address_present) }

        tv_doj.text = TimeUtils.getThFormatTime(driver.dateOfJoining ?: 0L)

        driver.vehicleNumber?.let {
            vehicle_number_value_tv.text = it
        } ?: run {
            vehicle_number_value_tv.text = getString(R.string.no_vehicle_assigned_text)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_view_profile
    }

    companion object {
        fun instance() : ViewProfileFragment {
            return ViewProfileFragment()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
            return true
        }
        return false
    }

}