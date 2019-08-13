package com.example.medihelper.mainapp.family

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.pojos.PersonListItem

class FamilyViewModel : ViewModel() {

    val personListItemListLive: LiveData<List<PersonListItem>> = AppRepository.getPersonListItemListLive()

}