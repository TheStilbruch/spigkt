@file:Suppress("unused")

package com.stilbruch.spigkt.item

import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class RichItemStack private constructor(stack: ItemStack) : ItemStack(stack) {

    companion object {
        fun new(material: Material, func: RichItemStack.() -> Unit): RichItemStack {
            val stack = RichItemStack(ItemStack(material))
            stack.func()
            return stack
        }

        fun new(stack: ItemStack, func: RichItemStack.() -> Unit): RichItemStack {
            val stack = RichItemStack(stack)
            stack.func()
            return stack
        }
    }

    private fun changeMeta(func: ItemMeta.() -> Unit){
        val meta = itemMeta
        meta.func()
        itemMeta = meta
    }

    fun setName(name: String) {
        changeMeta {
            displayName = name
        }
    }

    fun getName(): String {
        return itemMeta.displayName
    }

    fun setLore(lore: List<String>) {
        changeMeta {
            setLore(lore)
        }
    }

    fun getLore(): List<String> {
        return itemMeta.lore
    }

    fun setColor(color: DyeColor) {
        durability = color.woolData.toShort()
    }

    fun addEnchant(enchant: Enchantment, level: Int){
        changeMeta {
            addEnchant(enchant, level, true)
        }
    }

    fun addFlag(itemFlag: ItemFlag){
        changeMeta {
            addItemFlags(itemFlag)
        }
    }

    fun unbreakable(state: Boolean = true){
        changeMeta {
            isUnbreakable = state
        }
    }
}