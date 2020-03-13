package ch.epfl.sdp

import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*
import ch.epfl.sdp.authentication.*

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() =_binding!!
    private var connector:IUserConnector = FirebaseUserConnector()

    fun setOtherConnector(con: IUserConnector) {
        connector = con
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSubmitButton.setOnClickListener {
            signIn(userNameLogin.text, userPasswordLogin.text, connector)
        }
        binding.registerSubmitButton.setOnClickListener {
            register(userNameLogin.text, userPasswordLogin.text, connector)
        }
    }

    private fun register(email:Editable, password:Editable, connector:IUserConnector) {
        connector.register(email.toString(), password.toString())
        if(user.connected)
            changeDummyText("Account created and logged in as " + user.username)
        else
            changeDummyText("Account creation failed")
    }

    private fun signIn(email:Editable, password:Editable, connector: IUserConnector) {
        connector.connect(email.toString(), password.toString())
        if(user.connected)
            changeDummyText("User logged in as " + user.username)
        else
            changeDummyText("Logging failed")
    }

    private fun changeDummyText(text:String) {
        binding.loginTextResult.text = text;
    }
}