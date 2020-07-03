package br.edu.infnet.lightcontrol

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.edu.infnet.lightcontrol.model.ControlledLight
import kotlinx.android.synthetic.main.item_light.view.*

class ExtraFunctionalityAdapter : RecyclerView.Adapter<ExtraFunctionalityAdapter.SwitchViewHolder>() {

    var controlledLights = mutableListOf(ControlledLight())
    private lateinit var lightButtonListener: PowerLightSwitchClick

    fun addToArray(controlledLight: ControlledLight) {
        controlledLights.add(controlledLight)
        notifyDataSetChanged()
    }

    fun setPowerButtonClickListener(listener: PowerLightSwitchClick) {
        lightButtonListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwitchViewHolder {
        return SwitchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_master_switch, parent, false))
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: SwitchViewHolder, position: Int) {
        holder.bind(controlledLights[position], lightButtonListener)
    }

    fun setMasterSwitchState(masterSwitchState: Boolean) {
        controlledLights[0].state = if (masterSwitchState) 1 else 0
        notifyDataSetChanged()
    }

    class SwitchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(controlledLight: ControlledLight?, lightButtonListener: PowerLightSwitchClick?) {
            if (controlledLight?.state == 1) {
                itemView.button_action_light?.text = itemView.context.getString(R.string.turn_off)
                itemView.button_action_light?.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                itemView.button_action_light?.setOnClickListener { lightButtonListener?.onPowerButtonClick(false) }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    itemView.button_action_light?.background = ContextCompat.getDrawable(itemView.context, R.drawable.background_list_item_red)
                }
            } else {
                itemView.button_action_light?.text = itemView.context.getString(R.string.turn_on)
                itemView.button_action_light?.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.black))
                itemView.button_action_light?.setOnClickListener { lightButtonListener?.onPowerButtonClick(true) }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    itemView.button_action_light?.background = ContextCompat.getDrawable(itemView.context, R.drawable.background_list_item_green)
                }
            }
        }
    }
}

interface PowerLightSwitchClick {
    fun onPowerButtonClick(turnOn: Boolean)
}