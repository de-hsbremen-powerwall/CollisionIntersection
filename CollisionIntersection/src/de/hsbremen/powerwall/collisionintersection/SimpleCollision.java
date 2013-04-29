package de.hsbremen.powerwall.simplecollision;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.system.AppSettings;


public class SimpleCollision extends SimpleApplication implements PhysicsCollisionListener {

  private BulletAppState physicsSpace;
	private PhysicsSpace space;
	private int sphereCount;
	private int boxCount;

	@Override
	public void collision(PhysicsCollisionEvent event) {
		if(!event.getNodeA().getName().equals("Boden")) {
			System.out.println("Kollision zwischen " + event.getNodeA().getName() + " und " + event.getNodeB().getName());
		}
	}

	@Override
	public void simpleInitApp() {
		space = new PhysicsSpace();
		
		physicsSpace = new BulletAppState();
		stateManager.attach(physicsSpace);
//		physicsSpace.getPhysicsSpace().enableDebug(assetManager);

		AmbientLight light = new AmbientLight();
		light.setColor(ColorRGBA.LightGray);
		rootNode.addLight(light);

		Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.DarkGray);
		
        //floor
        Box floorBox = new Box(140, 0.25f, 140);
        Geometry floorGeometry = new Geometry("Boden", floorBox);
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, -5, 0);
        floorGeometry.addControl(new RigidBodyControl(0));

        rootNode.attachChild(floorGeometry);
        physicsSpace.getPhysicsSpace().add(floorGeometry);
        
        
		// add ourselves as collision listener
		getPhysicsSpace().addCollisionListener(this);
        createBallShooter(this, rootNode, physicsSpace.getPhysicsSpace());

	}

	 public void createBallShooter(final Application app, final Node rootNode, final PhysicsSpace space) {
	        ActionListener actionListener = new ActionListener() {

	            public void onAction(String name, boolean keyPressed, float tpf) {
	                Sphere sphere = new Sphere(32, 32, 0.4f, true, false);
	                sphere.setTextureMode(TextureMode.Projected);
	                Material mat2 = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
	                if (name.equals("shootSphere") && !keyPressed) {
	                    Geometry sphereGeometry = new Geometry("Kugel " + sphereCount, sphere);
	                    sphereGeometry.setMaterial(mat2);
	                    sphereGeometry.setLocalTranslation(app.getCamera().getLocation());
	                    RigidBodyControl bulletControl = new RigidBodyControl(10);
	                    sphereGeometry.addControl(bulletControl);
	                    bulletControl.setLinearVelocity(app.getCamera().getDirection().mult(25));
	                    sphereGeometry.addControl(bulletControl);
	                    rootNode.attachChild(sphereGeometry);
	                    space.add(bulletControl);
	                    sphereCount++;
	                } else if (name.equals("shootBox") && !keyPressed) {
	                    Box box = new Box(0.25f, 0.25f, 0.25f);
	                    Geometry boxGeometry = new Geometry("Box " + boxCount, box);
	                    boxGeometry.setMaterial(mat2);
	                    boxGeometry.setLocalTranslation(app.getCamera().getLocation());
	                    RigidBodyControl bulletControl = new RigidBodyControl(100);
	                    boxGeometry.addControl(bulletControl);
	                    bulletControl.setLinearVelocity(app.getCamera().getDirection().mult(10));
	                    rootNode.attachChild(boxGeometry);
	                    space.add(bulletControl);
	                    boxCount++;
	                }
	            }
	        };
	        app.getInputManager().addMapping("shootSphere", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
	        app.getInputManager().addListener(actionListener, "shootSphere");
	        
	        app.getInputManager().addMapping("shootBox", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
	        app.getInputManager().addListener(actionListener, "shootBox");
	 }
	
	private PhysicsSpace getPhysicsSpace(){
		return physicsSpace.getPhysicsSpace();
	}

	public static void main(String[] args) {
		AppSettings settings = new AppSettings(true);
		settings.setResolution(800, 600);
		settings.setBitsPerPixel(32);
		settings.setFullscreen(false);

		SimpleCollision app = new SimpleCollision();
		app.setSettings(settings);
		app.setShowSettings(false);
		app.start();
	}

}
