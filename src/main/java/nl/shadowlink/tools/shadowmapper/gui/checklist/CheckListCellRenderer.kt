package nl.shadowlink.tools.shadowmapper.gui.checklist

import java.awt.BorderLayout
import java.awt.Component
import javax.swing.*

// @author Santhosh Kumar T - santhosh@in.fiorano.com
class CheckListCellRenderer<T>(
    private val delegate: ListCellRenderer<T>,
    private val selectionModel: ListSelectionModel
) : JPanel(), ListCellRenderer<T> {

    private val checkBox = JCheckBox()

    init {
        layout = BorderLayout()
        isOpaque = false
        checkBox.isOpaque = false
    }

    override fun getListCellRendererComponent(
        list: JList<out T>?,
        value: T,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val renderer = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        checkBox.isSelected = selectionModel.isSelectedIndex(index)
        removeAll()
        add(checkBox, BorderLayout.WEST)
        add(renderer, BorderLayout.CENTER)
        return this
    }
}