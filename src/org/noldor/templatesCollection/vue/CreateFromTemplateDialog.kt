package org.noldor.templatesCollection.vue

import com.intellij.ide.actions.CreateFileAction
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import org.noldor.templatesCollection.BaseDialog
import org.noldor.templatesCollection.toKebabCase
import org.noldor.templatesCollection.toPascalCase
import org.noldor.templatesCollection.vue.forms.SingleFileComponentBindings
import org.noldor.templatesCollection.vue.forms.SingleFileComponentForm
import java.util.*
import javax.swing.JComponent

open class CreateFromTemplateDialog(
        private val form: SingleFileComponentForm,
        private val formBindings: SingleFileComponentBindings,
        private val project: Project,
        private val directory: PsiDirectory,
        private val template: FileTemplate
) : BaseDialog(form, formBindings, project, directory, template) {

    override fun getPreferredFocusedComponent(): JComponent {
        return form.name
    }

    override fun modifyProperties(enteredProperties: Properties) {
        enteredProperties["fileName"] = "component"
        enteredProperties["name"] = toPascalCase(enteredProperties["name"] as String)
    }

    override fun resolveDirectory(fileName: String): PsiDirectory {
        if (formBindings.createDirectory) {
            return CreateFileAction.findOrCreateSubdirectory(directory, toKebabCase(formBindings.name))
        } else {
            return super.resolveDirectory(fileName)
        }
    }

    override fun afterCreate(enteredProperties: Properties, directory: PsiDirectory) {
        createScriptFile(enteredProperties, directory)
        createTemplateFile(enteredProperties, directory)
        createStyleFile(enteredProperties, directory)
    }

    private fun createScriptFile(enteredProperties: Properties, directory: PsiDirectory) {
        val scriptTemplate = if (enteredProperties["classBasedComponent"] == true) {
            FileTemplateManager.getInstance(project).getInternalTemplate(
                    "VueSingleFileComponentScriptClassBased.${enteredProperties["scriptType"]}")
        } else {
            FileTemplateManager.getInstance(project).getInternalTemplate(
                    "VueSingleFileComponentScript.${enteredProperties["scriptType"]}")
        }

        FileTemplateUtil.createFromTemplate(
                scriptTemplate,
                "script",
                enteredProperties,
                directory
        )
    }

    private fun createTemplateFile(enteredProperties: Properties, directory: PsiDirectory) {
        val templateType = when (enteredProperties["templateType"]) {
            "pug" -> "Pug File"
            else -> "VueSingleFileComponentTemplateHtml"
        }

        FileTemplateUtil.createFromTemplate(
                FileTemplateManager.getInstance(project).getInternalTemplate(templateType),
                "template",
                enteredProperties,
                directory
        )
    }

    private fun createStyleFile(enteredProperties: Properties, directory: PsiDirectory) {
        if (enteredProperties["styleType"] === "none") return
        val styleType = when (enteredProperties["styleType"]) {
            "scss" -> "SCSS File"
            "stylus" -> "Stylus File"
            "sass" -> "Sass File"
            "less" -> "Less File"
            else -> "CSS File"
        }

        FileTemplateUtil.createFromTemplate(
                FileTemplateManager.getInstance(project).getInternalTemplate(styleType),
                "style",
                enteredProperties,
                directory
        )
    }
}
