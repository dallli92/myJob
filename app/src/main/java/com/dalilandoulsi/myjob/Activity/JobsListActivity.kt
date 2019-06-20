package com.dalilandoulsi.myjob.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.dalilandoulsi.myjob.Data.JobDataBaseHandler
import com.dalilandoulsi.myjob.Data.JobListAdapter
import com.dalilandoulsi.myjob.Model.Job
import com.dalilandoulsi.myjob.R
import kotlinx.android.synthetic.main.activity_jobs_list.*
import kotlinx.android.synthetic.main.popup.view.*

class JobsListActivity : AppCompatActivity() {
    private var adapter: JobListAdapter? = null
    private var jobsList: ArrayList<Job>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var jobListItem: ArrayList<Job>? = null
    var dbHandler: JobDataBaseHandler? = null
    private var dialogBuilder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs_list)
        jobsList = ArrayList<Job>()
        jobListItem = ArrayList<Job>()
        layoutManager = LinearLayoutManager(this)
        adapter = JobListAdapter(jobListItem!!, this)
        dbHandler = JobDataBaseHandler(this)

        myList.layoutManager = layoutManager
        myList.adapter = adapter

        jobsList = dbHandler?.readJobs()
        jobsList!!.reverse()

        for (j in jobsList!!.iterator()) {

            val job = Job()
            job.jobName = j.jobName
            job.assignTo = j.assignTo
            job.assignedBy = j.assignedBy
            job.id = j.id
            job.timeAssign = j.timeAssign
            jobListItem!!.add(job)

            Log.d("ttt", j.timeAssign.toString())
        }
        adapter!!.notifyDataSetChanged()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.add_menu) {
            createPopUpDialog()

        }
        return super.onOptionsItemSelected(item)

    }

    fun createPopUpDialog() {

        var view = layoutInflater.inflate(R.layout.popup, null)
        var jobName = view.jobETPop
        var jobAssignBy = view.bossET2Pop
        var jobAssignTo = view.employET2Pop
        var assignBtn = view.assignBtn2Pop
        dialogBuilder = AlertDialog.Builder(this).setView(view)
        dialog = dialogBuilder!!.create()
        dialog?.show()

        assignBtn.setOnClickListener {

            if (!TextUtils.isEmpty(jobName.text.toString().trim()) &&
                !TextUtils.isEmpty(jobAssignBy.text.toString().trim()) &&
                !TextUtils.isEmpty(jobAssignTo.text.toString().trim())
            ) {

                var job = Job()
                job.jobName = jobName.text.toString().trim()
                job.assignedBy = jobAssignBy.text.toString().trim()
                job.assignTo = jobAssignTo.text.toString().trim()

                dbHandler?.createJob(job)
                dialog?.dismiss()
                startActivity(Intent(this, JobsListActivity::class.java))
                finish()
            } else {

            }
        }
    }
}
