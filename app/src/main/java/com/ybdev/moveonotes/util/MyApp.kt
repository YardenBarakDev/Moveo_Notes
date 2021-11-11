package com.ybdev.moveonotes.util

import android.app.Application
import com.ybdev.moveonotes.db.NotesRoomDB

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        //initiate room db
        NotesRoomDB.invoke(this)
    }
}