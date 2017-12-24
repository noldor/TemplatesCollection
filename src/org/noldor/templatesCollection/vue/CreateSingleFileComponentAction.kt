package org.noldor.templatesCollection.vue

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import icons.VuejsIcons
import org.noldor.templatesCollection.BaseCreateFromTemplate
import org.noldor.templatesCollection.BaseDialog
import org.noldor.templatesCollection.vue.forms.SingleFileComponentBindings
import org.noldor.templatesCollection.vue.forms.SingleFileComponentForm

class CreateSingleFileComponentAction : DumbAware, BaseCreateFromTemplate(text, description, icon) {
    companion object {
        val VUE_TEMPLATE_NAME = "VueSingleFileComponent.vue"
        private val text = "Vue single file component"
        private val description = "Vue single file component"
        private val icon = VuejsIcons.Vue
    }

    override fun createDialog(project: Project, directory: PsiDirectory, template: FileTemplate): BaseDialog {
        return CreateFromTemplateDialog(SingleFileComponentForm(), SingleFileComponentBindings(), project, directory, template)
    }

    override protected val templateName: String = VUE_TEMPLATE_NAME
}
