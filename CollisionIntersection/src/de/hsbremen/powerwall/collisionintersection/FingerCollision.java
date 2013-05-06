import javax.vecmath.Matrix3d;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;



public class FingerCollision extends SimpleApplication implements PhysicsCollisionListener {

  private BulletAppState bulletAppState;
	private PhysicsSpace physicsSpace;
	private Vector3f fingerVec;
	private Geometry fingerGeometry;
	private Camera camera;
	private Matrix4f matrix;
	
	
	@Override
	public void collision(PhysicsCollisionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void simpleInitApp() {
		//init physics
		physicsSpace = new PhysicsSpace();
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		camera = getCamera();
		camera.setLocation(new Vector3f(0,0,3));
		fingerVec = new Vector3f(0,0,3);
		matrix = new Matrix4f();
		
		//light
		AmbientLight light = new AmbientLight();
		light.setColor(ColorRGBA.LightGray);
		rootNode.addLight(light);
		
		//material
		Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.DarkGray);
		
        Material boxMaterial = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        
		//floor
        Box floorBox = new Box(140, 0.25f, 140);
        Geometry floorGeometry = new Geometry("Boden", floorBox);
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, -5, 0);
        floorGeometry.addControl(new RigidBodyControl(0));
		rootNode.attachChild(floorGeometry);
        bulletAppState.getPhysicsSpace().add(floorGeometry);
        
        Box box = new Box(0.25f, 0.25f, 0.25f);
        Geometry boxGeometry = new Geometry("Box", box);
        boxGeometry.setMaterial(boxMaterial);
        boxGeometry.setLocalTranslation(0, 0, 0);
        RigidBodyControl boxControl = new RigidBodyControl(100);
        boxGeometry.addControl(boxControl);
        rootNode.attachChild(boxGeometry);
        bulletAppState.getPhysicsSpace().add(boxGeometry);
        
        
        Box finger = new Box(0.2f, 0.2f, 0.2f);
        fingerGeometry = new Geometry("Box", finger);
        fingerGeometry.setMaterial(boxMaterial);
        fingerGeometry.setLocalTranslation(fingerVec);
        RigidBodyControl fingerControl = new RigidBodyControl(0);
        fingerGeometry.addControl(fingerControl);
        rootNode.attachChild(fingerGeometry);
        bulletAppState.getPhysicsSpace().add(fingerGeometry);
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		//location von der Kamera
		//dann direction von der Kamera
		//dann rotation von der Kamera
		fingerGeometry.setLocalTranslation(camera.getLocation());
		fingerGeometry.rotateUpTo(camera.getDirection());
//		fingerGeometry.setLocalTranslation(camera.getDirection().x, camera.getDirection().y, camera.getDirection().z);
//		fingerGeometry.move();
		
		
	}

	public static void main(String[] args) {
		AppSettings settings = new AppSettings(true);
		settings.setResolution(800, 600);
		settings.setBitsPerPixel(32);
		settings.setFullscreen(false);

		FingerCollision app = new FingerCollision();
		app.setSettings(settings);
		app.setShowSettings(false);
		app.start();
	}

}
