package com.github.swalffy.roomig_codegen.utils.diff

import com.github.swalffy.roomig_codegen.model.diff.FieldDiff
import com.github.swalffy.roomig_codegen.model.schema.Field

class FieldDiffCalculator(
    private val fromField: Field,
    private val toField: Field
) {
    fun calculate(): FieldDiff? {
        val typeChanged = fromField.type != toField.type
        val notNullChanged = fromField.notNull != toField.notNull

        return takeIf { typeChanged || notNullChanged }?.let {
            FieldDiff(
                field = toField,
                typeChanged = typeChanged,
                notNullChanged = notNullChanged
            )
        }
    }
}