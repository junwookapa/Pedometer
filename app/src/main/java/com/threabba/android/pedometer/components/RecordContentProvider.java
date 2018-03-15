package com.threabba.android.pedometer.components;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.database.Database;

import com.threabba.android.pedometer.R;
import com.threabba.android.pedometer.db.DaoMaster;
import com.threabba.android.pedometer.db.DaoSession;
import com.threabba.android.pedometer.db.RecordDao;

// generate by greendao
// some fixed..

public class RecordContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.threabba.android.pedometer.db.provider";
    public static final String BASE_PATH = "";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + BASE_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + BASE_PATH;

    private static final String TABLENAME = RecordDao.TABLENAME;
    private static final String PK = RecordDao.Properties.Id.columnName;

    private static final int RECORD_DIR = 0;
    private static final int RECORD_ID = 1;

    private static final UriMatcher sURIMatcher;

    static {
        sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, RECORD_DIR);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", RECORD_ID);
    }

    /**
    * This must be set from outside, it's recommended to do this inside your Application object.
    * Subject to change (static isn't nice).
    */
    private DaoSession daoSession;
    private SQLiteDatabase mDB;
    @Override
    public boolean onCreate() {
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(getContext(), getContext().getString(R.string.db_name), null); //create database db file if not exist
        mDB = masterHelper.getWritableDatabase();  //get the created database db file
        DaoMaster master = new DaoMaster(mDB);//create masterDao
        daoSession =master.newSession(); //Creates Session session
        return (daoSession != null);

    }

    protected Database getDatabase() {
        if(daoSession == null) {
            throw new IllegalStateException("DaoSession must be set during content provider is active");
        }
        return daoSession.getDatabase();
    }

    protected SQLiteDatabase getSQLiteDatabase() {
        if(mDB == null) {
            throw new IllegalStateException("DaoSession must be set during content provider is active");
        }
        return mDB;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) throws IllegalArgumentException{
        int uriType = sURIMatcher.match(uri);
        long id = 0;
        String path = "";
        SQLiteDatabase db = getSQLiteDatabase();
        switch (uriType) {
        case RECORD_DIR:
            id =   db.insert(TABLENAME, null, values);
            path = BASE_PATH + "/" + id;
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(path);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) throws IllegalArgumentException{
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = getSQLiteDatabase();
        int rowsDeleted = 0;
        String id;
        switch (uriType) {
        case RECORD_DIR:
                rowsDeleted = db.delete(TABLENAME, selection, selectionArgs);
                break;
        case RECORD_ID:
            id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsDeleted = db.delete(TABLENAME, PK + "=" + id, null);
            } else {
                rowsDeleted = db.delete(TABLENAME, PK + "=" + id + " and "
                                + selection, selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = getSQLiteDatabase();
        int rowsUpdated = 0;
        String id;
        switch (uriType) {
        case RECORD_DIR:
            rowsUpdated = db.update(TABLENAME, values, selection, selectionArgs);
            break;
        case RECORD_ID:
            id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(TABLENAME, values, PK + "=" + id, null);
            } else {
                    rowsUpdated = db.update(TABLENAME, values, PK + "=" + id
                                    + " and " + selection, selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case RECORD_DIR:
            queryBuilder.setTables(TABLENAME);
            break;
        case RECORD_ID:
            queryBuilder.setTables(TABLENAME);
            queryBuilder.appendWhere(PK + "="
                    + uri.getLastPathSegment());
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Database db = getDatabase();
        Cursor cursor = queryBuilder.query(((StandardDatabase) db).getSQLiteDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public final String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
        case RECORD_DIR:
            return CONTENT_TYPE;
        case RECORD_ID:
            return CONTENT_ITEM_TYPE;
        default :
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
