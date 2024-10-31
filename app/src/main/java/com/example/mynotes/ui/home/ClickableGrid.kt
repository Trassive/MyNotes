package com.example.mynotes.ui.home

interface ClickableGrid {
    fun onItemClick(position: Int)
    fun onItemLongClick(position: Int): Boolean
}