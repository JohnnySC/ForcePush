package johnnysc.github.forcepush.ui.login

import android.content.Intent
import android.os.Bundle
import johnnysc.github.forcepush.databinding.ActivityLoginBinding
import johnnysc.github.forcepush.ui.core.BaseActivity
import johnnysc.github.forcepush.ui.main.MainActivity

/**
 * @author Asatryan on 14.08.2021
 */
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel = viewModel(LoginViewModel::class.java, this)
        viewModel.observe(this) {
            if (it is LoginUi.Success) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else
                it.map(binding.errorTextView, binding.progressBar, binding.loginButton)
        }
        binding.loginButton.setOnClickListener { viewModel.login(LoginWrapper.Base(this)) }
        viewModel.init()
    }
}