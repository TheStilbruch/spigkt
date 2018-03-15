@file:Suppress("unused")

package com.stilbruch.spigkt

import com.stilbruch.spigkt.gui.GUIListenerManager
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

abstract class KPlugin(val displayName: String) : JavaPlugin() {

    override fun onEnable() {
        registerListener(GUIListenerManager(this))
    }

    fun registerListener(listener: Listener) {
        SERVER.pluginManager.registerEvents(listener, this)
    }

}