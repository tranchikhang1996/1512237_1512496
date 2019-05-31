package com.example.highschoolmathsolver.detector.config

import com.example.highschoolmathsolver.defination.SymbolType
import com.example.highschoolmathsolver.detector.data.rule.BinaryRule
import com.example.highschoolmathsolver.detector.data.rule.TerminalRule

class Configuration(
    val terminalRules: List<TerminalRule> = arrayListOf(),
    val binaryRules: List<BinaryRule> = arrayListOf(),
    val starSymbol: List<String> = arrayListOf(),
    val symbolTypes: Map<String, SymbolType> = mapOf()
)