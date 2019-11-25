package br.edu.infnet.lightcontrol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_log.view.*

class LogAdapter : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    private val logList: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        return LogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false))
    }

    override fun getItemCount(): Int {
        return logList.size
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(logList[position])
    }

    fun addLog(msg: String) {
        logList.add(msg)
        notifyDataSetChanged()
    }

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(text: String?) {
            itemView.item_log_text?.text = text
        }
    }

}
