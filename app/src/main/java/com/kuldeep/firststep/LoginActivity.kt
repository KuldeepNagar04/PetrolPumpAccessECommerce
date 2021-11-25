package com.kuldeep.firststep


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity() {
    //Creating variable firebase Authentication
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN: Int = 1

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        //Checking weather user is already logged-in
        var currentUser = auth.currentUser
        if(currentUser != null) {
            intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("logInWithNo",true)
            startActivity(intent)
            finish()
        }

        //Calling function for Get OTP button
        btnLogin.setOnClickListener{
            login(phoneNumber.text.toString())
        }

        //Calling function for click on Resend
        tvResend.setOnClickListener{
            resendOtp(phoneNumber.text.toString())
        }

        //Calling function for click on Continue Button
        btnconti.setOnClickListener {
            verifyManually(storedVerificationId)
        }

        //sign in with google
        sign_in_button.setOnClickListener {
            Log.d("Message" , "1")
            signIn()
        }


        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                phoneNumberLayout.setVisibility(View.VISIBLE)
                btnLogin.setVisibility(View.VISIBLE)
                btnconti.setVisibility(View.GONE)
                id_otp.setVisibility(View.GONE)
                tvResend.setVisibility(View.GONE)
                tvv.setVisibility(View.GONE)
                tvPhoneNumber.setVisibility(View.GONE)
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("TAG", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
            }
        }

    }

    //funcationality of Get OTP buttton
    private fun login(mobileNumber: String) {

        var number=mobileNumber.trim().removePrefix("+91")

        if(!number.isEmpty()){
            if (number.length==10){
                number="+91"+number
                sendVerificationcode(number)

                tvPhoneNumber.text = "+91"+phoneNumber.text.toString()
                phoneNumberLayout.setVisibility(View.GONE)
                btnLogin.setVisibility(View.GONE)
                btnconti.setVisibility(View.VISIBLE)
                id_otp.setVisibility(View.VISIBLE)
                tvResend.setVisibility(View.VISIBLE)
                tvv.setVisibility(View.VISIBLE)
                tvPhoneNumber.setVisibility(View.VISIBLE)
                Toast.makeText(this, "OTP Sent", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Enter 10 digit mobile number", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }

    //Sending OTP
    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    //Auto scan OTP or Auto sign in
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //saving data to firebase
                    val currentUser = auth.currentUser
//                    database = FirebaseDatabase.getInstance()
//                    databaseReference = database?.reference!!.child("profile")
//                    val currentUSerDb = databaseReference?.child((currentUser?.uid!!))
//                    currentUSerDb?.child("phoneNumber")?.setValue(phoneNumber.text.toString())

                    //saving data to firestore
                    Log.d("Message" , "565656565656")
                    val phoneNo = hashMapOf(
                        "phone_number" to phoneNumber.text.toString())
                    Log.d("Message" , "2626262626")
                    var db = FirebaseFirestore.getInstance()
                        .collection("profiles")
                        .document(currentUser?.uid!!).get()
                        .addOnSuccessListener {document ->
                            if (document.data == null) {
                                Log.d("gsrtgtrfg","8888888888")
                                Log.d("gsrtgtrfg","8888888888")
                                Log.d(TAG, "DocumentSnapshot data: ${document?.data}")
                                FirebaseFirestore.getInstance().collection("profiles").document(currentUser?.uid!!).set(phoneNo)
                            } else {
                                Log.d(TAG, "Data already present")
                            }

                        }

                    // Sign in success, update UI with the signed-in user's information
                    intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("logInWithNo",true)
                    startActivity(intent)
                    finish()

                } else {
                    // Sign in failed, display a message and update the UI
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    //funcationality of Resend buttton
    private fun resendOtp(mobileNumber: String) {

        var number=mobileNumber.trim()

        if(!number.isEmpty()){
            number="+91"+number
            sendVerificationcode(number)

            findViewById<PinView>(R.id.id_otp).setVisibility(View.VISIBLE)
            findViewById<TextView>(R.id.tvResend).setVisibility(View.VISIBLE)
            Toast.makeText(this, "OTP Re-sent", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }

    //manually verifying otp on click of continue
    private fun verifyManually(storedVerificationId: String) {
        val otpGiven=findViewById<EditText>(R.id.id_otp)
        var otp =otpGiven.text.toString().trim()
        if(!otp.isEmpty()){
            val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                storedVerificationId.toString(), otp
            )
            signInWithPhoneAuthCredential(credential)
        }else{
            Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
        }
    }


    //funcationality of sign in with google button
    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    //handling result got from google signin client
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Message" , "5")
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Message" , "6")
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d("Message" , "7")
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("Message" , "8")
                Log.d("Message" , account.toString())
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    //Authenticting user for seleced gmail
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d("Message", user?.email.toString())
                    Log.d("Message", user?.phoneNumber.toString())
                    intent = Intent(applicationContext, MainActivity::class.java)
                    intent.putExtra("logInWithNo", false)
                    val currentUser = auth.currentUser
                    database = FirebaseDatabase.getInstance()
                    databaseReference = database?.reference!!.child("profile")
                    val currentUSerDb = databaseReference?.child((currentUser?.uid!!))
                    currentUSerDb?.child("email")?.setValue(user?.email.toString())
                    Log.d("Message", "9")
                    startActivity(intent)
                    finish()
                    Log.d("Message", "0")

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

}
