package com.threabba.android.dbgenerator;

import android.content.ContentProvider;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * Created by jun on 16. 12. 3.
 * reference :
 * http://www.arjunsk.com/android/use-greendao-android-studio/
 *
 */

public class DBGenerator {
    public static void main(String[] args)  throws Exception {

        //place where db folder will be created inside the project folder
        Schema schema = new Schema(1,"com.threabba.android.pedometer.db");

        //Entity i.e. Class to be stored in the database // ie table LOG
        Entity word_entity= schema.addEntity("Record");

        word_entity.addIdProperty(); //It is the primary key for uniquely identifying a row

        word_entity.addDateProperty("date").notNull();  //Not null is SQL constrain
        word_entity.addIntProperty("step_count").notNull();
        word_entity.addFloatProperty("distance").notNull();
        //  ./app/src/main/java/   ----   com/codekrypt/greendao/db is the full path
        //word_entity.addContentProvider();

        new DaoGenerator().generateAll(schema, "./app/src/main/java");


    }
}
