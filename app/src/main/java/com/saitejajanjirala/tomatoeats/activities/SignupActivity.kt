package com.saitejajanjirala.tomatoeats.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
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
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.view.*
import org.json.JSONObject
import java.lang.Exception

class SignupActivity : AppCompatActivity() {
    lateinit var name:EditText
    lateinit var email:EditText
    lateinit var phone:EditText
    lateinit var address:EditText
    lateinit var password:EditText
    lateinit var confirmpassword:EditText
    lateinit var signup:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.title="Registration Page"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        name=findViewById(R.id.name)
        email=findViewById(R.id.email)
        phone=findViewById(R.id.number)
        address=findViewById(R.id.address)
        password=findViewById(R.id.password)
        confirmpassword=findViewById(R.id.confirmpassword)
        signup=findViewById(R.id.signupbutton)
        signup.setOnClickListener{
            if(Connectivity().checkconnectivity(this@SignupActivity)) {
                val tname = name.text.toString()
                val temail = email.text.toString()
                val tphone = phone.text.toString()
                val taddress = address.text.toString()
                val tpassword = password.text.toString()
                val tconfirmpassword = confirmpassword.text.toString()
                if(tname.isEmpty()||temail.isEmpty()||tphone.isEmpty()||taddress.isEmpty()||tpassword.isEmpty()||tconfirmpassword.isEmpty()){
                    Toast.makeText(this@SignupActivity,"Fields shouldn't be empty",Toast.LENGTH_LONG).show()
                }
                else if (tname.length<3 || tpassword.length<6 || tconfirmpassword!=tpassword|| tphone.length!=10 ||temail.length==0){
                    Toast.makeText(this@SignupActivity,"Fields must satisfy the conditions in the hints",Toast.LENGTH_LONG).show()
                }
                else{
                    val queue=Volley.newRequestQueue(this@SignupActivity)
                    val jsonparams=JSONObject()
                    jsonparams.put("name",tname)
                    jsonparams.put("mobile_number",tphone)
                    jsonparams.put("password",tpassword)
                    jsonparams.put("address",taddress)
                    jsonparams.put("email",temail)
                    val url="http://13.235.250.119/v2/register/fetch_result"
                    val jsonrequest=object:JsonObjectRequest(Request.Method.POST,url,jsonparams,Response.Listener<JSONObject>{
                        try {
                            val succesobject:JSONObject=it.getJSONObject("data")
                            Log.i("json","$it")
                            val success = succesobject.getBoolean("success")
                            if (success) {
                                //have to fetch users data after registertation
                                try {
                                    val jsonobject = succesobject.getJSONObject("data")
                                    val user_id = jsonobject.getString("user_id")
                                    val username = jsonobject.getString("name")
                                    val useremail = jsonobject.getString("email")
                                    val usermobile_number = jsonobject.getString("mobile_number")
                                    val useraddress = jsonobject.getString("address")
                                    val sharedprefrences: SharedPreferences =
                                        getSharedPreferences("user", Context.MODE_PRIVATE)
                                    val editor: SharedPreferences.Editor = sharedprefrences.edit()
                                    editor.putString("user_id", user_id)
                                    editor.putString("name", username)
                                    editor.putString("email", useremail)
                                    editor.putString("mobile_number", usermobile_number)
                                    editor.putString("address", useraddress)
                                    editor.apply()
                                    editor.commit()
                                    val intent =
                                        Intent(this@SignupActivity, ContentsActivity::class.java)
                                    intent.putExtra("user_id", user_id)
                                    startActivity(intent)
                                    finish()
                                }
                                catch (e:Exception){
                                    Toast.makeText(this@SignupActivity,e.message,Toast.LENGTH_LONG).show()

                                }
                            } else {
                                Toast.makeText(
                                    this@SignupActivity,
                                    succesobject.getString("errorMessage"),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        catch(e:Exception){
                            Toast.makeText(this@SignupActivity,e.message,Toast.LENGTH_LONG).show()
                        }
                    },
                     Response.ErrorListener {
                         Toast.makeText(this@SignupActivity,"Unable to Register",Toast.LENGTH_LONG).show()
                     }   ){
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "0f768ec1585de2"
                            return headers
                        }
                    }
                    queue.add(jsonrequest)
                }
            }
            else{
                NetworkErrorDialog(this@SignupActivity).createdialog()

            }

        }



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            startActivity(Intent(this@SignupActivity,Loginactivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this@SignupActivity)

    }

}
