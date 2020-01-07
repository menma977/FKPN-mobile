package com.mobile.fkpn.content.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mobile.fkpn.LoginActivity
import com.mobile.fkpn.MainActivity
import com.mobile.fkpn.R
import com.mobile.fkpn.TokenActivity
import com.mobile.fkpn.content.profile.ProfileActivity
import com.mobile.fkpn.content.profile.password.PasswordActivity
import com.mobile.fkpn.controller.ImageGeneratorController
import com.mobile.fkpn.controller.LogoutController
import com.mobile.fkpn.controller.ProfileController
import com.mobile.fkpn.model.LoadingFragment
import com.mobile.fkpn.model.Token
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class ProfileFragment : Fragment() {

    private lateinit var token: Token
    private lateinit var loadingFragment: LoadingFragment
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent
    private lateinit var imageGeneratorController: ImageGeneratorController
    private lateinit var profileImage: CircleImageView
    private lateinit var name: TextView
    private lateinit var phone: TextView
    private lateinit var alertKTP: TextView
    private lateinit var logout: Button
    private lateinit var password: Button
    private lateinit var editProfile: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileImage = view.findViewById(R.id.profileImage)
        name = view.findViewById(R.id.nameTextView)
        phone = view.findViewById(R.id.phoneTextView)
        alertKTP = view.findViewById(R.id.alertTextView)
        logout = view.findViewById(R.id.logoutButton)
        password = view.findViewById(R.id.passwordButton)
        editProfile = view.findViewById(R.id.updateProfileButton)

        token = Token(view.context)
        loadingFragment = LoadingFragment(this.requireActivity())

        logout.setOnClickListener {
            Timer().schedule(100) {
                response = LogoutController(token.auth).execute().get()
                token.clear()
                activity?.runOnUiThread {
                    goTo = Intent(activity, LoginActivity::class.java)
                    activity?.finish()
                    startActivity(goTo)
                }
            }
        }

        password.setOnClickListener {
            goTo = Intent(activity, PasswordActivity::class.java)
            startActivity(goTo)
        }

        editProfile.setOnClickListener {
            goTo = Intent(activity, ProfileActivity::class.java)
            startActivity(goTo)
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        profile()
    }

    private fun changeProfileImage(urlImage: String) {
        if (urlImage.isNotEmpty()) {
            imageGeneratorController = ImageGeneratorController(urlImage)
            val gitBitmap = imageGeneratorController.execute().get()
            profileImage.setImageBitmap(gitBitmap)
        }
        loadingFragment.closeDialog()
    }

    private fun profile() {
        loadingFragment.openDialog()
        Timer().schedule(100) {
            response = ProfileController(token.auth).execute().get()
            if (response["code"] == 200) {
                activity?.runOnUiThread {
                    name.text = response.getJSONObject("data")["name"].toString()
                    phone.text = response.getJSONObject("data")["phone"].toString()
                    changeProfileImage(response.getJSONObject("data")["image"].toString())
                }
            } else {
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
