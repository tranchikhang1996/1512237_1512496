package com.example.highschoolmathsolver.detector.config

import com.example.highschoolmathsolver.AndroidApplication
import com.example.highschoolmathsolver.defination.SymbolType
import com.example.highschoolmathsolver.detector.data.rule.BinaryRule
import com.example.highschoolmathsolver.detector.data.rule.TerminalRule

class ConfigurationProvider {
    private var configuration : Configuration? = null
    private var default = ConfigurationBuilder.default()

    fun getConfiguration(): Configuration {
        configuration?.run { return this }
        build()
        return configuration ?: default
    }

    private fun build() {
        val builder = ConfigurationBuilder()
        builder.copy(default)

        val terms = loadTerm()
        val bins = loadBin()
        val starSymbol = loadStartSymbol()
        val symbolTypes = loadSymbolType()

        if(terms.isNotEmpty()) {
            builder.setTermRules(terms)
        }

        if(bins.isNotEmpty()) {
            builder.setBinRules(bins)
        }

        if(starSymbol.isNotEmpty()) {
            builder.setStartSymbol(starSymbol)
        }

        if(symbolTypes.isNotEmpty()) {
            builder.setSymbolTypes(symbolTypes)
        }

        configuration = builder.build()
    }

    private fun loadTerm() : List<TerminalRule> {
        return AndroidApplication.instance.terminalRules
    }

    private fun loadBin() : List<BinaryRule> {
        return AndroidApplication.instance.binaryRules
    }

    private fun loadStartSymbol() : List<String> {
        return AndroidApplication.instance.starSymbols
    }

    private fun loadSymbolType() : Map<String, SymbolType> {
        return AndroidApplication.instance.symbolTypes
    }
}