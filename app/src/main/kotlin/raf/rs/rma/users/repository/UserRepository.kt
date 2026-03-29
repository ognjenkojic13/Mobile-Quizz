package raf.rs.rma.users.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import raf.rs.rma.users.db.User
import raf.rs.rma.users.db.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    fun createUser(user: User) {
        userDao.insert(user)
    }

    suspend fun updateUser(user: User) {
        userDao.update(user)
    }

    val allUsers: Flow<List<User>> = userDao.observeAll()

    fun getCurrentUser(): Flow<User?> {
        return userDao.observeAll().map { users -> users.firstOrNull() }
    }
}