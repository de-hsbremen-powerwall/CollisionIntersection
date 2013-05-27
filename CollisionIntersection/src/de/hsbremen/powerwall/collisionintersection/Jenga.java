import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;


public class Jenga extends SimpleApplication implements PhysicsCollisionListener {

  public static final float BLOCK_HEIGHT = 0.1f; 
	public static final float BLOCK_WIDTH = 0.25f;
	public static final float BLOCK_LENGTH = 0.75f;

	private float mHeight = 0.0f;
	private BulletAppState physicsSpace;
	private PhysicsSpace space;

	private void increaseHeight() {
		mHeight += BLOCK_HEIGHT + 0.001f;
	}

	private Geometry createBlock(Vector3f location, int angle) {
		Material boxMaterial = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		Box box = new Box(BLOCK_WIDTH, BLOCK_HEIGHT, BLOCK_LENGTH);
		Geometry boxGeometry = new Geometry("Box", box);
		boxGeometry.setMaterial(boxMaterial);
		
		RigidBodyControl bulletControl = new RigidBodyControl(0f);

		Quaternion rotation = new Quaternion();
		rotation.fromAngles(0f, angle * FastMath.DEG_TO_RAD, 0f);
		
		boxGeometry.addControl(bulletControl);
		bulletControl.setMass(1f);
		rootNode.attachChild(boxGeometry);
		getPhysicsSpace().add(bulletControl);

		bulletControl.setPhysicsLocation(location);
		bulletControl.setPhysicsRotation(rotation);
		
		return boxGeometry;
	}

	private void createRow(boolean odd) {
		if(odd) {
			createBlock(new Vector3f(-2.1f * BLOCK_WIDTH, mHeight, 0), 0);
			createBlock(new Vector3f(0, mHeight, 0), 0);
			createBlock(new Vector3f(+2.1f * BLOCK_WIDTH, mHeight, 0), 0);
		} else {
			createBlock(new Vector3f(0, mHeight, -2.1f * BLOCK_WIDTH), 90);
			createBlock(new Vector3f(0, mHeight,0), 90);
			createBlock(new Vector3f(0, mHeight, +2.1f * BLOCK_WIDTH), 90);
		}
		increaseHeight();
	}

	private void buildTower() {
		//20 Reihen x 3 Blöcke
		for(int i = 0; i<=1000; i++) {
			if(i%2==0) {
				//gerade
				createRow(false);
			} else {
				//ungerade
				createRow(true);
			}
			increaseHeight();
		}
	}

	public float getHeight() {
		return mHeight;
	}

	public void setHeight(float height) {
		this.mHeight = height;
	}

	@Override
	public void collision(PhysicsCollisionEvent e) {

	}

	public void simpleInitApp() {
		space = new PhysicsSpace();
		physicsSpace = new BulletAppState();
		stateManager.attach(physicsSpace);

		getPhysicsSpace().enableDebug(assetManager);
		
		getCamera().setLocation(new Vector3f(0, 2, 10));
		
		AmbientLight light = new AmbientLight();
		light.setColor(ColorRGBA.LightGray);
		rootNode.addLight(light);

		Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.DarkGray);

		Box floorBox = new Box(140, 0.25f, 140);
		Geometry floorGeometry = new Geometry("Boden", floorBox);
		floorGeometry.setMaterial(material);
		floorGeometry.setLocalTranslation(0, -0.001f, 0);
		floorGeometry.addControl(new RigidBodyControl(0));

		rootNode.attachChild(floorGeometry);
		physicsSpace.getPhysicsSpace().add(floorGeometry);

		getPhysicsSpace().addCollisionListener(this);

		buildTower();
	}

	private PhysicsSpace getPhysicsSpace(){
		if(physicsSpace == null) {
			physicsSpace = new BulletAppState();
		}
		return physicsSpace.getPhysicsSpace();
	}

	public static void main(String[] args) {
		AppSettings settings = new AppSettings(true);
		settings.setResolution(800, 600);
		settings.setBitsPerPixel(32);
		settings.setFullscreen(false);

		Jenga app = new Jenga();
		app.setSettings(settings);
		app.setShowSettings(false);
		app.start();
	}


}
