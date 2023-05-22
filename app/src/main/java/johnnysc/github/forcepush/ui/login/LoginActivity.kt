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

        viewModel.liveDataEngine.observe(this) {
            it.handle(this)
        }
        viewModel.observe(this) {
            it.navigate(this)
            it.map(binding.errorTextView, binding.progressBar, binding.loginButton)
        }

        binding.loginButton.setOnClickListener {
            viewModel.login()
        }

        viewModel.init()
    }
}