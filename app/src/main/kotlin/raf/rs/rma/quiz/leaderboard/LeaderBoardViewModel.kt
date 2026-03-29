package raf.rs.rma.quiz.leaderboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raf.rs.rma.users.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state = _state.asStateFlow()


    init {
        getLeaderboard()
    }

     fun getLeaderboard() {
        viewModelScope.launch {
            try {

                val leaderboard = withContext(Dispatchers.IO){ leaderboardRepository.getLeaderboardWithQuizCount(1)}
                val currentUser = withContext(Dispatchers.IO){userRepository.getCurrentUser().firstOrNull()}
                val currentUserRank = leaderboard.indexOfFirst { it.nickname == currentUser?.nickname }

                _state.value = _state.value.copy(
                    results = leaderboard,
                    currentUserRank = if (currentUserRank >= 0) currentUserRank + 1 else 0
                )
            } catch (e: Exception) {
                Log.e("LeaderboardViewModel", "Error fetching leaderboard data", e)
            }
        }
    }

    fun shareResult(score: Float, onShared: () -> Unit) {
        Log.d("leaderBoardShare","shareResults")
        viewModelScope.launch {
            try {
                val currentUser = userRepository.getCurrentUser().firstOrNull()
                val nickname = currentUser?.nickname ?: "defaultUser"
                val quizResult = QuizResultApiModel(
                    category = 1,
                    nickname = nickname,
                    result = score
                )

                withContext(Dispatchers.IO){
                    leaderboardRepository.postQuizResult(quizResult)
                }

                getLeaderboard()
                onShared()
            } catch (e: Exception) {

            }
        }
    }

}