package nl.shadowlink.tools.shadowmapper.gui

import nl.shadowlink.tools.io.Vector3D
import nl.shadowlink.tools.shadowlib.ipl.Item_CARS
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowmapper.FileManager
import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.centerWindow
import java.awt.event.ActionEvent
import javax.swing.*

/**
 * @author Shadow-Link
 */
class CarForm(
    private val fm: FileManager,
    private val ipl: Int,
    private val position: Vector3D
) : JFrame() {

    private var carsComboBox: JComboBox<String> = JComboBox()
    private var addButton: JButton = JButton()

    /**
     * Creates new form CarForm
     */
    init {
        initComponents()
        this.isVisible = true
        this.centerWindow()
    }

    private fun initComponents() {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        title = "Add Car"
        carsComboBox.model = fm.modelVehicles
        addButton.text = "Add"
        addButton.addActionListener { evt: ActionEvent -> onAddButtonClicked(evt) }
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(carsComboBox, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addButton)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(
                                    carsComboBox,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(addButton)
                        )
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
                )
        )
        pack()
    }

    private fun onAddButtonClicked(evt: ActionEvent) {
        val iplItem = Item_CARS(GameType.GTA_IV).apply {
            name = carsComboBox.selectedItem.toString()
            position.x = position.x
            position.y = -position.z
            position.z = position.y
            rotation = Vector3D(0f, 0f, 0f)
            unknown1 = 0
            unknown2 = 0
            unknown3 = 0
            unknown4 = 0
            unknown5 = 0
            unknown6 = 0
            unknown7 = 0
        }

        fm.ipls[ipl].items_cars.add(iplItem)
        fm.ipls[ipl].changed = true

        fm.modelIPLItems.addElement(iplItem.name)
        dispose()
    }
}