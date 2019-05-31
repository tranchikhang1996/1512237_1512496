package com.example.highschoolmathsolver

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.highschoolmathsolver.defination.SymbolType
import com.example.highschoolmathsolver.detector.data.Gmm
import com.example.highschoolmathsolver.detector.data.rule.BinaryRule
import com.example.highschoolmathsolver.detector.data.rule.TerminalRule
import com.example.highschoolmathsolver.di.component.DaggerUserComponent
import com.example.highschoolmathsolver.di.component.UserComponent
import com.example.highschoolmathsolver.model.database.ExpressionDatabase
import com.example.highschoolmathsolver.util.ImageUtils
import org.opencv.ml.ANN_MLP
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class AndroidApplication : Application() {

    companion object {
        const val DATABASE_NAME = "expression_history_database"
        const val labelFileName = "label.txt"
        const val weightFileName = "weights.txt"
        const val gmmFileName = "sparel.txt"
        const val terminalFile = "term.txt"
        const val binaryFile = "binary.txt"
        const val startSymbolFile = "startsymbol.txt"
        const val symbolTypeFile = "symbolType.txt"
        lateinit var instance: AndroidApplication
    }

    lateinit var userComponent: UserComponent
    lateinit var database: ExpressionDatabase
    lateinit var labelTable: HashMap<Int, String>
    lateinit var mlp: ANN_MLP
    lateinit var gmm: Gmm
    lateinit var terminalRules: MutableList<TerminalRule>
    lateinit var binaryRules: MutableList<BinaryRule>
    lateinit var starSymbols: MutableList<String>
    lateinit var symbolTypes: MutableMap<String, SymbolType>


    override fun onCreate() {
        super.onCreate()
        instance = this
        userComponent = DaggerUserComponent.builder().build()
        database = Room.databaseBuilder(applicationContext, ExpressionDatabase::class.java, DATABASE_NAME).build()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun loadConfig() {
        loadMlp()
        loadLabelTable()
        loadGmm()
        loadBinaryRule()
        loadTerminalRule()
        loadStartSymbol()
        loadSymbolType()
    }

    private fun loadMlp() { // load du lieu , model thuoc openCv
        if (::mlp.isInitialized) {
            return
        }
        val weightFile = fromAssetToInternal(weightFileName)
        mlp = ANN_MLP.load(weightFile.path)
    }

    private fun loadLabelTable() { // load du lieu, model thuoc openCV
        if (::labelTable.isInitialized) {
            return
        }

        labelTable = HashMap()
        val labelFile = fromAssetToInternal(labelFileName)

        val b = BufferedReader(FileReader(labelFile))
        var line = b.readLine()
        while (line != null) {
            val values = line.split(',')
            val id = values[0].toInt()
            val character = values[1]
            labelTable[id] = character
            line = b.readLine()
        }
        b.close()
    }

    private fun loadGmm() {
        if (::gmm.isInitialized) {
            return
        }
        gmm = Gmm()
        val spaFile = fromAssetToInternal(gmmFileName)
        gmm.loadModel(spaFile)
    }

    private fun loadTerminalRule() {
        if (::terminalRules.isInitialized) {
            return
        }
        terminalRules = arrayListOf()
        val file = fromAssetToInternal(terminalFile)
        val b = BufferedReader(FileReader(file))
        var line = b.readLine()
        while (line != null) {
            val values = line.split(' ')
            val rule = TerminalRule(values[1], values[2], values[0].toDouble(), values[3])
            terminalRules.add(rule)
            Timber.d("[loadTerminalRule] ${rule.prob} ${rule.A} ${rule.s} ${rule.template}")
            line = b.readLine()
        }
        b.close()
    }

    private fun loadBinaryRule() {
        if (::binaryRules.isInitialized) {
            return
        }
        binaryRules = arrayListOf()
        val file = fromAssetToInternal(binaryFile)
        val b = BufferedReader(FileReader(file))
        var line = b.readLine()
        while (line != null) {
            val values = line.split(' ')
            val rule =
                BinaryRule(
                    values[2], values[3], values[4],
                    values[0].toDouble(), ImageUtils.toSpatialRelationship(values[1]),
                    values[5].replace('@',' '), ImageUtils.toMergeTag(values[6])
                )
            binaryRules.add(rule)
            Timber.d("[loadRule] ${rule.prob} ${rule.sparel} ${rule.A} ${rule.B} ${rule.C} ${rule.template}")
            line = b.readLine()
        }
        b.close()
    }

    private fun loadStartSymbol() {
        if (::starSymbols.isInitialized) {
            return
        }
        starSymbols = arrayListOf()
        val file = fromAssetToInternal(startSymbolFile)
        val b = BufferedReader(FileReader(file))
        var line = b.readLine()
        while (line != null) {
            val values = line.split(' ')
            starSymbols.add(values[0])
            line = b.readLine()
        }
        b.close()
    }

    private fun loadSymbolType() {
        if (::symbolTypes.isInitialized) {
            return
        }
        symbolTypes = mutableMapOf()
        val file = fromAssetToInternal(symbolTypeFile)
        val b = BufferedReader(FileReader(file))
        var line = b.readLine()
        while (line != null) {
            val values = line.split(' ')
            symbolTypes[values[0]] = ImageUtils.toSymbolType(values[1])
            line = b.readLine()
        }
        b.close()
    }

    private fun fromAssetToInternal(fileName: String): File {
        val file = File(filesDir, fileName)
        if (file.exists()) {
            return file
        }
        val fis = assets.open(fileName)
        openFileOutput(fileName, Context.MODE_PRIVATE).use {
            val buffer = ByteArray(1024)
            var n = fis.read(buffer)
            while (n != -1) {
                it.write(buffer, 0, n)
                n = fis.read(buffer)
            }
            it.close()
        }
        fis.close()
        return file
    }
}