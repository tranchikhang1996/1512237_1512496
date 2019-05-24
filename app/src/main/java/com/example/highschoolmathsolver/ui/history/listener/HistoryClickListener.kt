package com.example.highschoolmathsolver.ui.history.listener

import com.example.highschoolmathsolver.model.entity.Expression

interface HistoryClickListener {
    fun sendData(expression : Expression?)
}