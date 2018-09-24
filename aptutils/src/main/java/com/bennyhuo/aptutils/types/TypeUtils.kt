package com.bennyhuo.aptutils.types

import com.bennyhuo.aptutils.AptContext
import com.squareup.javapoet.TypeName
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass
import com.squareup.kotlinpoet.TypeName as KotlinTypeName

/**
 * Created by benny on 2/3/18.
 */
object TypeUtils {

    internal fun doubleErasure(elementType: TypeMirror): String {
        var name = AptContext.types.erasure(elementType).toString()
        val typeParamStart = name.indexOf('<')
        if (typeParamStart != -1) {
            name = name.substring(0, typeParamStart)
        }
        return name
    }

    internal fun getTypeFromClassName(className: String) = AptContext.elements.getTypeElement(className).asType()
}

fun TypeElement.packageName() = if (this.enclosingElement.kind == ElementKind.PACKAGE) {
    enclosingElement.asType().toString()
} else {
    throw IllegalArgumentException(enclosingElement.toString())
}

fun TypeElement.simpleName(): String = simpleName.toString()

fun TypeMirror.simpleName() = TypeUtils.doubleErasure(this).let { name -> name.substring(name.lastIndexOf(".") + 1) }

fun TypeMirror.erasure() = AptContext.types.erasure(this)

//region subType
fun TypeMirror.isSubTypeOf(className: String): Boolean {
    return AptContext.types.isSubtype(this, TypeUtils.getTypeFromClassName(className))
}

fun TypeMirror.isSubTypeOf(cls: Class<*>): Boolean {
    return cls.canonicalName?.let {className ->
        isSubTypeOf(className)
    }?: false
}

fun TypeMirror.isSubTypeOf(cls: KClass<*>) = isSubTypeOf(cls.java)

fun TypeMirror.isSubTypeOf(typeMirror: TypeMirror): Boolean {
    return AptContext.types.isSubtype(this, typeMirror)
}
//endregion

//region sameType
fun TypeMirror.isSameTypeWith(typeMirror: TypeMirror): Boolean {
    return AptContext.types.isSameType(this, typeMirror)
}

fun TypeMirror.isSameTypeWith(cls: Class<*>): Boolean{
    return  cls.canonicalName?.let {className ->
        isSameTypeWith(className)
    }?: false
}

fun TypeMirror.isSameTypeWith(cls: KClass<*>) = isSameTypeWith(cls.java)

fun TypeMirror.isSameTypeWith(className: String): Boolean{
    return isSameTypeWith(TypeUtils.getTypeFromClassName(className))
}
//endregion

//region Class/KClass
fun Class<*>.asTypeMirror(): TypeMirror {
    return AptContext.elements.getTypeElement(canonicalName).asType()
}
fun Class<*>.asJavaTypeName() = this.asTypeMirror().asJavaTypeName()
fun Class<*>.asKotlinTypeName() = this.asTypeMirror().asKotlinTypeName()
fun Class<*>.asElement() = this.asTypeMirror().asElement()

fun KClass<*>.asTypeMirror(): TypeMirror {
    return AptContext.elements.getTypeElement(qualifiedName).asType()
}
fun KClass<*>.asJavaTypeName() = this.asTypeMirror().asJavaTypeName()
fun KClass<*>.asKotlinTypeName() = this.asTypeMirror().asKotlinTypeName()
fun KClass<*>.asElement() = this.asTypeMirror().asElement()
//endregion

//region TypeMirror
fun TypeMirror.asElement() = AptContext.types.asElement(this)

fun TypeMirror.asJavaTypeName() = TypeName.get(this)

fun TypeMirror.asKotlinTypeName(): KotlinTypeName {
    when (kind) {
        TypeKind.BOOLEAN -> return BOOLEAN
        TypeKind.BYTE -> return BYTE
        TypeKind.SHORT -> return SHORT
        TypeKind.INT -> return INT
        TypeKind.LONG -> return LONG
        TypeKind.CHAR -> return CHAR
        TypeKind.FLOAT -> return FLOAT
        TypeKind.DOUBLE -> return DOUBLE
        TypeKind.DECLARED -> if (toString() == "java.lang.String") {
            return STRING
        }
        TypeKind.ARRAY -> {
            val arrayType = this as ArrayType
            when (arrayType.componentType.kind) {
                TypeKind.BOOLEAN -> return BOOLEAN_ARRAY
                TypeKind.BYTE -> return BYTE_ARRAY
                TypeKind.SHORT -> return SHORT_ARRAY
                TypeKind.INT -> return INT_ARRAY
                TypeKind.LONG -> return LONG_ARRAY
                TypeKind.CHAR -> return CHAR_ARRAY
                TypeKind.FLOAT -> return FLOAT_ARRAY
                TypeKind.DOUBLE -> return DOUBLE_ARRAY
                TypeKind.DECLARED -> if (toString() == "java.lang.String[]") {
                    return STRING_ARRAY
                }
            }
        }
    }
    return asTypeName()
}

private val STRING: ClassName = ClassName("kotlin", "String")
private val STRING_ARRAY = ClassName("kotlin", "Array").parameterizedBy(STRING)
private val LONG_ARRAY: ClassName = ClassName("kotlin", "LongArray")
private val INT_ARRAY: ClassName = ClassName("kotlin", "IntArray")
private val SHORT_ARRAY: ClassName = ClassName("kotlin", "ShortArray")
private val BYTE_ARRAY: ClassName = ClassName("kotlin", "ByteArray")
private val CHAR_ARRAY: ClassName = ClassName("kotlin", "CharArray")
private val BOOLEAN_ARRAY: ClassName = ClassName("kotlin", "BooleanArray")
private val FLOAT_ARRAY: ClassName = ClassName("kotlin", "FloatArray")
private val DOUBLE_ARRAY: ClassName = ClassName("kotlin", "DoubleArray")
//endregion