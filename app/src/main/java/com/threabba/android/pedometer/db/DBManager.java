package com.threabba.android.pedometer.db;

import com.threabba.android.pedometer.db.DaoSession;
import com.threabba.android.pedometer.db.Record;
import com.threabba.android.pedometer.db.RecordDao;

import java.util.Date;
import java.util.List;

/**
 * Created by jun on 16. 12. 5.
 */

public class DBManager {
    @SuppressWarnings("deprecation")
    public static Record getRecord(DaoSession session){
        Date date = new Date();
        List<Record> records = session.getRecordDao().queryBuilder().orderDesc(RecordDao.Properties.Id).build().list();
        if(records != null && records.size()>0){
            if((records.get(0).getDate().getYear() == date.getYear())
                    &&(records.get(0).getDate().getMonth() == date.getMonth())
                    &&(records.get(0).getDate().getDay() == date.getDay()) ){
                return records.get(0);

            }else{
                Record record = new Record();
                record.setDate(date);
                record.setStep_count(0);
                record.setDistance(0.f);
                session.getRecordDao().insert(record);
                return record;
            }

        }else{
            Record record = new Record();
            record.setDate(date);
            record.setStep_count(0);
            record.setDistance(0.f);
            session.getRecordDao().insert(record);
            return record;
        }
    }
    @SuppressWarnings("deprecation")
    public static boolean update(DaoSession session, Record record){
        session.update(record);
        Date date = new Date();
        if((date.getYear() == record.getDate().getYear())
                &&(date.getMonth() == record.getDate().getMonth())
                &&(date.getDay() == record.getDate().getDay()) ){
            return true;
        }else{
            return false;
        }
    }
}
