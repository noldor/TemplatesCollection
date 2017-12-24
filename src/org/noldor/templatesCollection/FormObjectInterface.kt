package org.noldor.templatesCollection

import javax.swing.JPanel

interface FormObjectInterface {
    val panel: JPanel

    fun getData(data: FormBindingsObjectInterface)
}
