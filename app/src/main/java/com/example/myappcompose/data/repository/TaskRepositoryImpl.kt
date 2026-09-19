package com.example.myappcompose.data.repository

import com.example.myappcompose.data.mapper.toDocument
import com.example.myappcompose.data.mapper.toDomain
import com.example.myappcompose.data.remote.model.TaskDocument
import com.example.myappcompose.domain.model.Task
import com.example.myappcompose.domain.repository.AuthRepository
import com.example.myappcompose.domain.repository.TaskRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val authRepository: AuthRepository
) : TaskRepository {
    private val tasksRef = db.collection("tasks")

    private fun getUserId(): String = authRepository.getCurrentUserId() 
        ?: throw IllegalStateException("Usuario no autenticado")

    override suspend fun getAllTasks(): List<Task> {
        val userId = getUserId()
        val snapshot = tasksRef
            .whereEqualTo("ownerId", userId)
            .get()
            .await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(TaskDocument::class.java)?.toDomain()
        }
    }

    override suspend fun getTaskById(taskId: String): Task? {
        val userId = getUserId()
        val doc = tasksRef.document(taskId).get().await()
        val taskDoc = doc.toObject(TaskDocument::class.java)
        return if (taskDoc?.ownerId == userId) taskDoc.toDomain() else null
    }

    override suspend fun getCompletedTasks(): List<Task> {
        val userId = getUserId()
        val snapshot = tasksRef
            .whereEqualTo("ownerId", userId)
            .whereEqualTo("completed", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(20)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject(TaskDocument::class.java)?.toDomain() }
    }

    override fun observeTasks(): Flow<List<Task>> = callbackFlow {
        val userId = try { getUserId() } catch (e: Exception) { 
            close(e)
            return@callbackFlow 
        }
        
        val listener = tasksRef
            .whereEqualTo("ownerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val tasks = snapshot?.documents?.mapNotNull {
                    it.toObject(TaskDocument::class.java)?.toDomain()
                } ?: emptyList()
                trySend(tasks)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun addTask(task: Task): String {
        val userId = getUserId()
        val taskToSave = task.copy(ownerId = userId).toDocument()
        val ref = tasksRef.add(taskToSave).await()
        return ref.id
    }

    override suspend fun updateTask(taskId: String, changes: Map<String, Any>) {
        val userId = getUserId()
        // Verificar propiedad antes de actualizar
        val doc = tasksRef.document(taskId).get().await()
        val ownerId = doc.getString("ownerId")
        if (ownerId != userId) {
            throw IllegalAccessException("No tienes permiso para modificar esta tarea")
        }
        
        // Impedir cambio de ownerId
        val safeChanges = changes.toMutableMap().apply {
            remove("ownerId")
        }
        
        tasksRef.document(taskId).update(safeChanges).await()
    }

    override suspend fun deleteTask(taskId: String) {
        val userId = getUserId()
        // Verificar propiedad antes de eliminar
        val doc = tasksRef.document(taskId).get().await()
        val ownerId = doc.getString("ownerId")
        if (ownerId != userId) {
            throw IllegalAccessException("No tienes permiso para eliminar esta tarea")
        }
        tasksRef.document(taskId).delete().await()
    }
}
