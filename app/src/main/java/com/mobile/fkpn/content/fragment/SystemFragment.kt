package com.mobile.fkpn.content.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mobile.fkpn.MainActivity
import com.mobile.fkpn.R
import com.mobile.fkpn.RegisterActivity
import com.mobile.fkpn.TokenActivity
import com.mobile.fkpn.content.menu.*
import com.mobile.fkpn.content.menu.bonus.BonusListActivity
import com.mobile.fkpn.content.menu.deposit.DepositListActivity
import com.mobile.fkpn.content.menu.ticket.TicketListActivity
import com.mobile.fkpn.content.menu.vocerPoint.VocerPointListActivity
import com.mobile.fkpn.controller.BalanceController
import com.mobile.fkpn.controller.ProfileController
import com.mobile.fkpn.model.LoadingFragment
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class SystemFragment : Fragment() {

    private lateinit var token: Token
    private lateinit var loadingFragment: LoadingFragment
    private lateinit var name: TextView
    private lateinit var ticket: TextView
    private lateinit var vocerPoint: TextView
    private lateinit var deposit: TextView
    private lateinit var bonus: TextView
    private lateinit var packageJoin: ImageButton
    private lateinit var binary: ImageButton
    private lateinit var depositButton: ImageButton
    private lateinit var ticketButton: ImageButton
    private lateinit var withdraw: ImageButton
    private lateinit var refresh: ImageButton
    private lateinit var register: ImageButton
    private lateinit var ticketLinearLayout: LinearLayout
    private lateinit var vocerPointLinearLayout: LinearLayout
    private lateinit var bonusLinearLayout: LinearLayout
    private lateinit var depositLinearLayout: LinearLayout
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent
    private var premium = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_system, container, false)

        name = view.findViewById(R.id.nameTextView)
        ticket = view.findViewById(R.id.ticketTextView)
        vocerPoint = view.findViewById(R.id.vocerPointTextView)
        deposit = view.findViewById(R.id.depositTextView)
        bonus = view.findViewById(R.id.bonusTextView)
        packageJoin = view.findViewById(R.id.packageJoinButton)
        binary = view.findViewById(R.id.binaryButton)
        depositButton = view.findViewById(R.id.depositButton)
        withdraw = view.findViewById(R.id.withdrawButton)
        refresh = view.findViewById(R.id.refreshButton)
        register = view.findViewById(R.id.registerButton)
        ticketButton = view.findViewById(R.id.ticketButton)

        //linear layout
        ticketLinearLayout = view.findViewById(R.id.ticketLinearLayout)
        vocerPointLinearLayout = view.findViewById(R.id.vocerPointLinearLayout)
        bonusLinearLayout = view.findViewById(R.id.bonusLinearLayout)
        bonusLinearLayout = view.findViewById(R.id.bonusLinearLayout)
        depositLinearLayout = view.findViewById(R.id.depositListLinearLayout)

        token = Token(view.context)
        loadingFragment = LoadingFragment(this.requireActivity())

        packageJoin.setOnClickListener {
            if (premium == 0) {
                Toast.makeText(activity, "Anda belum melakukan upload KTP", Toast.LENGTH_SHORT)
                    .show()
            } else {
                goTo = Intent(view.context, PackageJoinActivity::class.java)
                startActivity(goTo)
            }
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
            if (premium == 0) {
                Toast.makeText(activity, "Anda belum melakukan upload KTP", Toast.LENGTH_SHORT)
                    .show()
            } else {
                goTo = Intent(view.context, WithdrawActivity::class.java)
                startActivity(goTo)
            }
        }

        refresh.setOnClickListener {
            setBalance()
        }

        register.setOnClickListener {
            goTo = Intent(view.context, RegisterActivity::class.java)
            startActivity(goTo)
        }

        ticketButton.setOnClickListener {
            goTo = Intent(view.context, TicketActivity::class.java)
            startActivity(goTo)
        }

        //linear layout
        ticketLinearLayout.setOnClickListener {
            goTo = Intent(view.context, TicketListActivity::class.java)
            startActivity(goTo)
        }

        vocerPointLinearLayout.setOnClickListener {
            goTo = Intent(view.context, VocerPointListActivity::class.java)
            startActivity(goTo)
        }

        bonusLinearLayout.setOnClickListener {
            goTo = Intent(view.context, BonusListActivity::class.java)
            startActivity(goTo)
        }

        depositLinearLayout.setOnClickListener {
            goTo = Intent(view.context, DepositListActivity::class.java)
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
                    profile()
                    this.cancel()
                }
            } else if (response["code"] == 426 || response["code"] == 401) {
                activity?.runOnUiThread {
                    goTo = Intent(activity?.applicationContext, MainActivity::class.java)
                    loadingFragment.closeDialog()
                    activity?.finish()
                    this.cancel()
                    startActivity(goTo)
                }
            }
        }
    }

    private fun profile() {
        Timer().schedule(100) {
            response = ProfileController(token.auth).execute().get()
            if (response["code"] == 200) {
                activity?.runOnUiThread {
                    name.text = response.getJSONObject("data")["name"].toString()
                    premium = response.getJSONObject("data")["premium"].toString().toInt()
                    loadingFragment.closeDialog()
                }
            } else if (response["code"] == 426 || response["code"] == 401) {
                activity?.runOnUiThread {
                    goTo = Intent(activity?.applicationContext, MainActivity::class.java)
                    activity?.finish()
                    loadingFragment.closeDialog()
                    startActivity(goTo)
                }
            }
        }
    }
}
