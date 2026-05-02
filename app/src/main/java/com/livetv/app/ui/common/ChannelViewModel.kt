package com.livetv.app.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livetv.app.data.model.Channel
import com.livetv.app.data.model.ChannelCategory
import com.livetv.app.data.repository.ChannelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val repository: ChannelRepository
) : ViewModel() {

    private val _channels = MutableLiveData<UiState<List<Channel>>>()
    val channels: LiveData<UiState<List<Channel>>> = _channels

    private val _categories = MutableLiveData<UiState<List<ChannelCategory>>>()
    val categories: LiveData<UiState<List<ChannelCategory>>> = _categories

    init {
        loadByCategory()
    }

    fun loadChannels() {
        viewModelScope.launch {
            _channels.value = UiState.Loading
            val result = repository.getChannels()
            _channels.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull()!!)
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun loadByCategory() {
        viewModelScope.launch {
            _categories.value = UiState.Loading
            val result = repository.getChannelsByCategory()
            _categories.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull()!!)
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}
