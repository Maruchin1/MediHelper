package com.example.medihelper.remotedatabase

import retrofit2.HttpException
import java.net.SocketTimeoutException

enum class ApiResponse(val message: String) {
    OK("Operacja wykonana pomyślnie"),
    TIMEOUT("Przekroczono czas oczekiwania na połączenie"),
    BAD_REQUEST("Niepoprawne dane"),
    UNAUTHORIZED("Błąd autoryzacji"),
    OTHER("Błąd połączenia");

    companion object {
        fun getResponseByException(e: Exception): ApiResponse {
            return when (e) {
                is SocketTimeoutException -> TIMEOUT
                is HttpException -> when (e.code()) {
                    400 -> BAD_REQUEST
                    401 -> UNAUTHORIZED
                    else -> OTHER
                }
                else -> OTHER
            }
        }
    }
}