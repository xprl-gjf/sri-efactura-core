package ec.com.xprl.gradle

import org.gradle.api.Project
import kotlin.reflect.KProperty

/*
 * Adapted from https://gist.github.com/rpsrosario/7b7e6c697c0788f2f6f912945a6b11bb#file-gkb-06-kt
 */
internal inline fun <T, reified V> gradleProperty(project: Project, default: V? = null) =
    GradleProperty<T, V>(project, V::class.java, default)

internal class GradleProperty<T, V>(
    project: Project,
    type: Class<V>,
    default: V? = null
) {
    private val property = project.objects.property(type).apply {
        set(default)
    }

    operator fun getValue(thisRef: T, property: KProperty<*>): V =
        this.property.get()

    operator fun setValue(thisRef: T, property: KProperty<*>, value: V) =
        this.property.set(value)
}