package com.dalilandoulsi.myjob.Model

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Job() {
    var jobName: String? = null
    var assignedBy: String? = null
    var assignTo: String? = null
    var timeAssign: Long? = null
    var id: Int? = null

    constructor(jobName: String, assignedBy: String, assignTo: String, timeAssign: Long, id: Int) : this() {
        this.jobName = jobName
        this.assignedBy = assignedBy
        this.assignTo = assignTo
        this.timeAssign = timeAssign
        this.id = id
    }


    fun showDate(timeAssign: Long): String {
        val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm")
        val resultdate = Date(timeAssign)

        return sdf.format(resultdate)
    }

}