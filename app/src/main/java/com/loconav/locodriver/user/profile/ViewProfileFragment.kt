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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_view_profile.*
import org.koin.android.ext.android.inject
import java.util.*


class ViewProfileFragment : BaseFragment() {

    val picasso : Picasso by inject()

    val userHttpService : UserHttpService by inject()

    val sharedPreferenceUtil : SharedPreferenceUtil by inject()

    var viewProfileViewModel: ViewProfileViewModel? = null


    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        val actionBar = (activity as AppCompatActivity).supportActionBar as ActionBar
        actionBar.let {
            it.title = "My Profile"
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        viewProfileViewModel = ViewModelProviders.of(this).get(ViewProfileViewModel::class.java)


        button_logout.setOnClickListener {
            AppUtils.logout()
        }

        tv_change_language.setOnClickListener {
            LanguageDialogFragment().show(childFragmentManager, "Language_Dialog")
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

    private fun setData(driver : Driver){
        picasso.load(driver.profilePicture).error(R.drawable.ic_profile).into(iv_profile_picture)
        tv_driver_name.text = driver.name
        tv_transporter_name.text = driver.transporterName
        tv_avg_dist.text = driver.averageDistanceTravelled
        tv_address.text = driver.currentAddressAttributes
        tv_doj.text = Date(driver.dob?:0L).toString()
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