package com.shahrukhamd.realestate.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahrukhamd.realestate.data.model.PropertyItem
import com.shahrukhamd.realestate.data.repository.PropertyRepository
import com.shahrukhamd.realestate.ui.base.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class RealEstateViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository
): ViewModel() {

    private val _showProperties = MutableLiveData<List<PropertyItem>>()
    val showProperties: LiveData<List<PropertyItem>> = _showProperties

    private val _showPropertyDetails = MutableLiveData<Event<PropertyItem>>()
    val showPropertyDetails: LiveData<Event<PropertyItem>> = _showPropertyDetails

    private val _showError = MutableLiveData<Event<String>>()
    val showError: LiveData<Event<String>> = _showError

    private val _showLoading = MutableLiveData<Event<Boolean>>()
    val showLoading: LiveData<Event<Boolean>> = _showLoading

    private val _closePropertyDetails = MutableLiveData<Event<Boolean>>()
    val closePropertyDetails: LiveData<Event<Boolean>> = _closePropertyDetails

    init {
        loadProperties()
    }

    fun onPropertyListRefresh() {
        loadProperties()
    }

    fun onPropertyItemClicked(property: PropertyItem) {
        property.id?.let {
            getPropertyDetails(it)
        }
    }

    fun onPropertyDetailBackPress() {
        _closePropertyDetails.postValue(Event(true))
    }

    private fun loadProperties() {
        viewModelScope.launch(Dispatchers.IO) {
            propertyRepository.getProperties()
                .flowOn(Dispatchers.IO)
                .onStart {
                    _showLoading.postValue(Event(true))
                }.onCompletion {
                    _showLoading.postValue(Event(false))
                }.catch { e ->
                    _showError.postValue(Event(e.message.orEmpty()))
                    Log.e(TAG, "loadPropertyList: ", e)
                }.collect {
                    if (it.code == HttpURLConnection.HTTP_OK && it.responseData != null) {
                        _showProperties.postValue(it.responseData!!)
                    } else {
                        _showError.postValue(Event("[${it.code}] ${it.message}"))
                    }
                }
        }
    }

    private fun getPropertyDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            propertyRepository.getPropertyDetail(id)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _showError.postValue(Event(e.message.orEmpty()))
                    Log.e(TAG, "loadPropertyDetails: ", e)
                }.collect {
                    if (it.code == HttpURLConnection.HTTP_OK && it.responseData != null) {
                        _showPropertyDetails.postValue(Event(it.responseData))
                    } else {
                        _showError.postValue(Event("[${it.code}] ${it.message}"))
                    }
                }
        }
    }

    companion object {
        val TAG = RealEstateViewModel::class.simpleName
    }
}