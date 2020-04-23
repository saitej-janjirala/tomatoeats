package com.saitejajanjirala.tomatoeats.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.saitejajanjirala.tomatoeats.R


class Profile : Fragment() {

    lateinit var name:TextView
    lateinit var number:TextView
    lateinit var email:TextView
    lateinit var address:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.fragment_profile, container, false)
        name=view.findViewById(R.id.name)
        number=view.findViewById(R.id.number)
        email=view.findViewById(R.id.email)
        address=view.findViewById(R.id.address)
        val sharedpreferences= activity?.getSharedPreferences("user",Context.MODE_PRIVATE)
        name.text=sharedpreferences?.getString("name","")
        number.text=sharedpreferences?.getString("mobile_number","")
        email.text=sharedpreferences?.getString("email","")
        address.text=sharedpreferences?.getString("address","")

        return view
    }


}
