package br.edu.infnet.lightcontrol

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.edu.infnet.lightcontrol.model.ControlledLight
import br.edu.infnet.lightcontrol.model.Payload
import kotlinx.android.synthetic.main.item_light.view.*

class LightControlAdapter : RecyclerView.Adapter<LightControlAdapter.LightViewHolder>() {

    private val controlledLights: ArrayList<ControlledLight> = ArrayList()
    private lateinit var lightButtonListener: PowerLightButtonClick

    fun addToArray(payload: Payload) {
        for (block in payload.blocks) {
            for (floor in block.floors) {
                for (controlledLight in floor.controlled_lights) {
                    controlledLight.floor = floor.number
                    controlledLight.block = block.number
                    controlledLight.blockName = block.name
                    controlledLight.floorName = floor.name
                    controlledLights.add(controlledLight)
                }
            }
        }
        notifyDataSetChanged()
    }

    fun setPowerButtonClickListener(listener: PowerLightButtonClick) {
        lightButtonListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightViewHolder {
        return LightViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_light, parent, false))
    }

    override fun getItemCount(): Int {
        return controlledLights.size
    }

    override fun onBindViewHolder(holder: LightViewHolder, position: Int) {
        holder.bind(controlledLights[position], lightButtonListener)
    }

    fun clearArray() {
        controlledLights.clear()
    }

    class LightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(controlledLight: ControlledLight?, lightButtonListener: PowerLightButtonClick?) {
            itemView.text_light_status?.text = controlledLight?.area
            itemView.text_light_block?.text = controlledLight?.blockName
            itemView.text_light_floor?.text = controlledLight?.floorName
            if (controlledLight?.state == 1) {
                itemView.button_action_light?.text = itemView.context.getString(R.string.turn_off)
                itemView.button_action_light?.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                itemView.image_light_status?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_light_on))
                itemView.button_action_light?.setOnClickListener { lightButtonListener?.onPowerButtonClick(controlledLight, false) }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    itemView.button_action_light?.background = ContextCompat.getDrawable(itemView.context, R.drawable.background_list_item_red)
                }
            } else {
                itemView.button_action_light?.text = itemView.context.getString(R.string.turn_on)
                itemView.image_light_status?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_light_off))
                itemView.button_action_light?.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.black))
                itemView.button_action_light?.setOnClickListener { lightButtonListener?.onPowerButtonClick(controlledLight!!, true) }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    itemView.button_action_light?.background = ContextCompat.getDrawable(itemView.context, R.drawable.background_list_item_green)
                }
            }
        }
    }

}

interface PowerLightButtonClick {
    fun onPowerButtonClick(controlledLight: ControlledLight, turnOn: Boolean)
}
