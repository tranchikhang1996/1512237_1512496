package com.example.highschoolmathsolver.detector.config

import com.example.highschoolmathsolver.defination.SymbolType
import com.example.highschoolmathsolver.detector.data.rule.BinaryRule
import com.example.highschoolmathsolver.detector.data.rule.TerminalRule

class ConfigurationBuilder {
    private var terminalRules: List<TerminalRule> = arrayListOf()
    private var binaryRules: List<BinaryRule> = arrayListOf()
    private var starSymbols: List<String> = arrayListOf()
    private var symbolTypes: Map<String, SymbolType> = mapOf()

    fun setTermRules(terms: List<TerminalRule>): ConfigurationBuilder {
        this.terminalRules = terms
        return this
    }

    fun setBinRules(bins: List<BinaryRule>): ConfigurationBuilder {
        this.binaryRules = bins
        return this
    }

    fun setStartSymbol(startSymbols: List<String>): ConfigurationBuilder {
        this.starSymbols = startSymbols
        return this
    }

    fun setSymbolTypes(symbolTypes: Map<String, SymbolType>): ConfigurationBuilder {
        this.symbolTypes = symbolTypes
        return this
    }

    fun copy(config: Configuration) {
        this.terminalRules = config.terminalRules
        this.binaryRules = config.binaryRules
        this.starSymbols = config.starSymbol
        this.symbolTypes = config.symbolTypes
    }

    fun build(): Configuration {
        return Configuration(terminalRules, binaryRules, starSymbols, symbolTypes)
    }

    companion object {
        fun default(): Configuration {
            return Configuration()
        }
    }
}