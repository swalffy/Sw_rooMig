package com.github.swalffy.roomig_codegen

import com.github.swalffy.annotation.MyClass
import com.github.swalffy.roomig_codegen.utils.ClassNameManager
import com.github.swalffy.roomig_codegen.model.schema.DatabaseSchema
import com.github.swalffy.roomig_codegen.model.schema.SchemaRoot
import com.google.auto.service.AutoService
import com.google.gson.Gson
import com.squareup.kotlinpoet.asClassName
import java.io.File
import java.io.InputStreamReader
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class RoomMigrationGenProcessor : AbstractProcessor() {

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(MyClass::class.java.name)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        val file =
            processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
                ?.let { File(it) } ?: error("Cant find kaptDir")

        roundEnv?.getElementsAnnotatedWith(MyClass::class.java)
            ?.filterIsInstance<TypeElement>()
            ?.forEach {
                val schemas = readSchemas(it.asClassName().toString())
                    .sortedBy { it.version }
                    .notRepeated

                val entityClass = it.asClassName()
                val namingManager =
                    ClassNameManager(
                        packageName = entityClass.packageName,
                        elementName = entityClass.simpleName
                    )

                (0..(schemas.size - 2)).map { schemaIndex ->
                    MigrationGenerator(
                        namingManager,
                        schemas[schemaIndex],
                        schemas[schemaIndex + 1]
                    )
                }.onEach { it.generateTo(file) }
            }

        return true
    }

    private fun readSchemas(elementName: String): List<DatabaseSchema> {
        val schemaFolderPath = processingEnv.options["room.schemaLocation"]
            ?: error("Please define room schema location in gradle config")

        val schemaFiles = File(schemaFolderPath, elementName).takeIf { it.isDirectory }
            ?.listFiles() ?: error("Can't find schemas folted")
        val gson = Gson()

        return schemaFiles.mapNotNull {
            runCatching {
                InputStreamReader(it.inputStream(), "UTF-8").use { stream ->
                    gson.fromJson<SchemaRoot>(stream, SchemaRoot::class.java).database
                }
            }.getOrNull()
        }
    }

    private val List<DatabaseSchema>.notRepeated: List<DatabaseSchema>
        get() = mutableListOf<DatabaseSchema>().also { result ->
            (this.indices).forEach {
                if (it == 0 || this[it - 1].identityHash != this[it].identityHash) {
                    result.add(this[it])
                }
            }
        }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}