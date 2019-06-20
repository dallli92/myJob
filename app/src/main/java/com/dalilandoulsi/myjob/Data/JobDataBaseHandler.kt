package com.dalilandoulsi.myjob.Data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.format.DateFormat
import android.util.Log
import com.dalilandoulsi.myjob.Model.*
import java.util.*

class JobDataBaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
//sql structured query language
        var CREATE_JOB_TABLE =
            "CREATE TABLE " + TABLE_NAME + "( " + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_JOB_NAME + " TEXT," +
                    KEY_JOB_ASSIGNED_BY + " TEXT, " +
                    KEY_JOB_ASSIGNED_TO + " TEXT, " +
                    KEY_JOB_ASSIGNED_TIME + " LONG )"
        db?.execSQL(CREATE_JOB_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    //CRUD


    fun createJob(job: Job) {
        var db: SQLiteDatabase = writableDatabase
        var values: ContentValues = ContentValues()
        values.put(KEY_JOB_NAME, job.jobName)
        values.put(KEY_JOB_ASSIGNED_BY, job.assignedBy)
        values.put(KEY_JOB_ASSIGNED_TO, job.assignTo)
        values.put(KEY_JOB_ASSIGNED_TIME, System.currentTimeMillis())
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun readJob(id: Int): Job {
        var db: SQLiteDatabase = writableDatabase
        var cursor: Cursor = db.query(
            TABLE_NAME, arrayOf(
                KEY_ID, KEY_JOB_NAME, KEY_JOB_ASSIGNED_BY, KEY_JOB_ASSIGNED_TO,
                KEY_JOB_ASSIGNED_TIME
            ), KEY_ID + "=?", arrayOf(id.toString()), null, null, null, null
        )
        if (cursor != null) {
            cursor.moveToFirst()
        }
        var job = Job()
        job.jobName = cursor.getString(cursor.getColumnIndex(KEY_JOB_NAME))
        job.assignTo = cursor.getString(cursor.getColumnIndex(KEY_JOB_ASSIGNED_TO))
        job.assignedBy = cursor.getString(cursor.getColumnIndex(KEY_JOB_ASSIGNED_BY))
        job.timeAssign = cursor.getLong(cursor.getColumnIndex(KEY_JOB_ASSIGNED_TIME))

        var dateFormat: java.text.DateFormat = java.text.DateFormat.getDateInstance()
        var formattedDate = dateFormat.format(Date(cursor.getLong(cursor.getColumnIndex(KEY_JOB_ASSIGNED_TIME))).time)
        return job
    }


    fun updateJob(job: Job): Int {

        var db: SQLiteDatabase = writableDatabase
        var values: ContentValues = ContentValues()
        values.put(KEY_JOB_NAME, job.jobName)
        values.put(KEY_JOB_ASSIGNED_BY, job.assignedBy)
        values.put(KEY_JOB_ASSIGNED_TO, job.assignTo)
        values.put(KEY_JOB_ASSIGNED_TIME, System.currentTimeMillis())
        //update a row

        return db.update(TABLE_NAME, values, KEY_ID + "=?", arrayOf(job.id.toString()))
    }

    fun deleteJob(id: Int) {
        var db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_NAME, KEY_ID + "=?", arrayOf(id.toString()))
        db.close()

    }

    fun getJobsCount(): Int {
        var db: SQLiteDatabase = writableDatabase
        var countQuery = "SELECT * FROM " + TABLE_NAME
        var cursor: Cursor = db.rawQuery(countQuery, null)
        return cursor.count
    }

    fun readJobs(): ArrayList<Job> {


        var db: SQLiteDatabase = readableDatabase
        var list: ArrayList<Job> = ArrayList()

        //Select all chores from table
        var selectAll = "SELECT * FROM " + TABLE_NAME

        var cursor: Cursor = db.rawQuery(selectAll, null)

        //loop through our chores
        if (cursor.moveToFirst()) {
            do {
                var job = Job()

                job.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                job.jobName = cursor.getString(cursor.getColumnIndex(KEY_JOB_NAME))
                job.assignTo = cursor.getString(cursor.getColumnIndex(KEY_JOB_ASSIGNED_TO))
                job.timeAssign = cursor.getLong(cursor.getColumnIndex(KEY_JOB_ASSIGNED_TIME))
                job.assignedBy = cursor.getString(cursor.getColumnIndex(KEY_JOB_ASSIGNED_BY))
                list.add(job)

            } while (cursor.moveToNext())
        }


        return list

    }
}