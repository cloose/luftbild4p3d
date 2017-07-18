package org.luftbild4p3d.p3d

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.luftbild4p3d.app.WorkFolder
import java.io.BufferedReader
import java.io.InputStreamReader

class ResampleProcess(val workFolder: WorkFolder) {

    fun start(infFile: InfFile, subscriber: (String) -> Unit) {
        val process = ProcessBuilder("D:\\Daten\\Projects\\workspace\\ortho4p3d\\resample.exe", infFile.filePath).redirectErrorStream(true).start()
        val outputReader = BufferedReader(InputStreamReader(process.inputStream))

        Observable.create<String> {
            var line = outputReader.readLine()
            while (line != null) {
                it.onNext(line)
                line = outputReader.readLine()
            }

            it.onComplete()
        }.subscribeOn(Schedulers.single()).subscribe(subscriber)

        process.waitFor()
    }
}