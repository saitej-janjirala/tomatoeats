package com.saitejajanjirala.tomatoeats.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.saitejajanjirala.tomatoeats.R
import com.saitejajanjirala.tomatoeats.util.Connectivity
import com.saitejajanjirala.tomatoeats.util.NetworkErrorDialog
import org.json.JSONObject

class Loginactivity : AppCompatActivity() {
    lateinit var mobilenumber:EditText
    lateinit var password:EditText
    lateinit var login:Button
    lateinit var forgotpassword:TextView
    lateinit var signup:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginactivity)
        mobilenumber=findViewById(R.id.loginnumber)
        password=findViewById(R.id.password)
        login=findViewById(R.id.loginbutton)
        forgotpassword=findViewById(R.id.forgotpassword)
        signup=findViewById(R.id.signup)
        signup.setOnClickListener {
            startActivity(Intent(this@Loginactivity,SignupActivity::class.java))
        }
        forgotpassword.setOnClickListener {
            startActivity(Intent(this@Loginactivity,Enterdetailsforotp::class.java))
        }
        login.setOnClickListener{
            if(mobilenumber.text.toString().length!=10||password.text.toString().length<6 || password.text.isEmpty()) {
                mobilenumber.setError(resources.getString(R.string.mobilenumber))
                password.setError(resources.getString(R.string.password))
            }
            else{
                mobilenumber.setError(null)
                password.setError(null)
                if(Connectivity().checkconnectivity(this@Loginactivity)){
                    val queue=Volley.newRequestQueue(this@Loginactivity)
                    val url="http://13.235.250.119/v2/login/fetch_result"
                    val jsonparams=JSONObject()
                    jsonparams.put("mobile_number",mobilenumber.text.toString())
                    jsonparams.put("password",password.text.toString())
                    val jsonrequest=object :JsonObjectRequest(Request.Method.POST,url,jsonparams,Response.Listener<JSONObject>{
                        try{
                            Log.i("json","$it")
                            val succesjsonobject:JSONObject=it.getJSONObject("data")
                            val success=succesjsonobject.getBoolean("success")
                            if(success){
                                //have to fetch users data after registertation
                                val jsonobject=succesjsonobject.getJSONObject("data")
                                val user_id=jsonobject.getString("user_id")
                                val username=jsonobject.getString("name")
                                val useremail=jsonobject.getString("email")
                                val usermobile_number=jsonobject.getString("mobile_number")
                                val useraddress=jsonobject.getString("address")
                                val sharedprefrences: SharedPreferences =getSharedPreferences("user", Context.MODE_PRIVATE)
                                val editor: SharedPreferences.Editor=sharedprefrences.edit()
                                editor.putString("user_id",user_id)
                                editor.putString("name",username)
                                editor.putString("email",useremail)
                                editor.putString("mobile_number",usermobile_number)
                                editor.putString("address",useraddress)
                                editor.apply()
                                editor.commit()
                                val intent=Intent(this@Loginactivity,ContentsActivity::class.java)
                                intent.putExtra("user_id",user_id)
                                startActivity(intent)
                                finish()

                            }
                            else{
                                Toast.makeText(this@Loginactivity,succesjsonobject.getString("errorMessage"),Toast.LENGTH_SHORT).show()
                            }
                        }
                        catch (e:Exception){
                            Toast.makeText(this@Loginactivity,"Some unexpected error occured ",Toast.LENGTH_SHORT).show()
                            Log.d("jsonerror",e.message.toString())
                        }
                    },Response.ErrorListener {

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
                    NetworkErrorDialog(this@Loginactivity).createdialog()

                }
            }
        }
    }

    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this@Loginactivity)
    }
}
