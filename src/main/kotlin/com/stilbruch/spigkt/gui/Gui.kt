@file:Suppress("unused")

package com.stilbruch.spigkt.gui

import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Gui private constructor(val plugin: JavaPlugin) : InventoryHolder {

    companion object {
        fun new(plugin: JavaPlugin, init: Gui.() -> Unit = {}): Gui {
            val gui = Gui(plugin)
            gui.apply(init)
            return gui
        }
    }

    private val inv: Inventory by lazy {
        val i = Bukkit.createInventory(this, rows * 9, title)!!
        slots.forEach { slot, item -> i.setItem(slot, item) }
        i
    }

    var title = ""
    var rows = 1
    var clickable = true

    private val slots = mutableMapOf<Int, ItemStack>()
    val slotEvents = mutableMapOf<Int, InventoryClickEvent.() -> Unit>()
    var onClose: (InventoryCloseEvent.() -> Unit)? = null
    var onOpen: (InventoryOpenEvent.() -> Unit)? = null

    fun setItem(itemStack: ItemStack, slot: Int, clickEvent: (InventoryClickEvent.() -> Unit)? = null) {
        slots[slot] = itemStack
        clickEvent?.let { slotEvents[slot] = it }
    }

    fun setItem(itemStack: ItemStack, vararg slots: Int, clickEvent: (InventoryClickEvent.() -> Unit)? = null) {
        slots.forEach { setItem(itemStack, it, clickEvent) }
    }

    fun fill(itemStack: ItemStack, clickEvent: (InventoryClickEvent.() -> Unit)? = null) {
        for (i in 0 until (rows * 9)){
            setItem(itemStack, i, clickEvent)
        }
    }

    override fun getInventory(): Inventory = inv

}