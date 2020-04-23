package com.saitejajanjirala.tomatoeats.activities

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.util.Connectivity
import com.saitejajanjirala.tomatoeats.util.NetworkErrorDialog
import org.json.JSONObject

class Enterdetailsforotp : AppCompatActivity() {
    lateinit var number:EditText
    lateinit var email:EditText
    lateinit var next:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enterdetailsforotp)
        number=findViewById(R.id.number)
        email=findViewById(R.id.email)
        next=findViewById(R.id.next)
        next.setOnClickListener{
            val phonenumber=number.text.toString()
            val emailid=email.text.toString()
            if(phonenumber.length!=10 || emailid.length==0){
                Toast.makeText(this@Enterdetailsforotp,"Invalid Credentials",Toast.LENGTH_LONG).show()
            }
            else {
                if (Connectivity().checkconnectivity(this@Enterdetailsforotp)) {
                    val queue = Volley.newRequestQueue(this@Enterdetailsforotp)
                    val url="http://13.235.250.119/v2/forgot_password/fetch_result"
                    val jsonparams=JSONObject()
                    jsonparams.put("mobile_number",phonenumber)
                    jsonparams.put("email",emailid)

                    val jsonrequest=object:JsonObjectRequest(Request.Method.POST,url,jsonparams,Response.Listener<JSONObject>{
                        try {
                            val jsonobject = it.getJSONObject("data")
                            val success = jsonobject.getBoolean("success")
                            Log.d("jsonobject","$jsonobject")
                            if (success) {
                                val alertbox = AlertDialog.Builder(this@Enterdetailsforotp)
                                alertbox.setTitle("Reset Password")
                                alertbox.setMessage("Check the previous email for Otp")
                                alertbox.setCancelable(false)
                                alertbox.setPositiveButton("ok") { text, listener ->
                                    val intent = Intent(
                                        this@Enterdetailsforotp,
                                        EnterotpandResetpassword::class.java
                                    )
                                    intent.putExtra("number", phonenumber)
                                    startActivity(intent)
                                }
                                alertbox.create()
                                alertbox.show()
                            } else {
                                Toast.makeText(
                                    this@Enterdetailsforotp,
                                    jsonobject.getString("errorMessage"),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        catch (e:Exception){
                            Toast.makeText(this@Enterdetailsforotp,e.message.toString(),Toast.LENGTH_LONG).show()

                        }
                    },Response.ErrorListener {
                        Toast.makeText(this@Enterdetailsforotp,it.message.toString(),Toast.LENGTH_LONG).show()
                    }){
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "0f768ec1585de2"
                            return headers
                        }
                    }
                    queue.add(jsonrequest)
                } else {
                    NetworkErrorDialog(this@Enterdetailsforotp).createdialog()
                }
            }
        }
    }

    override fun onBackPressed() {

    }
}
