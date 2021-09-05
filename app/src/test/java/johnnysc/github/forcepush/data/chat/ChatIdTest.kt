package johnnysc.github.forcepush.data.chat

import org.junit.Assert.*
import org.junit.Test

/**
 * @author Asatryan on 27.08.2021
 */
class ChatIdTest {

    @Test
    fun test() {
        val chatId = ChatId(Pair("aVsfwersa", "Bwersdf"))
        val actual = chatId.value()
        val expected = "aVsfwersa_Bwersdf"
        assertEquals(expected, actual)
    }

    @Test
    fun test_2() {
        val chatId = ChatId(Pair("AVsfwersa", "aBwersdf"))
        val actual = chatId.value()
        val expected = "aBwersdf_AVsfwersa"
        assertEquals(expected, actual)
    }

    @Test
    fun test_other_user_id() {
        val chatId = ChatId("user1Id_user2Id", "user1Id")
        val actual = chatId.otherUserId()
        val expected = "user2Id"
        assertEquals(expected, actual)
    }
}