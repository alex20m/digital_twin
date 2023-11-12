package mygame;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;

public class RobotArm2 {
	
	Node node = new Node();
	Node tooltipNode = new Node();
	
	Geometry mastGeom;
	Geometry xArmGeom;
	Geometry yArmGeom;
	Geometry zArmGeom;
	Geometry tooltipGeom;
	
	Box mast;
	Box xArm;
	Box yArm;
	Box zArm;
	Box tooltip;
	
	private Vector3f targetLocation; // välietappi
	float step = 10f; // etäisyys akselia kohden mikä liikutaan yhden syklin aikana
	
	public RobotArm2(AssetManager assetManager, Node rootNode) {
		
		
		mast = new Box(0.2f, 6f, 0.2f); 
		xArm = new Box(18f, 0.2f, 0.2f);
		yArm = new Box(0.2f, 6f, 0.2f);
		zArm = new Box(0.2f, 0.2f, 20f);
		tooltip = new Box(0.14f, 0.4f, 0.14f);
		
		mastGeom = new Geometry("Box", mast);
		xArmGeom = new Geometry("Box", xArm);
		yArmGeom = new Geometry("Box", yArm);
		zArmGeom = new Geometry("Box", zArm);
		tooltipGeom = new Geometry("Box", tooltip);
		
		node.attachChild(mastGeom);
		node.attachChild(xArmGeom);
		node.attachChild(yArmGeom);
		node.attachChild(zArmGeom);
		node.attachChild(tooltipGeom);
		
		Material mat = new Material(assetManager,
		"Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors",true); 
		
		mat.setColor("Diffuse", ColorRGBA.Orange);
		
		mastGeom.setMaterial(mat);
		xArmGeom.setMaterial(mat);
		yArmGeom.setMaterial(mat);
		zArmGeom.setMaterial(mat);
		tooltipGeom.setMaterial(mat);
		
		node.attachChild(tooltipNode);
		
		tooltipNode.attachChild(tooltipGeom);
		
		mastGeom.setLocalTranslation(-8f, 0f, -10f);
		xArmGeom.setLocalTranslation(6f, 6f, 0f);
		yArmGeom.setLocalTranslation(-7f, 6f, 0f);
		zArmGeom.setLocalTranslation(-8f, 6f, -8f);
		
		tooltipNode.setLocalTranslation(-7f, -0.4f, 0f);
	}
	
	// target on välietappi johon kuuluu ajaa
	public void initMove(Vector3f target) {
		targetLocation = target;
	}
	
	// palauttaa tooltipin alapinnan keskipisteen koordinaatit maailma-koordinaateissa
	// käytä Geometry luokan getWorldTranslation()
	public Vector3f getToolTipLocation() {
		return tooltipGeom.getWorldTranslation();
	}
	
	// moves towards target location and returns false when it reached the location
	public boolean move() {
		
	 Vector3f location = getToolTipLocation();
	 
	 // lasketaan etäisyys määränpäähän maailma-koordinaateissa
	 float xDistance = targetLocation.getX() - location.getX();
	 float zDistance = targetLocation.getZ() - location.getZ();
	 float yDistance = targetLocation.getY() - location.getY();
	 
	 // booleanit ilmaisee että onko kyseisen akselin suuntainen liike valmis
	 boolean xReady = false;
	 boolean yReady = false;
	 boolean zReady = false;
	 
	 float x = 0f; // x-akselin suuntainen liike tämän syklin aikana
	 float y = 0f; // y-akselin suuntainen liike tämän syklin aikana
	 float z = 0f; // z-akselin suuntainen liike tämän syklin aikana
	 
	 // siirrytään stepin verran oikeaan suuntaan jos matkaa on yli stepin verran
	 // muuten siirrytään targetLocationin x koordinaattiit
	 
	 if (xDistance > step) {
		 x = step;
	 } else if ((-1 * xDistance) > step) {
		 x = -1 * step;
	 } else {
		 xReady = true;
		 x = xDistance;
	 }
	 
	 if (zDistance > step) {
		 z = step;
	 } else if ((-1 * zDistance) > step) {
		 z = -1 * step;
	 } else {
		 zReady = true;
		 z = zDistance;
	 }
	 
	 if (yDistance > step) {
		 y = step;
	 } else if ((-1 * yDistance) > step) {
		 y = -1 * step;
	 } else {
		 yReady = true;
		 y = yDistance;
	 }

	 
	 // siirretään mastossa kiinni oleva zArm, joka liikkuu siis z-suuntaan
	 // 0.5f siitä syystä että robotti ulottuu paremmin (xArm liikuu zArmia pitkin)
	 
	 Vector3f v = new Vector3f(0, 0, 0.5f*z);
	 zArmGeom.setLocalTranslation(zArmGeom.getLocalTranslation().add(v));
	 
	 // xArm on zArmin varassa minkä lisäksi se liikkuu sitä pitkin, joten nyt
	 // käytetä 0.5f kerrointa kuten äsken
	 
	 Vector3f v1 = new Vector3f(0, 0, z);
	 xArmGeom.setLocalTranslation(xArmGeom.getLocalTranslation().add(v1));
	 
	 // yArm liikkuu xArm Pitkin x suuntaan ja tekee myös y-suuntaisen liikkeen,
	 // minkä lisäksi zArmin liike siirtää myös yArmia
	 
	 Vector3f v2 = new Vector3f(x, y, z);
	 yArmGeom.setLocalTranslation(yArmGeom.getLocalTranslation().add(v2));
	 
	 // nodetoolTip paikaksi on määritelty yArm alapinta, mutta nodetoolTipin parent
	 // noodi ei liiku, joten nodetoolTip pitää siirtää kuten yArm
	 // samalla liikkuu nodetoolTippiin liitetty tooltipin geometria
	 
	 tooltipNode.setLocalTranslation(tooltipNode.getLocalTranslation().add(v2));
	 
	 if((yReady && xReady) && zReady) {
		 return false; //i.e. not moving anymore
	 } else {
		 return true;
	 }
	 
	} 
	
}