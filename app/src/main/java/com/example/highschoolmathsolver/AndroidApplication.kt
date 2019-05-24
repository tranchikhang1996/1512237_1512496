package com.example.highschoolmathsolver

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.highschoolmathsolver.di.component.DaggerUserComponent
import com.example.highschoolmathsolver.di.component.UserComponent
import com.example.highschoolmathsolver.model.database.ExpressionDatabase
import org.opencv.ml.ANN_MLP
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


class AndroidApplication : Application() {

    companion object {
        const val DATABASE_NAME = "expression_history_database"
        lateinit var instance: AndroidApplication
    }

    lateinit var userComponent: UserComponent
    lateinit var database: ExpressionDatabase
    lateinit var labelTable: HashMap<Int, String>
    lateinit var mlp: ANN_MLP

    override fun onCreate() {
        super.onCreate()
        instance = this
        userComponent = DaggerUserComponent.builder().build()
        database = Room.databaseBuilder(applicationContext, ExpressionDatabase::class.java, DATABASE_NAME).build()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun loadMlp() { // load du lieu , model thuoc openCv
        if(::mlp.isInitialized) {
            return
        }
        val weightFile = File(filesDir, "weights.txt")
        if (!weightFile.exists()) {
            val fis = assets.open("weights.txt")
            openFileOutput("weights.txt", Context.MODE_PRIVATE).use {
                val buffer = ByteArray(1024)
                var n = fis.read(buffer)
                while (n != -1) {
                    it.write(buffer, 0, n)
                    n = fis.read(buffer)
                }
                it.close()
            }
            fis.close()
        }
        mlp = ANN_MLP.load(weightFile.path)
    }

    fun loadLabelTable() { // load du lieu, model thuoc openCV
        if(::labelTable.isInitialized) {
            return
        }

        labelTable = HashMap()
        val labelFile = File(filesDir, "label.txt")
        if (!labelFile.exists()) {
            val fis = assets.open("label.txt")
            openFileOutput("label.txt", Context.MODE_PRIVATE).use {
                val buffer = ByteArray(1024)
                var n = fis.read(buffer)
                while (n != -1) {
                    it.write(buffer, 0, n)
                    n = fis.read(buffer)
                }
                it.close()
            }
            fis.close()
        }

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
}