package com.github.swalffy.roomig_codegen.utils.diff

import com.github.swalffy.roomig_codegen.model.diff.PrimaryKeyDiff
import com.github.swalffy.roomig_codegen.model.schema.PrimaryKey

class PrimaryKeyDiffCalculator(
    private val fromPrimaryKey: PrimaryKey,
    private val toPrimaryKey: PrimaryKey
) {
    fun calculate(): PrimaryKeyDiff? {
        return takeIf { fromPrimaryKey != toPrimaryKey }?.let {
            PrimaryKeyDiff(fromPrimaryKey, toPrimaryKey)
        }
    }
}