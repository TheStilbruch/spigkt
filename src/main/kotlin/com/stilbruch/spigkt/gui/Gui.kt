@file:Suppress("unused")

package com.stilbruch.spigkt.gui

import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class Gui constructor(
    val title: String,
    val rows: Int,
    val clickable: Boolean = false,
    val onClose: (InventoryCloseEvent) -> Unit = {},
    val onOpen: (InventoryOpenEvent) -> Unit = {},
    init: Gui.() -> Unit = {}
) : InventoryHolder {

    private val slots = mutableMapOf<Int, ItemStack>()
    internal val slotEvents = mutableMapOf<Int, InventoryClickEvent.() -> Unit>()

    init {
        init()
    }

    fun setItem(itemStack: ItemStack, slot: Int, onClick: (InventoryClickEvent) -> Unit = {}) {
        slots[slot] = itemStack
        slotEvents[slot] = onClick
    }

    fun setItem(itemStack: ItemStack, slots: List<Int>, onClick: (InventoryClickEvent) -> Unit = {}) {
        slots.forEach { setItem(itemStack, it, onClick) }
    }

    fun fillRow(itemStack: ItemStack, row: Int, onClick: (InventoryClickEvent) -> Unit = {}) {
        val slots = (0 until 9).map { it + (9 * row) }
        setItem(itemStack, slots, onClick)
    }

    fun fillColumn(itemStack: ItemStack, column: Int, onClick: (InventoryClickEvent) -> Unit = {}) {
        val slots = (0 until rows).map { (it * 9) + column }
        setItem(itemStack, slots, onClick)
    }

    fun fill(itemStack: ItemStack, onClick: (InventoryClickEvent) -> Unit = {}) {
        for (i in 0 until (rows * 9)){
            setItem(itemStack, i, onClick)
        }
    }

    fun fillEmpty(itemStack: ItemStack, onClick: (InventoryClickEvent) -> Unit = {}) {
        for (i in 0 until (rows * 9)){
            if (!slots.containsKey(i)){
                setItem(itemStack, i, onClick)
            }
        }
    }

    override fun getInventory(): Inventory {
        val i = Bukkit.createInventory(this, rows * 9, title)!!
        slots.forEach { slot, item -> i.setItem(slot, item) }
        return i
    }

}