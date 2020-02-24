package com.github.swalffy.roomig_codegen

import com.github.swalffy.annotation.GenerateMigrations
import com.github.swalffy.roomig_codegen.model.schema.DatabaseSchema
import com.github.swalffy.roomig_codegen.model.schema.SchemaRoot
import com.github.swalffy.roomig_codegen.utils.ClassNameManager
import com.github.swalffy.roomig_codegen.utils.RoomCodegenSupport
import com.google.auto.service.AutoService
import com.google.gson.Gson
import com.squareup.kotlinpoet.*
import java.io.File
import java.io.InputStreamReader
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class RoomMigrationGenProcessor : AbstractProcessor() {

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(GenerateMigrations::class.java.name)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        val file = processingEnv.kaptDir

        roundEnv?.getElementsAnnotatedWith(GenerateMigrations::class.java)
            ?.filterIsInstance<TypeElement>()
            ?.forEach {
                val schemas = readSchemas(it.asClassName().toString())
                    .sortedBy { it.version }

                val entityClass = it.asClassName()
                val namingManager = ClassNameManager(
                    packageName = entityClass.packageName,
                    elementName = entityClass.simpleName
                )

                val zipWithNext = schemas.zipWithNext { a, b ->
                    a.version to b.version
                }.toMap()

//                TODO(zipWithNext.toString())
//


                schemas.zipWithNext { a, b ->
                    MigrationGenerator(namingManager, a, b)
                }.onEach { it.writeTo(file) }
                    .map(MigrationGenerator::className)
                    .run(::generateMigrationsHolder)

            }

        return true
    }

    private fun readSchemas(elementName: String): List<DatabaseSchema> {
        val schemaFolderPath = processingEnv.options["room.schemaLocation"]
            ?: error("Please define room schema location in gradle config")

        val schemaFiles = File(schemaFolderPath, elementName).takeIf { it.isDirectory }
            ?.listFiles() ?: error("Can't find schemas folder")
        val gson = Gson()

        return schemaFiles.mapNotNull {
            runCatching {
                InputStreamReader(it.inputStream(), "UTF-8").use { stream ->
                    gson.fromJson<SchemaRoot>(stream, SchemaRoot::class.java).database
                }
            }.getOrNull()
        }
    }

    private fun generateMigrationsHolder(migrations: List<ClassName>) {
        migrations.firstOrNull()?.let {
            val className = "MigrationHolder"
            val arrayOfMigrationsType =
                ParameterizedTypeName.run { ARRAY.parameterizedBy(RoomCodegenSupport.MIGRATION_TYPE) }
            val listOfMigrationsType =
                ParameterizedTypeName.run {
                    ArrayList::class.asClassName()
                        .parameterizedBy(RoomCodegenSupport.MIGRATION_TYPE)
                }

            val buildFun = FunSpec.builder("build")
                .returns(arrayOfMigrationsType)
                .addStatement("val result = %T()", listOfMigrationsType)
            migrations.forEach { buildFun.addStatement("result += %T()", it) }
            buildFun.addStatement("return result.toTypedArray()")

            FileSpec.builder(it.packageName, className)
                .addType(
                    TypeSpec.objectBuilder(className)
                        .addFunction(buildFun.build())
                        .build()
                )
                .build().writeTo(processingEnv.kaptDir)
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

    private val ProcessingEnvironment.kaptDir: File
        get() = options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
            ?.let(::File) ?: error("Cant find kaptDir")

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}