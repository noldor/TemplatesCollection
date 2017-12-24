package org.noldor.templatesCollection

import com.intellij.ide.IdeBundle
import com.intellij.ide.actions.CreateFileAction
import com.intellij.ide.fileTemplates.FileTemplate
import org.noldor.templatesCollection.FileTemplateUtil
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import org.jetbrains.annotations.NonNls
import java.util.*
import javax.swing.JComponent
import kotlin.reflect.full.memberProperties

abstract class BaseDialog(
        private val form: FormObjectInterface,
        private val formBindings: FormBindingsObjectInterface,
        private val project: Project,
        private val directory: PsiDirectory,
        private val template: FileTemplate) : DialogWrapper(project, true) {
    private var createdElement: PsiElement? = null

    private val errorMessage: String
        get() = FileTemplateUtil.findHandler(template).errorMessage

    open fun beforeInit() {}
    open fun afterInit() {}
    open fun beforeShow() {}
    open fun afterShow() {}
    open fun beforeCreate(enteredProperties: Properties, directory: PsiDirectory) {}
    open fun afterCreate(enteredProperties: Properties, directory: PsiDirectory) {}
    open fun modifyProperties(enteredProperties: Properties) {}

    open fun resolveDirectory(fileName: String): PsiDirectory {
        val dir = WriteAction.compute<CreateFileAction.MkDirs, Throwable> {
            CreateFileAction.MkDirs(fileName, directory)
        }

        return dir.directory
    }


    init {
        title = IdeBundle.message("title.new.from.template", template.name)

        beforeInit()
        init()
        afterInit()
    }

    fun create(): PsiElement? {
        beforeShow()
        show()
        afterShow()
        return createdElement
    }

    override fun doOKAction() {
        doCreate()
        if (createdElement != null) {
            super.doOKAction()
        }
    }

    private fun doCreate() {
        try {
            val properties = enteredProperties()
            modifyProperties(properties)
            val name = properties["fileName"] as String
            val directory = resolveDirectory(name)
            beforeCreate(properties, directory)
            createdElement = FileTemplateUtil.createFromTemplate(template, name, properties, directory)
            afterCreate(properties, directory)
        } catch (e: Exception) {
            showErrorDialog(e)
        }
    }

    private fun enteredProperties(): Properties {
        form.getData(formBindings)

        val properties = Properties()

        formBindings.javaClass.kotlin.memberProperties.forEach {
            properties[it.name] = it.getter.call(formBindings)
        }

        return properties
    }

    private fun showErrorDialog(e: Exception) {
        LOG.info(e)
        Messages.showMessageDialog(project, filterMessage(e.message), errorMessage, Messages.getErrorIcon())
    }

    private fun filterMessage(message: String?): String? {
        var newMessage = message
        if (newMessage == null) {
            newMessage = "unknown error"
        }

        @NonNls val ioExceptionPrefix = "java.io.IOException:"
        if (newMessage.startsWith(ioExceptionPrefix)) {
            return newMessage.substring(ioExceptionPrefix.length)
        }
        return if (newMessage.contains("File already exists")) {
            message
        } else IdeBundle.message("error.unable.to.parse.template.message", template.name, message)

    }

    override fun createCenterPanel(): JComponent {
        return form.panel
    }

    companion object {
        private val LOG = Logger.getInstance("#com.intellij.ide.fileTemplates.ui.CreateFromTemplateDialog")
    }
}
