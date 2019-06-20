package com.dalilandoulsi.myjob.Data

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dalilandoulsi.myjob.Activity.JobsListActivity
import com.dalilandoulsi.myjob.Model.Job
import com.dalilandoulsi.myjob.R
import kotlinx.android.synthetic.main.popup.view.*

class JobListAdapter(
    private val list: ArrayList<Job>, private val context: Context
) :
    RecyclerView.Adapter<JobListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_job, parent, false)
        return ViewHolder(view, context, list)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View, context: Context, list: ArrayList<Job>) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var mContext = context
        var mList = list

        var jobName = itemView.findViewById<TextView>(R.id.jobNameList)
        var jobAssignedBy = itemView.findViewById<TextView>(R.id.jobAssignedByList)
        var jobAssignedDate = itemView.findViewById<TextView>(R.id.jobAssignedTimeList)
        var jobAssignedto = itemView.findViewById<TextView>(R.id.jobAssignedToList)
        var deleteBtn = itemView.findViewById<Button>(R.id.deleteBtn)
        var editBtn = itemView.findViewById<Button>(R.id.editBtn)


        fun bindView(job: Job) {
            jobName.text = "Job: " + job.jobName
            jobAssignedBy.text = "Created by: " + job.assignedBy
            jobAssignedto.text = "Assigned to: " + job.assignTo
            jobAssignedDate.text = "Created: " + job.showDate(job.timeAssign!!)
            deleteBtn.setOnClickListener(this)
            editBtn.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var mPosition: Int = adapterPosition
            var job = mList[mPosition]

            when (v!!.id) {
                deleteBtn.id -> {
                    deleteJob(job.id!!)
                    mList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
                editBtn.id -> {
                    editJob(job)
                    notifyItemChanged(adapterPosition, job)
                }
            }
        }

        fun deleteJob(id: Int) {

            var db: JobDataBaseHandler = JobDataBaseHandler(mContext)
            db.deleteJob(id)
        }


        fun editJob(job: Job) {
            var dialogBuilder: AlertDialog.Builder? = null
            var dialog: AlertDialog? = null
            val dbHandler: JobDataBaseHandler = JobDataBaseHandler(context)
            var view = LayoutInflater.from(context).inflate(R.layout.popup, null)
            var jobName = view.jobETPop
            var jobAssignBy = view.bossET2Pop
            var jobAssignTo = view.employET2Pop
            var assignBtn = view.assignBtn2Pop
            dialogBuilder = AlertDialog.Builder(context).setView(view)
            dialog = dialogBuilder!!.create()
            dialog?.show()
            jobName.setText(job.jobName)
            jobAssignBy.setText(job.assignedBy)
            jobAssignTo.setText(job.assignTo)
            assignBtn.setOnClickListener {

                if (!TextUtils.isEmpty(jobName.text.toString().trim()) &&
                    !TextUtils.isEmpty(jobAssignBy.text.toString().trim()) &&
                    !TextUtils.isEmpty(jobAssignTo.text.toString().trim())
                ) {

                    //  var job = Job()
                    job.jobName = jobName.text.toString().trim()
                    job.assignedBy = jobAssignBy.text.toString().trim()
                    job.assignTo = jobAssignTo.text.toString().trim()

                    dbHandler?.updateJob(job)
                    notifyItemChanged(adapterPosition, job)

                    dialog?.dismiss()
                    /*startActivity(Intent(this, JobsListActivity::class.java))
                    finish()*/
                } else {

                }
            }
        }
    }


}