@file:Suppress("unused")

package com.stilbruch.spigkt.item

import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta

open class RichItemStack constructor(
    material: Material,
    name: String? = null,
    lore: List<String>? = null,
    enchantments: Map<Enchantment, Int>? = null,
    flags: Set<ItemFlag>? = null
) : ItemStack(material) {

    companion object {
        fun clone(
            itemStack: ItemStack,
            material: Material? = null,
            name: String? = null,
            lore: List<String>? = null,
            enchantments: Map<Enchantment, Int>? = null,
            flags: Set<ItemFlag>? = null
        ): RichItemStack {
            val richItem = itemStack as RichItemStack

            if (material != null) richItem.type = material
            if (name != null) richItem.setName(name)
            if (lore != null) richItem.setLore(lore)
            enchantments?.forEach(richItem::addEnchant)
            flags?.forEach(richItem::addFlag)

            return richItem
        }
    }

    init {
        if (name != null) setName(name)
        if (lore != null) setLore(lore)
        enchantments?.forEach(::addEnchant)
        flags?.forEach(::addFlag)
    }

    fun changeMeta(func: ItemMeta.() -> Unit){
        val meta = itemMeta!!
        meta.func()
        itemMeta = meta
    }

    fun setName(name: String) {
        changeMeta {
            setDisplayName(name)
        }
    }

    fun getName(): String {
        return itemMeta!!.displayName
    }

    fun setLore(lore: List<String>) {
        changeMeta {
            setLore(lore)
        }
    }

    fun getLore(): List<String>? {
        return itemMeta?.lore
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

    fun setSkullOwner(player: OfflinePlayer) {
        val meta = (itemMeta as SkullMeta)
        meta.owningPlayer = player
        this.itemMeta = meta
    }

    fun setSkullOwner(owner: String) {
        val meta = (itemMeta as SkullMeta)
        meta.owner = owner
        this.itemMeta = meta
    }
}