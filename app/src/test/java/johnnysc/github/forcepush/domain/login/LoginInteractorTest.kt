package johnnysc.github.forcepush.domain.login

import johnnysc.github.forcepush.data.login.LoginRepository
import johnnysc.github.forcepush.data.login.UserInitial
import johnnysc.github.forcepush.ui.login.Auth
import johnnysc.github.forcepush.ui.login.LoginEngine
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Test for [LoginInteractor]
 *
 * @author Asatryan on 15.08.2021
 */
class LoginInteractorTest {

    private val mapper = Auth.AuthResultMapper.Base()
    private val exception = java.lang.IllegalStateException("error")

    @Test
    fun test_login_success() = runBlocking {
        val repository = TestRepository()
        val interactor = LoginInteractor.Base(repository, mapper)
        val actual = interactor.login(TestLoginWrapper(true))
        val expected = Auth.Base(HashMap<String, Any>().apply {
            put("bio", "test bio")
            put("name", "test name")
            put("login", "test login")
            put("email", "test email")
            put("avatar_url", "test avatar_url")
        })
        assertEquals(expected, actual)
        val actualResult = repository.saved
        val expectedResult = UserInitial(
            "test name",
            "test login",
            "test email",
            "test bio",
            "test avatar_url"
        )
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun test_login_fail() = runBlocking {
        val repository = TestRepository()
        val interactor = LoginInteractor.Base(repository, mapper)
        val actual = interactor.login(TestLoginWrapper(false))
        val expected = Auth.Fail(exception)
        assertEquals(expected, actual)
    }

    @Test
    fun test_authorized() {
        val repository = TestRepository(true)
        val interactor = LoginInteractor.Base(repository, mapper)
        val actual = interactor.authorized()
        val expected = true
        assertEquals(expected, actual)
    }

    @Test
    fun test_not_authorized() {
        val repository = TestRepository(false)
        val interactor = LoginInteractor.Base(repository, mapper)
        val actual = interactor.authorized()
        val expected = false
        assertEquals(expected, actual)
    }

    private inner class TestRepository(private val authorized: Boolean = false) : LoginRepository {
        var saved: UserInitial = UserInitial()
        override suspend fun saveUser(user: UserInitial) {
            saved = user
        }
        override fun user() = if (authorized) Object() else null
    }

    private inner class TestLoginWrapper(private val success: Boolean) : LoginEngine {
        override suspend fun login() =
            if (success) Auth.Base(HashMap<String, Any>().apply {
                put("bio", "test bio")
                put("name", "test name")
                put("login", "test login")
                put("email", "test email")
                put("avatar_url", "test avatar_url")
            }) else Auth.Fail(exception)
    }
}