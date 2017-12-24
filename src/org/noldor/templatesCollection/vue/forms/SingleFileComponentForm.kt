package org.noldor.templatesCollection.vue.forms

import org.jetbrains.annotations.NotNull
import org.noldor.templatesCollection.FormBindingsObjectInterface
import org.noldor.templatesCollection.FormObjectInterface
import javax.swing.*

class SingleFileComponentForm : FormObjectInterface, JComponent() {
    override lateinit var panel: JPanel
    lateinit var labelName: JLabel
    lateinit var labelScriptType: JLabel
    lateinit var name: JTextField
    lateinit var createDirectory: JCheckBox
    lateinit var scriptType: JComboBox<String>
    lateinit var classBasedComponent: JCheckBox
    lateinit var labelTemplateType: JLabel
    lateinit var templateType: JComboBox<String>
    lateinit var labelStyleType: JLabel
    lateinit var styleType: JComboBox<String>
    lateinit var scopedStyles: JCheckBox
    lateinit var functionalComponent: JCheckBox

    fun setData(data: SingleFileComponentBindings) {
        name.text = data.name
        createDirectory.isSelected = data.createDirectory
        scriptType.selectedItem = data.scriptType
        classBasedComponent.isSelected = data.classBasedComponent
        templateType.selectedItem = data.templateType
        styleType.selectedItem = data.styleType
        scopedStyles.isSelected = data.scopedStyles
        functionalComponent.isSelected = data.functionalComponent
    }

    override fun getData(@NotNull data: FormBindingsObjectInterface) {
        writeData(data as SingleFileComponentBindings)
    }

    private fun writeData(data: SingleFileComponentBindings) {
        data.name = name.text
        data.createDirectory = createDirectory.isSelected
        data.scriptType = scriptType.selectedItem as String
        data.classBasedComponent = classBasedComponent.isSelected
        data.templateType = templateType.selectedItem as String
        data.styleType = styleType.selectedItem as String
        data.scopedStyles = scopedStyles.isSelected
        data.functionalComponent = functionalComponent.isSelected
    }
}
