package org.noldor.templatesCollection

import com.intellij.ide.fileTemplates.CreateFromTemplateHandler
import com.intellij.ide.fileTemplates.DefaultCreateFromTemplateHandler
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.extensions.Extensions
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import java.util.*

class FileTemplateUtil : FileTemplateUtil() {
    companion object {
        @Throws(Exception::class)
        fun createFromTemplate(template: FileTemplate,
                               fileName: String?,
                               props: Properties?,
                               directory: PsiDirectory): PsiElement {
            val map: Map<String, Any>?
            if (props != null) {
                map = HashMap()
                for (propertyName in props.propertyNames()) {
                    map[propertyName as String] = props[propertyName] as Any
                }
            } else {
                map = null
            }

            return createFromTemplate(template, fileName, map, directory, null)
        }

        private val DEFAULT_HANDLER = DefaultCreateFromTemplateHandler()

        fun findHandler(template: FileTemplate): CreateFromTemplateHandler {
            for (handler in Extensions.getExtensions(CreateFromTemplateHandler.EP_NAME)) {
                if (handler.handlesTemplate(template)) {
                    return handler
                }
            }
            return DEFAULT_HANDLER
        }
    }
}
