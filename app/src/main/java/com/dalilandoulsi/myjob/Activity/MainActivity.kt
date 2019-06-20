package com.dalilandoulsi.myjob.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.dalilandoulsi.myjob.Data.JobDataBaseHandler
import com.dalilandoulsi.myjob.Data.JobListAdapter
import com.dalilandoulsi.myjob.Model.Job
import com.dalilandoulsi.myjob.R
import kotlinx.android.synthetic.main.activity_jobs_list.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var dbHandler: JobDataBaseHandler? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle("Saving ...")
        dbHandler = JobDataBaseHandler(this)
        checkDB()

        assignBtn.setOnClickListener {

            progressDialog?.setMessage("Saving ...")
            progressDialog?.show()
            if (!TextUtils.isEmpty(JobET.text.toString()) &&
                !TextUtils.isEmpty(employET.text.toString()) &&
                !TextUtils.isEmpty(bossET.text.toString())
            ) {
                var job = Job()
                job.jobName = JobET.text.toString()
                job.assignedBy = employET.text.toString()
                job.assignTo = bossET.text.toString()

                saveToDb(job)
                progressDialog?.cancel()
                intent = Intent(this, JobsListActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "please fill the form", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveToDb(job: Job) {

        dbHandler?.createJob(job)
    }

    fun checkDB() {

        if (dbHandler!!.getJobsCount() > 0) {
            startActivity(Intent(this, JobsListActivity::class.java))
        }
    }
}

