package johnnysc.github.forcepush.data.groups.create

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import johnnysc.github.forcepush.core.FirebaseDatabaseProvider
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author Asatryan on 08.09.2021
 */
interface CreateGroupRepository {

    suspend fun createGroup(groupName: String): Boolean

    suspend fun groups(): List<String>

    class Base(private val firebaseDatabaseProvider: FirebaseDatabaseProvider) :
        CreateGroupRepository {

        private val uid = Firebase.auth.currentUser!!.uid

        override suspend fun createGroup(groupName: String): Boolean {
            val ref = firebaseDatabaseProvider.provideDatabase().child("groups").push()
            val result = ref.setValue(GroupItem(uid, groupName.lowercase()))
            return handle(result)
        }

        override suspend fun groups(): List<String> {
            val groups = firebaseDatabaseProvider.provideDatabase()
                .child("groups")
                .orderByChild("userId")
                .equalTo(uid)
            return handleGroups(groups)
        }

        private suspend fun handle(data: Task<Void>) = suspendCoroutine<Boolean> { cont ->
            data.addOnSuccessListener { cont.resume(true) }
                .addOnFailureListener { cont.resume(false) }
        }

        private suspend fun handleGroups(query: Query) = suspendCoroutine<List<String>> { cont ->
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.children.mapNotNull { it.getValue(GroupItem::class.java) }
                    cont.resume(data.map { it.name })
                }

                override fun onCancelled(error: DatabaseError) = Unit
            })
        }
    }
}

data class GroupItem(
    val userId: String = "",
    val name: String = ""
)