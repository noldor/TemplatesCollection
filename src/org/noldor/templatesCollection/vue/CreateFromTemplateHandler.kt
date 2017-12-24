package org.noldor.templatesCollection.vue

import com.intellij.ide.fileTemplates.DefaultCreateFromTemplateHandler
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx
import org.jetbrains.vuejs.VueFileType
import org.noldor.templatesCollection.vue.CreateSingleFileComponentAction.Companion.VUE_TEMPLATE_NAME

class CreateFromTemplateHandler : DefaultCreateFromTemplateHandler() {
    override fun handlesTemplate(template: FileTemplate?): Boolean {
        template ?: return false
        val fileType = FileTypeManagerEx.getInstanceEx().getFileTypeByExtension(template.extension)
        return VueFileType.INSTANCE == fileType && VUE_TEMPLATE_NAME == template.name
    }
}
