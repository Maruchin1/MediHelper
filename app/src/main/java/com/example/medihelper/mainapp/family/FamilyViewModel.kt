package com.example.medihelper.mainapp.family

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.pojos.PersonItem

class FamilyViewModel : ViewModel() {

    val personListItemLive: LiveData<List<PersonItem>> = AppRepository.getPersonListItemListLive()

}