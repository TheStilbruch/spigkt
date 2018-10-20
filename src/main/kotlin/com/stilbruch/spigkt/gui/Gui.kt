@file:Suppress("unused")

package com.stilbruch.spigkt.gui

import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class Gui private constructor() : InventoryHolder {

    companion object {
        fun new(init: Gui.() -> Unit = {}): Gui {
            val gui = Gui()
            gui.apply(init)
            return gui
        }
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

    fun fillRow(itemStack: ItemStack, row: Int, clickEvent: (InventoryClickEvent.() -> Unit)? = null) {
        val slots = (0 until 9).map { it + (9 * row) }.toIntArray()
        setItem(itemStack, *slots, clickEvent = clickEvent)
    }

    fun fillColumn(itemStack: ItemStack, column: Int, clickEvent: (InventoryClickEvent.() -> Unit)? = null) {
        val slots = (0 until rows).map { (it * 9) + column }.toIntArray()
        setItem(itemStack, *slots, clickEvent = clickEvent)
    }

    fun fill(itemStack: ItemStack, clickEvent: (InventoryClickEvent.() -> Unit)? = null) {
        for (i in 0 until (rows * 9)){
            setItem(itemStack, i, clickEvent)
        }
    }

    fun fillEmpty(itemStack: ItemStack, clickEvent: (InventoryClickEvent.() -> Unit)? = null) {
        for (i in 0 until (rows * 9)){
            if (!slots.containsKey(i)){
                setItem(itemStack, i, clickEvent)
            }
        }
    }

    override fun getInventory(): Inventory {
        val i = Bukkit.createInventory(this, rows * 9, title)!!
        slots.forEach { slot, item -> i.setItem(slot, item) }
        return i
    }

}