package johnnysc.github.forcepush.ui.login

import android.os.Bundle
import johnnysc.github.forcepush.databinding.ActivityLoginBinding
import johnnysc.github.forcepush.ui.core.BaseActivity

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
            if (it is LoginUi.Success)
                switchToMain()
            else
                it.map(binding.errorTextView, binding.progressBar, binding.loginButton)
        }
        binding.loginButton.setOnClickListener { viewModel.login(LoginEngine.Login(this)) }
        viewModel.init(LoginEngine.SignIn(this))
    }
}