package br.com.etm.checkseries.utils;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;

import br.com.etm.checkseries.daos.BDCore;


/**
 * Created by EDUARDO_MARGOTO on 10/10/2016.
 */

public class MyBackupAgent extends BackupAgentHelper {


    static final String PREFS_BACKUP_KEY = "backup";
    static final String File_Name_Of_Prefrences = "myPrefrences";

    @Override
    public void onCreate() {
//        FileBackupHelper helper = new FileBackupHelper(this,"/data/data/br.com.etm.checkseries/databases//ambiserie_bd.db");
//        addHelper(PREFS_BACKUP_KEY, helper);
        Log.i("Backup", "addHelper");

    }
}
