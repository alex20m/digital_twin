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

public class Lego {
	
 Node node = new Node();
 Geometry geom;
 Box box;
 String color;
 Vector3f location;

 
 public Lego(AssetManager assetManager, String inputColor) {
	 
	 box = new Box(0.8f, 0.2f, 0.4f); 
	 geom = new Geometry("Box", box);
	 node.attachChild(geom);
	 color = inputColor;
	 
	 Material mat = new Material(assetManager,
	"Common/MatDefs/Light/Lighting.j3md");
	 mat.setBoolean("UseMaterialColors",true); 
	 
	 float distance = -0.6f;
	 
	 for (int i = 1; i < 9; i++) {
		 Cylinder cyl = new Cylinder(20, 20, 0.1f, 0.1f, true);
		 Geometry g = new Geometry("C", cyl);
		 g.rotate(FastMath.HALF_PI, 0, 0);
		 if (i % 2 == 0) {
			 g.setLocalTranslation(distance, 0.2f, 0.2f);
			 distance += 0.4f;
		 } else {
			 g.setLocalTranslation(distance, 0.2f, -0.2f);
		 }
		 g.setMaterial(mat);
		 node.attachChild(g); 
		 
	 }
	 
	 ColorRGBA c;
	 
	 if (color == "green") {
		 c = ColorRGBA.Green;
	 } else if (color == "red") {
		 c = ColorRGBA.Red;
	 } else if (color == "yellow") {
		 c = ColorRGBA.Yellow;
	 } else if (color == "pink") {
		 c = ColorRGBA.Pink;
	 } else if (color == "blue") {
		 c = ColorRGBA.Blue;
	 } else {
		 c = ColorRGBA.DarkGray;
	 }
	 
	 mat.setColor("Diffuse",c);
	 geom.setMaterial(mat);
	 
	 }
} 