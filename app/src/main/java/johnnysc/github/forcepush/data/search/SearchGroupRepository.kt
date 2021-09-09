package johnnysc.github.forcepush.data.search

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import johnnysc.github.forcepush.core.FirebaseDatabaseProvider
import johnnysc.github.forcepush.core.Save
import johnnysc.github.forcepush.data.groups.create.GroupItem
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author Asatryan on 07.09.2021
 */
interface SearchGroupRepository : Save<String> {
    suspend fun search(query: String): List<SearchData>
    suspend fun initChat(groupId: String): Boolean

    class Base(
        private val firebaseDatabaseProvider: FirebaseDatabaseProvider,
        private val groupIdContainer: Save<String>
    ) : SearchGroupRepository {

        override suspend fun search(query: String): List<SearchData> {
            val users = firebaseDatabaseProvider.provideDatabase()
                .child("groups")
                .orderByChild("name")
                .equalTo(query)
            return handleResult(users).map { (key, data) ->
                SearchData.Group(key, data.name)
            }
        }

        private suspend fun handleResult(users: Query) =
            suspendCoroutine<List<Pair<String, GroupItem>>> { cont ->
                users.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) =
                        snapshot.children.mapNotNull {
                            Pair(it.key!!, it.getValue(GroupItem::class.java)!!)
                        }.let { cont.resume(it) }

                    override fun onCancelled(error: DatabaseError) = Unit
                })
            }

        private val myUid = Firebase.auth.currentUser!!.uid

        override suspend fun initChat(groupId: String): Boolean = firebaseDatabaseProvider
            .provideDatabase().child("users-groups").run {
                var result = handleData(child(myUid).child(groupId).setValue(true))
                if (result) {
                    result = handleData(child(groupId).child(myUid).setValue(true))
                    if (result) save(groupId)
                }
                return result
            }

        private suspend fun handleData(data: Task<Void>) = suspendCoroutine<Boolean> { cont ->
            data.addOnSuccessListener { cont.resume(true) }
                .addOnFailureListener { cont.resume(false) }
        }

        override fun save(data: String) = groupIdContainer.save(data)
    }
}