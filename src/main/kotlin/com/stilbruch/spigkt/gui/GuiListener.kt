@file:Suppress("unused")

package com.stilbruch.spigkt.gui

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.plugin.java.JavaPlugin

class GuiListener(private val plugin: JavaPlugin): Listener {

    @EventHandler
    fun onClick(e: InventoryClickEvent) {

        if (e.currentItem == null) return

        val slot = e.slot
        val gui: Gui = e.inventory.holder as? Gui ?: return
        if (!gui.clickable) e.isCancelled = true

        gui.slotEvents[slot]?.invoke(e)
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        val gui: Gui = (e.inventory.holder as? Gui) ?: return
        gui.onClose(e)
    }

    @EventHandler
    fun onOpen(e: InventoryOpenEvent) {
        val gui: Gui = e.inventory.holder as? Gui ?: return
        gui.onOpen(e)
    }
}
