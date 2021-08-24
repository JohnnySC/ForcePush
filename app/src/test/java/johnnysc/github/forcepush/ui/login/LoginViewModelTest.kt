package johnnysc.github.forcepush.ui.login

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import johnnysc.github.forcepush.domain.login.LoginInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Test for [LoginViewModel]
 *
 * @author Asatryan on 15.08.2021
 */
class LoginViewModelTest {

    @ExperimentalCoroutinesApi
    @Test
    fun test_success() = runBlocking {
        val communication = TestCommunication()
        val dispatcher = TestCoroutineDispatcher()
        val viewModel = LoginViewModel(communication, TestInteractor(), dispatcher, dispatcher)
        viewModel.login(TestLoginEngine(true))
        val actual = communication.loginUi
        val expected = LoginUi.Success
        assertEquals(expected, actual)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_fail() = runBlocking {
        val communication = TestCommunication()
        val dispatcher = TestCoroutineDispatcher()
        val viewModel = LoginViewModel(communication, TestInteractor(), dispatcher, dispatcher)
        viewModel.login(TestLoginEngine(false))
        val actual = communication.loginUi
        val expected = LoginUi.Failed("error")
        assertEquals(expected, actual)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_authorized() = runBlocking {
        val communication = TestCommunication()
        val dispatcher = TestCoroutineDispatcher()
        val viewModel = LoginViewModel(communication, TestInteractor(true), dispatcher, dispatcher)
        viewModel.init(TestLoginEngine(true))
        val actual = communication.loginUi
        val expected = LoginUi.Success
        assertEquals(expected, actual)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_not_authorized() = runBlocking {
        val communication = TestCommunication()
        val dispatcher = TestCoroutineDispatcher()
        val viewModel = LoginViewModel(communication, TestInteractor(false), dispatcher, dispatcher)
        viewModel.init(TestLoginEngine(true))
        val actual = communication.loginUi
        val expected = LoginUi.Initial
        assertEquals(expected, actual)
    }

    private inner class TestCommunication : LoginCommunication {
        var loginUi: LoginUi = LoginUi.Progress()

        override fun observe(owner: LifecycleOwner, observer: Observer<LoginUi>) = Unit
        override fun map(data: LoginUi) {
            loginUi = data
        }
    }

    private inner class TestInteractor(private val authorized: Boolean = false) : LoginInteractor {
        override fun authorized() = authorized
        override suspend fun login(loginEngine: LoginEngine) = loginEngine.login()
        override suspend fun signIn(signIn: LoginEngine): Auth = signIn.login()
    }

    private inner class TestLoginEngine(private val success: Boolean) : LoginEngine {
        override suspend fun login() =
            if (success) Auth.Base(emptyMap()) else Auth.Fail(IllegalStateException("error"))
    }
}