package com.jparkbro.search.main

interface SearchEvent {
    data class SearchSuccess(val keyword: String) : SearchEvent
}