package com.github.swalffy.roomig_codegen

import androidx.room.Database
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import java.lang.reflect.Type
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class DatabaseCreateTableSqlProcessor : AbstractProcessor() {

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Database::class.java.name)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
//        roundEnv?.getElementsAnnotatedWith(Database::class.java)
//            ?.forEach { element ->
//                val packageName =
////                    processingEnv.elementUtils.getPackageOf(element).toString() + "/" + element.simpleName.toString()
//                    processingEnv.elementUtils.getPackageOf(element).toString()
//
                val file = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
                    ?.let { File(it) } ?: error("Cant find kaptDir")
//
//                val className = "Test1_generated"
//
//                val statements = mutableListOf<String>()
//
//                element.getEntites().map {
//                    val kProperty0 = (it as Any).javaClass
//                    statements += "[$it] - [$kProperty0]"
//                }
//
//                val funSpec = FunSpec.builder("create_table_$className")
//                    .returns(String::class)
//
//                statements.forEach {
//                    funSpec.addComment(it)
//                }
//
//                // read json
//
//                val s = processingEnv.options["room.schemaLocation"]
//                    ?: error("Room schema location is not defined")
//
//
//                // end read json
//
//
//                funSpec.addStatement("return \"test123123\"")
//
//                FileSpec.builder(packageName, className)
//                    .addType(
//                        TypeSpec.classBuilder(className)
//                            .addFunction(funSpec.build())
//                            .build()
//                    ).build().writeTo(file)
//
//
//            }

        return false
    }

    private fun Element.getEntites() = this.getAnnotation(Database::class.java)?.let {
        ((this.annotationMirrors)
            .filter { it.annotationType.toString() == Database::class.java.name }
            .map { it.elementValues }
            .first()
            .filter { it.key.simpleName.toString() == Database::entities.name }
            .map { it.value }
            .first().value as List<AnnotationValue>)
            .map { ((it.value as DeclaredType).asElement() as TypeElement)}
    } ?: error("No Db annotation")

    private fun <T> T.toList() = listOf<T>(this)

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}