package com.mobile.fkpn.content.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mobile.fkpn.R
import com.mobile.fkpn.TokenActivity
import com.mobile.fkpn.content.menu.*
import com.mobile.fkpn.controller.BalanceController
import com.mobile.fkpn.model.LoadingFragment
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class SystemFragment : Fragment() {

    private lateinit var token: Token
    private lateinit var loadingFragment: LoadingFragment
    private lateinit var ticket: TextView
    private lateinit var vocerPoint: TextView
    private lateinit var deposit: TextView
    private lateinit var bonus: TextView
    private lateinit var packageJoin: Button
    private lateinit var binary: Button
    private lateinit var depositButton: Button
    private lateinit var withdraw: Button
    private lateinit var profileButton: Button
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_system, container, false)

        ticket = view.findViewById(R.id.ticketTextView)
        vocerPoint = view.findViewById(R.id.vocerPointTextView)
        deposit = view.findViewById(R.id.depositTextView)
        bonus = view.findViewById(R.id.bonusTextView)
        packageJoin = view.findViewById(R.id.packageJoinButton)
        binary = view.findViewById(R.id.binaryButton)
        depositButton = view.findViewById(R.id.depositButton)
        withdraw = view.findViewById(R.id.withdrawButton)
        profileButton = view.findViewById(R.id.profileButton)

        token = Token(view.context)
        loadingFragment = LoadingFragment(this.requireActivity())

        packageJoin.setOnClickListener {
            goTo = Intent(view.context, PackageJoinActivity::class.java)
            startActivity(goTo)
        }

        binary.setOnClickListener {
            goTo = Intent(view.context, BinaryActivity::class.java)
            startActivity(goTo)
        }

        depositButton.setOnClickListener {
            goTo = Intent(view.context, DepositActivity::class.java)
            startActivity(goTo)
        }

        withdraw.setOnClickListener {
            goTo = Intent(view.context, WithdrawActivity::class.java)
            startActivity(goTo)
        }

        profileButton.setOnClickListener {
            goTo = Intent(view.context, ProfileActivity::class.java)
            startActivity(goTo)
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        setBalance()
    }

    private fun setBalance() {
        loadingFragment.openDialog()
        Timer().schedule(1000, 1000) {
            response = BalanceController(token.auth).execute().get()
            if (response["code"] == 200) {
                activity?.runOnUiThread {
                    ticket.text = response["ticket"].toString()
                    vocerPoint.text = response["vocerPoint"].toString()
                    deposit.text = response["deposit"].toString()
                    bonus.text = response["bonus"].toString()
                    loadingFragment.closeDialog()
                    this.cancel()
                }
            } else if (response["code"] == 426 || response["code"] == 401) {
                activity?.runOnUiThread {
                    goTo = Intent(activity?.applicationContext, TokenActivity::class.java)
                    loadingFragment.closeDialog()
                    activity?.finish()
                    this.cancel()
                    startActivity(goTo)
                }
            }
        }
    }
}
