package com.example.myappcompose.data.repository

import com.example.myappcompose.domain.model.Task
import com.example.myappcompose.domain.repository.TaskRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : TaskRepository {
    private val tasksRef = db.collection("tasks")

    override suspend fun getAllTasks(): List<Task> {
        val snapshot = tasksRef.get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Task::class.java)?.copy(id = doc.id)
        }
    }

    override suspend fun getTaskById(taskId: String): Task? {
        val doc = tasksRef.document(taskId).get().await()
        return doc.toObject(Task::class.java)?.copy(id = doc.id)
    }

    override suspend fun getCompletedTasks(): List<Task> {
        val snapshot = tasksRef
            .whereEqualTo("completed", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(20)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject(Task::class.java)?.copy(id = it.id) }
    }

    override fun observeTasks(): Flow<List<Task>> = callbackFlow {
        val listener = tasksRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val tasks = snapshot?.documents?.mapNotNull {
                it.toObject(Task::class.java)?.copy(id = it.id)
            } ?: emptyList()
            trySend(tasks)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun addTask(task: Task): String {
        val ref = tasksRef.add(task).await()
        return ref.id
    }

    override suspend fun updateTask(taskId: String, changes: Map<String, Any>) {
        tasksRef.document(taskId).update(changes).await()
    }

    override suspend fun deleteTask(taskId: String) {
        tasksRef.document(taskId).delete().await()
    }
}
