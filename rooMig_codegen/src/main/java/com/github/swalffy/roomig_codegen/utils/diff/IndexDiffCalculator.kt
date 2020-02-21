package com.github.swalffy.roomig_codegen.utils.diff

import com.github.swalffy.roomig_codegen.model.diff.IndexDiff
import com.github.swalffy.roomig_codegen.model.schema.Index

class IndexDiffCalculator(
    private val fromIndices: List<Index>,
    private val toIndices: List<Index>
) {

    fun calculate(): IndexDiff? {
        val removedIndices = fromIndices.filter { oldIndex ->
            toIndices.none { it.name == oldIndex.name }
        }.toMutableList()

        val addedIndices = toIndices.filter { newIndex ->
            fromIndices.none { it.name == newIndex.name }
        }.toMutableList()

        (fromIndices + toIndices).groupBy { it.name }
            .filter { it.value.size == 2 }
            .forEach { (_, indices) ->
                takeIf {
                    indices.first().unique != indices.last().unique || indices.first().columnNames != indices.last().columnNames
                }?.let {
                    removedIndices += indices.last()
                    addedIndices += indices.last()
                }
            }

        return takeIf { removedIndices.isNotEmpty() || addedIndices.isNotEmpty() }
            ?.let {
                IndexDiff(
                    removed = removedIndices.map { it.name },
                    added = addedIndices
                )
            }
    }
}