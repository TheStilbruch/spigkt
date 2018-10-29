@file:Suppress("unused")

package com.stilbruch.spigkt.data

class Stack<T> constructor(list: MutableList<T>) {

    constructor() : this(mutableListOf<T>())

    var items: MutableList<T> = list

    fun isEmpty(): Boolean = this.items.isEmpty()

    fun count(): Int = this.items.count()

    fun push(element:T) {
        val position = this.count()
        this.items.add(position, element)
    }

    override  fun toString() = this.items.toString()

    fun pop():T? {
        return if (this.isEmpty()) {
            null
        } else {
            val item =  this.items.count() - 1
            this.items.removeAt(item)
        }
    }

    fun peek():T? {
        return if (isEmpty()) {
            null
        } else {
            this.items[this.items.count() - 1]
        }
    }

}