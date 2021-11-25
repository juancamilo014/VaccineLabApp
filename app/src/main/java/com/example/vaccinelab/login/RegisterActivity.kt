package com.example.vaccinelab.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

        private lateinit var txtCompanyName:EditText
        private lateinit var txtNit:EditText
        private lateinit var txtName:EditText
        private lateinit var txtEmail:EditText
        private lateinit var txtPhone:EditText
        private lateinit var txtPassword:EditText
        private lateinit var progressBar: ProgressBar
        private lateinit var dbReference: DatabaseReference
        private lateinit var database: FirebaseDatabase
        private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        txtCompanyName=findViewById(R.id.txtCompanyName)
        txtNit=findViewById(R.id.txtNit)
        txtName=findViewById(R.id.txtName)
        txtEmail=findViewById(R.id.txtEmail)
        txtPhone=findViewById(R.id.txtPhone)
        txtPassword=findViewById(R.id.txtPassword)

        progressBar= findViewById(R.id.progressBar2)

        database= FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()

        dbReference=database.reference.child("User")
    }

    fun register(view:View){
        createNewAccount()
    }

    private fun createNewAccount(){
        val companyName:String=txtCompanyName.text.toString()
        val nit:String=txtNit.text.toString()
        val name:String=txtName.text.toString()
        val email:String=txtEmail.text.toString()
        val phone:String=txtPhone.text.toString()
        val password:String=txtPassword.text.toString()

        if (!TextUtils.isEmpty(companyName)&&!TextUtils.isEmpty(nit)&&!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(password)){
            progressBar.visibility=View.VISIBLE

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){
                        task ->

                    if(task.isComplete){
                        val user:FirebaseUser?=auth.currentUser
                        verifyEmail(user)

                        val userBD=dbReference.child(user?.uid.toString())//falta guardar en firebase

                        userBD.child("CompanyName").setValue(companyName)
                        userBD.child("Nit").setValue(nit)
                        userBD.child("Name").setValue(name)
                        userBD.child("Phone").setValue(phone)
                        action()
                    }
                }
        }
    }
    private fun action(){
        startActivity(Intent(this,LoginActivity::class.java))
    }
    private fun verifyEmail(user:FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                    task ->

                if (task.isComplete){
                    Toast.makeText(this,"Email enviado",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this,"Error al enviar Email",Toast.LENGTH_LONG).show()
                }
            }

    }
}