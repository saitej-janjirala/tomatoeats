package com.saitejajanjirala.tomatoeats.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class EnterotpandResetpassword : AppCompatActivity() {
    lateinit var otp:EditText
    lateinit var password:EditText
    lateinit var confirmpassword:EditText
    lateinit var submit:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enterotpand_resetpassword)
        val number=intent?.getStringExtra("number")
        otp=findViewById(R.id.otp)
        password=findViewById(R.id.password)
        confirmpassword=findViewById(R.id.confirmpassword)
        submit=findViewById(R.id.submitbutton)
        submit.setOnClickListener {
            val otptext=otp.text.toString()
            val pwd=password.text.toString()
            val cpwd=confirmpassword.text.toString()
            if(otptext.length==4){
                otp.setError(null)
                if(pwd==cpwd && pwd.length>=6 && cpwd.length>=6){
                    password.setError(null)
                    confirmpassword.setError(null)
                    val queue=Volley.newRequestQueue(this@EnterotpandResetpassword)
                    if(Connectivity().checkconnectivity(this@EnterotpandResetpassword)) {
                        val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                        val jsonparams=JSONObject()
                        jsonparams.put("mobile_number",number)
                        jsonparams.put("password",pwd)
                        jsonparams.put("otp",otptext)
                        val jsonrequest=object:JsonObjectRequest(Request.Method.POST,url,jsonparams,Response.Listener<JSONObject>{
                            val jsonObject=it.getJSONObject("data")
                            val success=jsonObject.getBoolean("success")
                            if(success){
                                Toast.makeText(this@EnterotpandResetpassword,jsonObject.getString("successMessage"),Toast.LENGTH_LONG).show()
                                startActivity(Intent(this@EnterotpandResetpassword,Loginactivity::class.java))
                            }
                            else{
                                Toast.makeText(this@EnterotpandResetpassword,jsonObject.getString("errorMessage"),Toast.LENGTH_LONG).show()
                            }
                        },Response.ErrorListener {
                            Toast.makeText(this@EnterotpandResetpassword,it.message.toString(),Toast.LENGTH_LONG).show()
                        }){
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "0f768ec1585de2"
                                return headers
                            }
                        }
                        queue.add(jsonrequest)
                    }
                    else{
                        NetworkErrorDialog(this@EnterotpandResetpassword).createdialog()
                    }

                }
                else{
                    val err="both passwords should be equal and must be greater than 6 characters"
                    password.setError(err)
                    confirmpassword.setError(err)
                }
            }
            else{
                otp.setError("otp must be 4 digits")
            }

        }

    }

    override fun onBackPressed() {

    }
}
