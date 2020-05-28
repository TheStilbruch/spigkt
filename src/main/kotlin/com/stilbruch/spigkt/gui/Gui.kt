@file:Suppress("unused")

package com.stilbruch.spigkt.gui

import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

abstract class Gui constructor(
    private val title: String,
    private val rows: Int,
    internal val clickable: Boolean = false,
    private val inventoryType: InventoryType? = null
) : InventoryHolder {

    private val slots = mutableMapOf<Int, ItemStack>()
    internal val slotEvents = mutableMapOf<Int, InventoryClickEvent.() -> Unit>()

    open fun onClose(e: InventoryCloseEvent) { }
    open fun onOpen(e: InventoryOpenEvent) { }

    protected fun setItem(itemStack: ItemStack, slot: Int, onClick: ((InventoryClickEvent) -> Unit)? = null) {
        slots[slot] = itemStack
        if (onClick != null) slotEvents[slot] = onClick
    }

    protected fun setItems(itemStack: ItemStack, slots: List<Int>, onClick: ((InventoryClickEvent) -> Unit)? = null) {
        slots.forEach { setItem(itemStack, it, onClick) }
    }

    protected fun fillRow(itemStack: ItemStack, row: Int, onClick: ((InventoryClickEvent) -> Unit)? = null) {
        val slots = (0 until 9).map { it + (9 * row) }
        setItems(itemStack, slots, onClick)
    }

    protected fun fillColumn(itemStack: ItemStack, column: Int, onClick: ((InventoryClickEvent) -> Unit)? = null) {
        val slots = (0 until rows).map { (it * 9) + column }
        setItems(itemStack, slots, onClick)
    }

    protected fun fill(itemStack: ItemStack, onClick: ((InventoryClickEvent) -> Unit)? = null) {
        for (i in 0 until (rows * 9)){
            setItem(itemStack, i, onClick)
        }
    }

    protected fun fillEmpty(itemStack: ItemStack, onClick: ((InventoryClickEvent) -> Unit)? = null) {
        for (i in 0 until (rows * 9)){
            if (!slots.containsKey(i)){
                setItem(itemStack, i, onClick)
            }
        }
    }

    override fun getInventory(): Inventory {
        val i = if (inventoryType != null)
            Bukkit.createInventory(this, inventoryType, title)
        else
            Bukkit.createInventory(this, rows * 9, title)
        slots.forEach { (slot, item) -> i.setItem(slot, item) }
        return i
    }

}