package nl.shadowlink.tools.shadowmapper.gui.checklist

import nl.shadowlink.tools.shadowmapper.FileManager
import java.awt.event.*
import javax.swing.*
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

// @author Santhosh Kumar T - santhosh@in.fiorano.com
class CheckListManager<T>(
    private val list: JList<T>
) : MouseAdapter(), ListSelectionListener, ActionListener {

    private val selectionModel: ListSelectionModel = DefaultListSelectionModel()
    var hotspot = JCheckBox().preferredSize.width
    private var fm: FileManager? = null

    init {
        list.cellRenderer = CheckListCellRenderer(list.cellRenderer, selectionModel)
        list.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JComponent.WHEN_FOCUSED)
        list.addMouseListener(this)
        selectionModel.addListSelectionListener(this)
    }

    private fun toggleSelection(index: Int) {
        if (index < 0) return
        if (selectionModel.isSelectedIndex(index)) {
            selectionModel.removeSelectionInterval(index, index)
            fm!!.ipls[index].selected = false
        } else {
            selectionModel.addSelectionInterval(index, index)
            fm!!.ipls[index].selected = true
        }
    }

    override fun mouseClicked(me: MouseEvent) {
        val index = list.locationToIndex(me.point)
        if (index < 0) return
        if (me.x > list.getCellBounds(index, index).x + hotspot) return
        toggleSelection(index)
    }

    override fun valueChanged(e: ListSelectionEvent) {
        list.repaint(list.getCellBounds(e.firstIndex, e.lastIndex))
    }

    override fun actionPerformed(e: ActionEvent) {
        toggleSelection(list.selectedIndex)
    }

    fun setFileManager(fm: FileManager?) {
        this.fm = fm
    }
}