/**
 * @author Benny
 */

package de.hsbremen.powerwall.collisionintersection;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.GImpactCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

public class CollisionIntersection extends SimpleApplication implements PhysicsCollisionListener {

	private BulletAppState		bulletAppState;
	private Geometry 			teapot;
	private Geometry 			sphere;
	private Node				teapotNode;
	private Node				sphereNode;
	
	private void loadObjects() {
		
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		bulletAppState.getPhysicsSpace().enableDebug(assetManager);
		
		//create a teapot
		//load a material for the teapot and assign it		
		//set the teapot in front of the camera
		//attach the teapot to the root node
		teapot = (Geometry) assetManager.loadModel("Models/Teapot/Teapot.obj");
		teapot.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md"));
		//teapot.setLocalTranslation(0f, 0f, 5f);
		teapot.setLocalTranslation(0f, 0f, 0f);
		rootNode.attachChild(teapot);


		//create a sphere and assign it to a geometry, etc...
		Sphere s = new Sphere(32, 32, 0.2f);
		sphere = new Geometry("Sphere", s);
		sphere.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md"));
		//sphere.setLocalTranslation(2f, 1f, 2f);
		sphere.setLocalTranslation(0f, 0.3f, 0.8f);
		rootNode.attachChild(sphere);


		//create a directional light
		//set the direction
		//attach it to the scene
		DirectionalLight light = new DirectionalLight();
		light.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
		rootNode.addLight(light);
		
		
		// create collision mesh
		Geometry teapotGeom = (Geometry) teapot;
		Geometry sphereGeom = (Geometry) sphere;
		
		//GImpactCollisionShape collisionShape = new GImpactCollisionShape(sphereGeom.getMesh());
		//GImpactCollisionShape collisionShape = new GImpactCollisionShape(sphereGeom.getMesh());
			
		RigidBodyControl rigidSphere = new RigidBodyControl(new GImpactCollisionShape(sphereGeom.getMesh()), 0f);
		sphere.addControl(rigidSphere);
		
		RigidBodyControl rigidTeapot = new RigidBodyControl(new GImpactCollisionShape(teapotGeom.getMesh()), 0f);
		teapot.addControl(rigidTeapot);
		
		teapotNode = new Node("teapotNode");
		sphereNode = new Node("sphereNode");
		teapotNode.attachChild(teapotGeom);
		sphereNode.attachChild(sphereGeom);
		
		rootNode.attachChild(teapotNode);
		rootNode.attachChild(sphereNode);
		
		bulletAppState.getPhysicsSpace().add(rigidTeapot);
		bulletAppState.getPhysicsSpace().add(rigidSphere);	
		bulletAppState.getPhysicsSpace().setAccuracy(0.005f);
	}

	 public void collision(PhysicsCollisionEvent event) {
		 /*
	        if ("box".equals(event.getNodeA().getName()) || "box".equals(event.getNodeB().getName())) {
	            if ("bullet".equals(event.getNodeA().getName()) || "bullet".equals(event.getNodeB().getName())) {
	                fpsText.setText("You hit the box!");
	            }
	        }
	        if ("mesh".equals(event.getNodeA().getName()) || "mesh".equals(event.getNodeB().getName())) {
	            if ("bullet".equals(event.getNodeA().getName()) || "bullet".equals(event.getNodeB().getName())) {
	                fpsText.setText("You hit the mesh!");
	            }
	        }
	        */
		 System.out.println("Collision!");
	    }

	    public boolean collide(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB) {
	        //group 2 only randomly collides
	        /*
	    	if (Math.random() < 0.5f) {
	            return true;
	        } else {
	            return false;
	        }
	        */
	    	System.out.println("Test collision!");
	    	return false;
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
		teapotNode.rotate(0f, 0.02f, 0f);
		
		/*
		teapotNode.updateModelBound();
		teapotNode.updateGeometricState();
		*/
		
		/*
		teapot.rotate(0f,  2*tpf, 0f);
		sphere.rotate(tpf, tpf, tpf);
		*/
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
