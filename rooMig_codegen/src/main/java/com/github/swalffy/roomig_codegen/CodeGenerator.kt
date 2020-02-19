package com.github.swalffy.roomig_codegen

import com.github.swalffy.annotation.MyClass
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

//@AutoService(Processor::class)
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//class CodeGenerator : AbstractProcessor() {
//
//    override fun getSupportedSourceVersion(): SourceVersion {
//        return SourceVersion.latest()
//    }
//
//    override fun getSupportedAnnotationTypes(): MutableSet<String> {
//        return mutableSetOf(MyClass::class.java.name)
//    }
//
//    override fun process(
//        annotations: MutableSet<out TypeElement>?,
//        roundEnv: RoundEnvironment?
//    ): Boolean {
//        roundEnv?.getElementsAnnotatedWith(MyClass::class.java)
//            ?.forEach {
//                val packageName = processingEnv.elementUtils.getPackageOf(it).toString()
//
//                val className = it.simpleName.toString() + "_generated"
//                val kaptKotlinGeneratedDir =
//                    processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
//                        ?: error("Cant find kaptDir")
//                val file = File(kaptKotlinGeneratedDir)
//
//                FileSpec.builder(packageName, className)
//                    .addType(
//                        TypeSpec.classBuilder(className)
//                            .addFunction(
//                                FunSpec.builder("greeting")
//                                    .returns(String::class)
//                                    .addStatement("return \"NewGreetings\"")
//                                    .build()
//                            )
//                            .build()
//                    )
//                    .build()
//                    .writeTo(file)
//            }
//
//        return true
//    }
//
//
//    private fun generateClass(className: String, pack: String) {
//        val fileName = "Generated_$className"
//        val fileContent = KotlinClassBuilder(fileName, pack).getContent()
//        FunSpec
//        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
//        val file = File(kaptKotlinGeneratedDir, "$fileName.kt")
//
//        file.writeText(fileContent)
//    }
//
//    companion object {
//        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
//    }
//}