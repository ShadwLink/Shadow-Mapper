package nl.shadowlink.tools.shadowmapper.render;

import nl.shadowlink.tools.io.Vector3D;

import javax.swing.JTextField;

/**
 * This class is used for the Camera movement
 *
 * @author Shadow-Link 
 * @version 1.0
 */
public class Camera {

    public Vector3D pos = new Vector3D();
    public Vector3D view = new Vector3D();
    public Vector3D up = new Vector3D();
    private float camYaw = 0.0f;    //Yaw of the Cam
    private float camPitch = 0.0f;  //Pitch of the Cam

    private JTextField textX = null;
    private JTextField textY = null;
    private JTextField textZ = null;

    private boolean hasText;

    //The next vars are used to store the current camera
    private Vector3D spos;
    private Vector3D sview;
    private Vector3D sup;
    private float scamYaw = 0.0f; //save
    private float scamPitch = 0.0f; //save
    private boolean saved = false;  //Check if the camera is saved

    /**
     * Constructor of the camera.
     * @param posX The X Position of the camera
     * @param posY The Y Position of the camera
     * @param posZ The Z Position of the camera
     * @param viewX The X View of the camera
     * @param viewY The Y View of the camera
     * @param viewZ The Z View of the camera
     * @param upX The X Up of the camera
     * @param upY The Y Up of the camera
     * @param upZ The Z Up of the camera
     */
    public Camera(float posX, float posY, float posZ, float viewX, float viewY, float viewZ, float upX, float upY, float upZ) {
        pos.x = posX;
        pos.y = posY;
        pos.z = posZ;
        view.x = viewX;
        view.y = viewY;
        view.z = viewZ;
        up.x = upX;
        up.y = upY;
        up.z = upZ;
        updateCameraText();
    }

    /**
     * Set the camera position to a certain position
     * Use this with pointCamera
     * @param posX The X Position of the camera
     * @param posY The Y Position of the camera
     * @param posZ The Z Position of the camera
     */
    public void setCameraPosition(float posX, float posY, float posZ) {
        pos.x = posX;
        pos.y = posY;
        pos.z = posZ;
        updateCameraText();
    }

    /**
     * Change all vars from the camera at once
     * @param posX The X Position of the camera
     * @param posY The Y Position of the camera
     * @param posZ The Z Position of the camera
     * @param viewX The X View of the camera
     * @param viewY The Y View of the camera
     * @param viewZ The Z View of the camera
     * @param upX The X Up of the camera
     * @param upY The Y Up of the camera
     * @param upZ The Z Up of the camera
     */
    public void setAllCamera(float posX, float posY, float posZ, float viewX, float viewY, float viewZ, float upX, float upY, float upZ) {
        pos.x = posX;
        pos.y = posY;
        pos.z = posZ;
        view.x = viewX;
        view.y = viewY;
        view.z = viewZ;
        up.x = upX;
        up.y = upY;
        up.z = upZ;
        updateCameraText();
    }

    /**
     * Point the camera to a certain psoition
     * @param viewX
     * @param viewY
     * @param viewZ
     */
    public void pointCamera(float viewX, float viewY, float viewZ) {
        view.x = viewX;
        view.y = viewY;
        view.z = viewZ;
    }

    /**
     * Move Camera in the direction you are viewing
     * @param speed The speed of the camera
     */
    public void moveCamera(float speed) {
        float x = view.x - pos.x;
        float y = view.y - pos.y;
        float z = view.z - pos.z;

        pos.x = pos.x + x * speed;
        pos.y = pos.y + y * speed;
        pos.z = pos.z + z * speed;
        view.x = view.x + x * speed;
        view.y = view.y + y * speed;
        view.z = view.z + z * speed;
        updateCameraText();
    }

    /**
     * Change the camera rotation
     * @param speed The speed of the camera
     */
    public void rotateView(float speed) {
        float x = view.x - pos.x;
        float z = view.z - pos.z;
        view.z = (float) (pos.z + Math.sin(speed) * x + Math.cos(speed) * z);
        view.x = (float) (pos.x + Math.cos(speed) * x - Math.sin(speed) * z);
    }

    /**
     * Strafe the camera
     * @param speed The speed of the camera
     */
    public void strafeCamera(float speed) {
        float x = view.x - pos.x;
        float z = view.z - pos.z;
        float oX;
        float oZ;

        oX = -z;
        oZ = x;

        pos.x = pos.x + oX * speed;
        pos.z = pos.z + oZ * speed;
        view.x = view.x + oX * speed;
        view.z = view.z + oZ * speed;
        updateCameraText();
    }

    private void updateCameraText(){
        if(hasText){
            textX.setText(String.format("%.2f", Float.valueOf(pos.x)));
            textZ.setText(String.format("%.2f", Float.valueOf(pos.y)));
            textY.setText(String.format("%.2f", Float.valueOf(pos.z)));
        }
    }

    public void giveText(JTextField textX, JTextField textY, JTextField textZ){
        this.textX = textX;
        this.textY = textY;
        this.textZ = textZ;
        hasText = true;
    }

    /**
     * Returns the current X position
     * @return The X Position of the camera
     */
    public float getPosX() {
        return pos.x;
    }

    /**
     * Returns the current Y position
     * @return The Y Position of the camera
     */
    public float getPosY() {
        return pos.y;
    }

    /**
     * Returns the current Z position
     * @return The Z position of the camera
     */
    public float getPosZ() {
        return pos.z;
    }

    /**
     * Returns the current X view
     * @return The X view
     */
    public float getViewX() {
        return view.x;
    }

    /**
     * Returns the current Y view
     * @return The Y view
     */
    public float getViewY() {
        return view.y;
    }

    /**
     * Returns the current Z view
     * @return The Z view
     */
    public float getViewZ() {
        return view.z;
    }

    /**
     * Returns the current X up
     * @return The X up
     */
    public float getUpX() {
        return up.x;
    }

    /**
     * Returns the current Y up
     * @return The Y up
     */
    public float getUpY() {
        return up.y;
    }

    /**
     * Returns the current Z up
     * @return The Z up
     */
    public float getUpZ() {
        return up.z;
    }

    /**
     * Set X view
     * @param x Set the X view
     */
    public void setViewX(float x) {
        this.view.x = x;
    }

    /**
     * Set Y view
     * @param y Set the Y view
     */
    public void setViewY(float y) {
        this.view.y = y;
    }

    /**
     * Set Z view
     * @param z Set the Z view
     */
    public void setViewZ(float z) {
        this.view.z = z;
    }

    /**
     * Reset the camera to the default position
     */
    public void reset() {
        setAllCamera(-20,45,-16,-15,45,-12, 0, 1, 0);
        camPitch = 0.0f;
        camYaw = 0.0f;
    }

    /**
     * Set the camera pitch
     * @param pitch Set the camera Pitch
     */
    public void setPitch(float pitch) {
        this.camPitch = pitch;
    }

    /**
     * Set the camera yaw
     * @param yaw Set the camera Yaw
     */
    public void setYaw(float yaw) {
        this.camYaw = yaw;
    }

    /**
     * Returns the camera pitch
     * @return The camera pitch
     */
    public float getPitch() {
        return camPitch;
    }

    /**
     * Returns the camera yaw
     * @return The camera yaw
     */
    public float getYaw() {
        return camYaw;
    }

    /**
     * Stores the current camera position
     */
    public void saveCamera() {
        if (!saved) {
            this.spos.x = pos.x;
            this.spos.y = pos.y;
            this.spos.z = pos.z;
            this.sview.x = view.x;
            this.sview.y = view.y;
            this.sview.z = view.z;
            this.sup.x = up.x;
            this.sup.y = up.y;
            this.sup.z = up.z;
            this.scamPitch = camPitch;
            this.scamYaw = camYaw;
            saved = true;
        }
    }

    /**
     * Loads the stored camera position
     */
    public void loadCamera() {
        if (saved) {
            this.pos.x = spos.x;
            this.pos.y = spos.y;
            this.pos.z = spos.z;
            this.view.x = sview.x;
            this.view.y = sview.y;
            this.view.z = sview.z;
            this.up.x = sup.x;
            this.up.y = sup.y;
            this.up.z = sup.z;
            this.camPitch = scamPitch;
            this.camYaw = scamYaw;
            saved = false;
        }
    }
}
