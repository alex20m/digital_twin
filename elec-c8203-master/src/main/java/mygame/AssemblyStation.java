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

public class AssemblyStation {
	
	Node node = new Node();
	Geometry geom;
	Box box;
	float maxHeight = 4; // max korkeus reitin välietapeille
	boolean moving = false; // true jos matkalla seuraavaan välietappiin
	Trajectory trajectory;
	RobotArm assemblyArm;
	RobotArm assemblyArm2;
	float x;
	float z;
	float surfaceHeight;
	
	public AssemblyStation(AssetManager assetManager, Node rootNode, float xOffset, float zOffset) {
		
		x = xOffset;
		z = zOffset;
		
		float yExtent = 6f;
		surfaceHeight = Main.floorHeight + yExtent;
		
		assemblyArm = new RobotArm(assetManager, rootNode, "orange");
		//assemblyArm2 = new RobotArm(assetManager, rootNode, "pink");
		node.attachChild(assemblyArm.node);
		//node.attachChild(assemblyArm2.node);
		
		box = new Box(20f, yExtent, 10f); 
		geom = new Geometry("Box", box);
		node.attachChild(geom);
		
		Material mat = new Material(assetManager,
		"Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors",true); 
		
		mat.setColor("Diffuse", ColorRGBA.LightGray);
		geom.setMaterial(mat);
		
		geom.setLocalTranslation(xOffset, Main.floorHeight + yExtent, zOffset);
	}
	
	// tehdään APP eli reitinsuunnittelu destination koordinaatteihin
	public void initTestMove(Vector3f destination) {
		
		trajectory = new Trajectory();
		
		// eka välietappi suoraan ylös max korkeuteen
		Vector3f v1 =  assemblyArm.getToolTipLocation();
		v1.setY(maxHeight);
		trajectory.addPoint(v1);
		
		// toka välietappi max korkeuteen destination ylle
		Vector3f v2 =  destination.clone();
		v2.setY(maxHeight);
		trajectory.addPoint(v2);
		
		trajectory.addPoint(destination);
		trajectory.initTrajectory();
	}
	
	// käskyttää robottia ajamaan reitin, joka on määritelty trajectory-attribuuttiin
	// palauttaa false jos saavutettiin trajectory viimeinen (väli)etappi, eli
	// initTestMove() saama destination. Muuten palauttaa true.
	// tätä tulee kutsua syklisesti kunnes se palauttaa false
	
	public boolean move() {
		
		 if (moving) {
			 moving = assemblyArm.move();
			 return true;
		 } else {
			 // tänne tullaan jos edellinen välietappi saavutettiin
			 Vector3f nextPoint = trajectory.nextPoint();
			 if (nextPoint == null) {
			 return false;
			 } else {
				 // debug printit tulee konsoliin näkyviin kun suljet ohjelman
				 System.out.println(nextPoint.toString());
				 // annetaan robotille seuraava välietappi ja alustetaan moving seuraavaa
				 // move() kutsua silmälläpitäen
				 assemblyArm.initMove(nextPoint);
				 moving = assemblyArm.move();
				 return true;
			 }
		 }
	}
	
	float legoSpacingX = 2; // legojen slottipaikkojen etäisyys x-suuntaan
	float legoSpacingZ = 2; // legojen slottipaikkojen etäisyys z-suuntaan
	
	// kokoonpanoasemalla on slotteja, joiden indeksi on kokonaisluku
	// tämä palauttaa slotin 3D koordinaatit
	
	public Vector3f slotPosition(int slot) {
	 // vain osa asemasta on varattu tähän tarkoitukseen. Sen koko on 16x12
		
	 int rowSize = (int)((16)/legoSpacingX);
	 int columnSize = (int)((12)/legoSpacingZ);
	 int rowIndex = slot % rowSize;
	 float xOffset = (rowIndex-1) * legoSpacingX;
	 int columnIndex = slot / rowSize;
	 float zOffset = (columnIndex + 2) * legoSpacingZ;
	 float yOffset = 0.4f; // legonyExtent
	 
	 // ’x’ ja ’z’ on float muuttujia, joihin on tallennettu konstruktorin xOffset/zOffset
	 // laske ’surfaceHeight’ konstruktorissa
	 return new Vector3f(x + xOffset, surfaceHeight + yOffset + 6.2f, z + zOffset - 12);
	}
	
	// APP kohteeseen lego.location
	// sama idea kuin edellisen harjoituksen initTestMove()
	
	public void initMoveToLego(Lego lego) {
		trajectory = new Trajectory();
		
		// eka välietappi suoraan ylös max korkeuteen
		Vector3f v1 =  assemblyArm.getToolTipLocation();
		v1.setY(maxHeight);
		trajectory.addPoint(v1);
		
		// toka välietappi max korkeuteen destination ylle
		Vector3f v2 =  lego.location.clone();
		v2.setY(maxHeight);
		trajectory.addPoint(v2);
		
		trajectory.addPoint(lego.location);
		trajectory.initTrajectory();
	}
	
	// APP kohteeseen destination
	
	public void initMoveToStation(Lego lego, Vector3f destination, float counter) {
		
	 assemblyArm.tooltipNode.attachChild(lego.node); // muuten lego ei lähde mukaan
	 
	 // nyt legon noden sijainti pitää määritellä nodeToolTip paikallisissa
	 // koordinaateissa. lego.node.setLocalTranslation(0,0,0) laittaisi legon
	 // keskipisteen tooltipin keskipisteeseen
	 // vinkki: tooltipin yExtent = 0.4f ja legon yExtent = 0.2f
	 lego.node.setLocalTranslation(0f, -0.4f, 0f);
	 
	 // sitten tehdään APP kohteeseen ”destination”
	 initTestMove(destination);
	}
	
	
	
	
	
	
	
	
	
	
	/*public boolean move2() {
		
		 if (moving) {
			 moving = assemblyArm2.move();
			 return true;
		 } else {
			 // tänne tullaan jos edellinen välietappi saavutettiin
			 Vector3f nextPoint = trajectory.nextPoint();
			 if (nextPoint == null) {
			 return false;
			 } else {
				 // debug printit tulee konsoliin näkyviin kun suljet ohjelman
				 System.out.println(nextPoint.toString());
				 // annetaan robotille seuraava välietappi ja alustetaan moving seuraavaa
				 // move() kutsua silmälläpitäen
				 assemblyArm2.initMove(nextPoint);
				 moving = assemblyArm2.move();
				 return true;
			 }
		 }
	}
	
	public void initMoveToLego2(Lego lego) {
		trajectory = new Trajectory();
		
		// eka välietappi suoraan ylös max korkeuteen
		Vector3f v1 =  assemblyArm2.getToolTipLocation();
		v1.setY(maxHeight);
		trajectory.addPoint(v1);
		
		// toka välietappi max korkeuteen destination ylle
		Vector3f v2 =  lego.location.clone();
		v2.setY(maxHeight);
		trajectory.addPoint(v2);
		
		trajectory.addPoint(lego.location);
		trajectory.initTrajectory();
	}
	
	// APP kohteeseen destination
	
	public void initMoveToBuffer2(Lego lego, Vector3f destination, int slotposition2) {
		
	 assemblyArm2.tooltipNode.attachChild(lego.node); // muuten lego ei lähde mukaan
	 
	 // nyt legon noden sijainti pitää määritellä nodeToolTip paikallisissa
	 // koordinaateissa. lego.node.setLocalTranslation(0,0,0) laittaisi legon
	 // keskipisteen tooltipin keskipisteeseen
	 // vinkki: tooltipin yExtent = 0.4f ja legon yExtent = 0.2f
	 
	 	Vector3f v1 =  assemblyArm2.getToolTipLocation();
	 	v1.setY(maxHeight);
		trajectory.addPoint(v1);
		
		// toka välietappi max korkeuteen destination ylle
		Vector3f v2 =  slotPosition(slotposition2);
		v2.setY(maxHeight);
		trajectory.addPoint(v2);
		
		trajectory.initTrajectory();
	 
	 //lego.node.setLocalTranslation(0f, -0.4f, 0f);
	 
	 // sitten tehdään APP kohteeseen ”destination”
	 initTestMove(destination);
	}
	*/
}


