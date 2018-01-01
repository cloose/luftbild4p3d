package org.luftbild4p3d.p3d

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader

fun runResampleProcess(infFile: InfFile) {
    println("Start resample process for ${infFile.filePath}")
    val process = ProcessBuilder("resample.exe", infFile.filePath).redirectErrorStream(true).start()
    var outputReader = BufferedReader(InputStreamReader(process.inputStream))

    Observable.create<String> {
        var line = outputReader.readLine()
        while (line != null) {
            it.onNext(line)
            line = outputReader.readLine()
        }

        it.onComplete()
    }.subscribeOn(Schedulers.single()).subscribe {
        println(it)
    }

    process.waitFor()
}
