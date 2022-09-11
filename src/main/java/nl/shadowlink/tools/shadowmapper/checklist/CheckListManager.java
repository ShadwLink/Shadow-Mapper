package nl.shadowlink.tools.shadowmapper.checklist;

// @author Santhosh Kumar T - santhosh@in.fiorano.com

import nl.shadowlink.tools.shadowmapper.gui.FileManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

public class CheckListManager extends MouseAdapter implements ListSelectionListener, ActionListener {
    private ListSelectionModel selectionModel = new DefaultListSelectionModel();
    private JList list = new JList();
    int hotspot = new JCheckBox().getPreferredSize().width;
    private FileManager fm;

    public CheckListManager(JList list) {
        this.list = list;
        list.setCellRenderer(new CheckListCellRenderer(list.getCellRenderer(), selectionModel));
        list.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JComponent.WHEN_FOCUSED);
        list.addMouseListener(this);
        selectionModel.addListSelectionListener(this);
    }

    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }

    private void toggleSelection(int index) {
        if (index < 0)
            return;

        if (selectionModel.isSelectedIndex(index)) {
            selectionModel.removeSelectionInterval(index, index);
            fm.ipls.get(index).selected = false;
        } else {
            selectionModel.addSelectionInterval(index, index);
            fm.ipls.get(index).selected = true;
        }
    }

    /*------------------------------[ MouseListener ]-------------------------------------*/

    public void mouseClicked(MouseEvent me) {
        int index = list.locationToIndex(me.getPoint());
        if (index < 0)
            return;
        if (me.getX() > list.getCellBounds(index, index).x + hotspot)
            return;
        toggleSelection(index);
    }

    /*-----------------------------[ ListSelectionListener ]---------------------------------*/

    public void valueChanged(ListSelectionEvent e) {
        list.repaint(list.getCellBounds(e.getFirstIndex(), e.getLastIndex()));
    }

    /*-----------------------------[ ActionListener ]------------------------------*/

    public void actionPerformed(ActionEvent e) {
        toggleSelection(list.getSelectedIndex());
    }

    public void setFileManager(FileManager fm) {
        this.fm = fm;
    }
}