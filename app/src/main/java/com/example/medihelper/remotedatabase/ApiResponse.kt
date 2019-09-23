package com.example.medihelper.remotedatabase

import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.net.ssl.HttpsURLConnection

enum class ApiResponse(val message: String) {
    OK("Operacja wykonana pomyślnie"),
    TIMEOUT("Przekroczono czas oczekiwania na połączenie"),
    BAD_REQUEST("Niepoprawne rządanie"),
    UNAUTHORIZED("Błąd autoryzacji"),
    NOT_FOUND("Nie znaleziono zasobu"),
    CONFLICT("Zasób już istnieje"),
    INCORRECT_DATA("Niepoprawne dane"),
    OTHER("Błąd połączenia");

    companion object {
        fun getResponseByException(e: Exception): ApiResponse {
            return when (e) {
                is SocketTimeoutException -> TIMEOUT
                is HttpException -> when (e.code()) {
                    HttpsURLConnection.HTTP_BAD_REQUEST -> BAD_REQUEST
                    HttpsURLConnection.HTTP_UNAUTHORIZED -> UNAUTHORIZED
                    HttpsURLConnection.HTTP_NOT_FOUND -> NOT_FOUND
                    HttpsURLConnection.HTTP_CONFLICT -> CONFLICT
                    422 -> INCORRECT_DATA
                    else -> OTHER
                }
                else -> OTHER
            }
        }
    }
}