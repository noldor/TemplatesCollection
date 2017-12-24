package org.noldor.templatesCollection

import com.intellij.ide.IdeView
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.util.DirectoryChooserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.application.WriteActionAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import javax.swing.Icon

abstract class BaseCreateFromTemplate(title: String, description: String, icon: Icon)
    : WriteActionAware, AnAction(title, description, icon) {
    abstract protected val templateName: String
    abstract protected fun createDialog(project: Project, directory: PsiDirectory, template: FileTemplate): BaseDialog

    override fun actionPerformed(e: AnActionEvent) {
        val dataContext = e.dataContext
        val view = LangDataKeys.IDE_VIEW.getData(dataContext) ?: return
        val dir = getTargetDirectory(view)
        val project = dir!!.project
        val selectedTemplate = getTemplate(project)
        FileTemplateManager.getInstance(project).addRecentName(selectedTemplate.name)
        val dialog = createDialog(project, dir, selectedTemplate)

        val createdElement = dialog.create() ?: return
        view.selectElement(createdElement)
    }

    private fun getTemplate(project: Project): FileTemplate {
        return FileTemplateManager.getInstance(project).getInternalTemplate(templateName)
    }

    private fun getTargetDirectory(view: IdeView): PsiDirectory? {
        return DirectoryChooserUtil.getOrChooseDirectory(view)
    }
}
