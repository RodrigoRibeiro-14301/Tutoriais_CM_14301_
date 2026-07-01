package com.notes.notesproxmlviews

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    var loginBtn: Button? = null
    var progressBar: ProgressBar? = null
    var createAccountBtnTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById<EditText?>(R.id.email_edit_text)
        passwordEditText = findViewById<EditText?>(R.id.password_edit_text)
        loginBtn = findViewById<Button?>(R.id.login_btn)
        progressBar = findViewById<ProgressBar?>(R.id.progress_bar)
        createAccountBtnTextView = findViewById<TextView?>(R.id.create_account_text_view_btn)

        loginBtn!!.setOnClickListener(View.OnClickListener { v: View? -> loginUser() })
        createAccountBtnTextView!!.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(this@LoginActivity, CreateAccountActivity::class.java)
            )
        })
    }

    fun loginUser() {
        val email = emailEditText!!.getText().toString()
        val password = passwordEditText!!.getText().toString()

        val isValidated = validateData(email, password)
        if (!isValidated) {
            return
        }

        loginAccountInFirebase(email, password)
    }

    fun loginAccountInFirebase(email: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        changeInProgress(true)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    changeInProgress(false)
                    if (task.isSuccessful) {

                        if (firebaseAuth.currentUser!!.isEmailVerified) {

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Utility.showToast(
                                this@LoginActivity,
                                "Email not verified, Please verify your email."
                            )
                        }
                    } else {

                        Utility.showToast(
                            this@LoginActivity,
                            task.exception!!.localizedMessage
                        )
                    }
                }
            })
    }

    fun changeInProgress(inProgress: Boolean) {
        progressBar!!.visibility = if (inProgress) View.VISIBLE else View.GONE
        loginBtn!!.visibility = if (inProgress) View.GONE else View.VISIBLE
    }

    fun validateData(email: String, password: String): Boolean {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText!!.error = "Email is invalid"
            return false
        }
        if (password.length < 6) {
            passwordEditText!!.error = "Password length is invalid"
            return false
        }
        return true
    }
}
