/**
 * @author Benny
 */

package de.hsbremen.powerwall.collisionintersection;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

public class CollisionIntersection extends SimpleApplication {

	private Geometry teapot;
	private Geometry sphere;

	private void loadObjects() {
		//create a teapot
		//load a material for the teapot and assign it		
		//set the teapot in front of the camera
		//attach the teapot to the root node
		teapot = (Geometry) assetManager.loadModel("Models/Teapot/Teapot.obj");
		teapot.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md"));
		teapot.setLocalTranslation(0f, 0f, 5f);
		rootNode.attachChild(teapot);


		//create a sphere and assign it to a geometry, etc...
		Sphere s = new Sphere(32, 32, 0.2f);
		sphere = new Geometry("Sphere", s);
		sphere.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md"));
		sphere.setLocalTranslation(2f, 1f, 2f);
		rootNode.attachChild(sphere);


		//create a directional light
		//set the direction
		//attach it to the scene
		DirectionalLight light = new DirectionalLight();
		light.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
		rootNode.addLight(light);
	}

	private void initCrossHair() {
		//same as in jme docu
		//http://jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_picking
		guiNode.detachAllChildren();
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+");
		ch.setLocalTranslation(
				settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
				settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
		guiNode.attachChild(ch);
	}


	private void initGui() {
		initCrossHair();
	}
	
	@Override
	public void simpleInitApp() {
		loadObjects();
		initGui();
	}

	@Override
	public void simpleUpdate(float tpf) {
		//rotate the teapot
		teapot.rotate(0f,  2*tpf, 0f);
		sphere.rotate(tpf, tpf, tpf);
	}

	public static void main(String[] args) {
		//create the app settings, hide the settings dialog and start the application
		AppSettings settings = new AppSettings(true);
		settings.setResolution(800, 600);
		settings.setBitsPerPixel(32);
		settings.setFullscreen(false);

		CollisionIntersection app = new CollisionIntersection();
		app.setSettings(settings);
		app.setShowSettings(false);
		app.start();
	}

}
