package com.loconav.locodriver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.loconav.locodriver.application.LocoDriverApplication

class MainActivity : AppCompatActivity() {



    var agent: ApolloCall<AgentProfileQuery.Data>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val profileMut = AgentProfileQuery.builder().build()

        agent = LocoDriverApplication.instance.apolloClient.query(profileMut)

        agent?.enqueue(object : ApolloCall.Callback<AgentProfileQuery.Data>() {

            override fun onFailure(e: ApolloException) {
                Log.e("failure", e.message)
            }

            override fun onResponse(response: Response<AgentProfileQuery.Data>) {
                val agent = response.data()?.agent()
                agent?.let { agentNotnull ->
                    Log.i("driver info ", agentNotnull.firstName)
                }
            }
        })

    }
}
