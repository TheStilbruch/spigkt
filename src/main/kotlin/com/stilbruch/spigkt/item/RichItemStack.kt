@file:Suppress("unused")

package com.stilbruch.spigkt.item

import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class RichItemStack private constructor(material: Material) : ItemStack(material) {

    companion object {
        fun create(material: Material, func: RichItemStack.() -> Unit): RichItemStack {
            val stack = RichItemStack(material)
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

    fun setGlassColor(color: DyeColor) {
        durability = color.woolData.toShort()
    }

    fun setEnchant(enchant: Enchantment, level: Int){
        changeMeta {
            addEnchant(enchant, level, true)
        }
    }

    fun setFlag(itemFlag: ItemFlag){
        changeMeta {
            addItemFlags(itemFlag)
        }
    }
}