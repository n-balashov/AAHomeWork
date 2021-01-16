package com.aacademy.homework.ui.movielist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aacademy.homework.data.DataRepository
import com.aacademy.homework.data.model.Movie
import com.aacademy.homework.foundations.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Collections

class MoviesListViewModel @ViewModelInject constructor(
    private val dataRepository: DataRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _movies = MutableLiveData<Resource<List<Movie>>>()
    val movies: LiveData<Resource<List<Movie>>> = _movies

    private val moviesExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
        _movies.postValue(Resource.error(throwable.message ?: ""))
    }

    private var moviesList: MutableList<Movie>
    private var currentPage = 1
    private var isLoading = false
    private var isLastPageLoaded = false
    var currentQuery = ""

    init {
        moviesList = mutableListOf()
        loadMovies()
    }

    fun refreshMoviesPreviews() {
        currentPage = 1
        isLoading = false
        isLastPageLoaded = false
        loadMovies()
    }

    fun searchMovies(query: String?) {
        currentPage = 1
        isLoading = false
        isLastPageLoaded = false
        currentQuery = query ?: ""
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch(dispatcher + moviesExceptionHandler) {
            if (!isLoading && !isLastPageLoaded) {
                isLoading = true
                if (moviesList.isNullOrEmpty()) {
                    _movies.postValue(Resource.loading(null))
                }
                val movies = dataRepository.getMovies(currentQuery, currentPage)

                if (currentPage == 1) {
                    moviesList = movies.second.toMutableList()
                } else {
                    moviesList.addAll(movies.second)
                }
                currentPage++
                _movies.postValue(
                    Resource.success(moviesList)
                )
                isLastPageLoaded = currentPage > movies.first
                isLoading = false
            }
        }
    }

    fun setMovieLiked(id: Long, isLiked: Boolean) {
        viewModelScope.launch(dispatcher) {
            try {
                dataRepository.setMovieLiked(id, isLiked)
            } catch (thr: Throwable) {
                Timber.e(thr)
            }
        }
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        val newMovies = movies.value!!.data!!.toMutableList()
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(newMovies, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo (toPosition + 1)) {
                Collections.swap(newMovies, i, i - 1)
            }
        }

        _movies.postValue(Resource.success(newMovies))
    }
}
