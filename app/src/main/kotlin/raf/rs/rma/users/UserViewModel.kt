package raf.rs.rma.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raf.rs.rma.quiz.db.QuizRepository
import raf.rs.rma.quiz.leaderboard.LeaderboardRepository
import raf.rs.rma.users.db.User
import raf.rs.rma.users.repository.UserRepository
import java.io.IOException
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val quizRepository: QuizRepository,
    private val leaderboardRepository: LeaderboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.allUsers.collect { users ->
                val userList = users.map { user ->
                    UserUiModel(
                        id = user.id,
                        name = user.name,
                        nickname = user.nickname,
                        email = user.email
                    )
                }
                val currentUser = userList.firstOrNull()
                _state.value = UserState(
                    currentUser = currentUser,
                    users = userList,
                    loading = false
                )
                currentUser?.let {
                    fetchQuizCountAndBestRank(it.nickname)
                }
            }
        }
    }

    private suspend fun fetchQuizCountAndBestRank(nickname: String) {
        val leaderboard = withContext(Dispatchers.IO){ leaderboardRepository.getLeaderboard(category = 1)}
        val bestRank = leaderboard.indexOfFirst { it.nickname == nickname }.takeIf { it >= 0 }?.plus(1) ?: 0

        _state.update { it.copy(bestLeaderboardPosition = bestRank) }

        repository.getCurrentUser().collect { currentUser ->
            val nicknamee = currentUser?.nickname ?: "defaultUser"
            quizRepository.getResultsByNickname(nicknamee).collect { results ->
                _state.value = _state.value.copy(quizResults = results, quizCount = results.size)
            }

        }
    }

    fun validateNickname(nickname: String): Boolean {
        val pattern = Pattern.compile("^[a-zA-Z0-9_]+$")
        return pattern.matcher(nickname).matches()
    }

    fun validateEmailFormat(email: String): Boolean {
        val pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        return pattern.matcher(email).matches()
    }

    fun createUser(name: String, nickname: String, email: String, onValidationFailed: (String) -> Unit, onSuccess: () -> Unit) {
        if (!validateNickname(nickname)) {
            onValidationFailed("Nickname može sadržati samo slova, brojeve i underscore.")
            return
        }

        if (!validateEmailFormat(email)) {
            onValidationFailed("Email adresa nije u validnom formatu.")
            return
        }
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(updating = true)
                withContext(Dispatchers.IO){
                    repository.createUser(User(0, name, nickname, email))
                }
                onSuccess()
            } catch (error: IOException){
                error(error.toString());
            } finally {
                _state.value = _state.value.copy(updating = false)
            }
        }
    }

    fun updateUser(name: String, nickname: String, email: String, onValidationFailed: (String) -> Unit, onSuccess: () -> Unit) {
        if (!validateNickname(nickname)) {
            onValidationFailed("Nickname može sadržati samo slova, brojeve i underscore.")
            return
        }

        if (!validateEmailFormat(email)) {
            onValidationFailed("Email adresa nije u validnom formatu.")
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _state.value.currentUser?.let { currentUser ->
                    repository.updateUser(User(currentUser.id, name, nickname, email))
                    _state.value = _state.value.copy(
                        currentUser = UserUiModel(currentUser.id, name, nickname, email)
                    )
                }
            }
            onSuccess()
        }
    }

}