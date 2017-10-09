package org.luftbild4p3d.p3d

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.luftbild4p3d.app.WorkFolder
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class ScenProcBatchProcess {

    companion object {

        fun run(osmFileName: String, workFolder: WorkFolder, subscriber: (String) -> Unit) {
            val process = ProcessBuilder("D:\\Programme\\scenproc_latest_development_release_x64\\scenProc.exe", "autogen_script.spc", "/run", "$osmFileName").redirectErrorStream(true).directory(File(workFolder.name)).start()
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
}