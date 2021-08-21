package johnnysc.github.forcepush.domain.login

import johnnysc.github.forcepush.core.TextMapper
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

    private val exception = java.lang.IllegalStateException("error")

    @Test
    fun test_success() = runBlocking {
        val repository = TestRepository()
        val interactor = LoginInteractor.Base(repository)
        val actual = interactor.login(TestLoginWrapper(true))
        val expected = Auth.Base(HashMap<String, Any>().apply {
            put("bio", "test bio")
        })
        assertEquals(expected, actual)
        val expectedString = "test bio"
        assertEquals(expectedString, repository.saved)
    }

    @Test
    fun test_fail() = runBlocking {
        val repository = TestRepository()
        val interactor = LoginInteractor.Base(repository)
        val actual = interactor.login(TestLoginWrapper(false))
        val expected = Auth.Fail(exception)
        assertEquals(expected, actual)
        val expectedString = expected.map(TestTextMapper()) //todo fix tests
        val actualString = actual.map(TestTextMapper())
        assertEquals(expectedString, actualString)
    }

    @Test
    fun test_authorized() {
        val repository = TestRepository(true)
        val interactor = LoginInteractor.Base(repository)
        val actual = interactor.authorized()
        val expected = true
        assertEquals(expected, actual)
    }

    @Test
    fun test_not_authorized() {
        val repository = TestRepository(false)
        val interactor = LoginInteractor.Base(repository)
        val actual = interactor.authorized()
        val expected = false
        assertEquals(expected, actual)
    }

    private inner class TestTextMapper : TextMapper<String> {
        override fun map(data: String) = data
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
            }) else Auth.Fail(exception)
    }
}