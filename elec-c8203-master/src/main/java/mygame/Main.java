package mygame;
import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.light.PointLight;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
	
 public static void main(String[] args) {
 Main app = new Main();
 app.start();
 }
 
 public static float floorHeight = -15;
 AssemblyStation station;
 LegoBuffer legoBuffer;
 LegoBuffer legoBuffer2;
 //RobotArm robot;
 boolean freeze = false; // debug tarkoituksiin – laita true siinä kohdassa koodia
 
 boolean stack = true;
 int counter = 0;
 int done = 0;
 boolean next_robot = false;
 int slotIndex2 = 0;
 int counter2 = 0;
 
 // mihin haluat robotin pysähtyvän
 boolean moving = false; // true kun robotti liikkuu
 boolean goingToLego = false; // true kun mennään hakemaan legoa bufferista
 Lego lego;
 int slotIndex = 0; // kokoonpanoaseman slot
 final int numColors = 4; // final on sama kuin C-kielen const
 int colorIndex = 0; // “colors” (kts alla) listan indeksi
 
 // listan perusteella tiedetään missä järjestyksessä lajitellaan värin mukaan
 ArrayList<String> colors = new ArrayList(numColors);
 
 @Override
 public void simpleInitApp() {
	 flyCam.setMoveSpeed(10);
	 
	 colors.add("yellow");
	 colors.add("blue");
	 colors.add("pink");
	 colors.add("green");
	 
	 /*Lego legoGreen = new Lego(assetManager, "green");
	 Lego legoYellow = new Lego(assetManager, "yellow");
	 Lego legoPink = new Lego(assetManager, "pink");
	 Lego legoBlue = new Lego(assetManager, "blue");
	 Lego legoRed = new Lego(assetManager, "red");
	 
	 rootNode.attachChild(legoGreen.node);
	 rootNode.attachChild(legoYellow.node);
	 rootNode.attachChild(legoPink.node);
	 rootNode.attachChild(legoBlue.node);
	 rootNode.attachChild(legoRed.node);
	 
	 legoYellow.node.setLocalTranslation(2f, 0, 0); 
	 legoPink.node.setLocalTranslation(4f, 0, 0);
	 legoBlue.node.setLocalTranslation(6f, 0, 0);
	 legoRed.node.setLocalTranslation(8f, 0, 0);*/
	 
	 station = new AssemblyStation(assetManager, rootNode, 5f, -11f);
	 //robot = new RobotArm(assetManager, rootNode);
	 
	 rootNode.attachChild(station.node);
	 //rootNode.attachChild(robot.node);
	 
	 legoBuffer = new LegoBuffer(assetManager, rootNode, 5, -29, 10, 6, true);
	 //legoBuffer2 = new LegoBuffer(assetManager, rootNode, 5, 7, 10, 6, false);

	 
	 PointLight lamp_light = new PointLight();
	 lamp_light.setColor(ColorRGBA.White);
	 lamp_light.setRadius(10000f);
	 lamp_light.setPosition(new Vector3f(2f, 8f, 10f));
	 rootNode.addLight(lamp_light);
	 
	 //station.initTestMove(new Vector3f(0,0,-5));
} 
 

/*public void simpleUpdate(float tpf) {
	station.move();
}*/
 

 public void simpleRender(RenderManager rm) {
 //TODO: add render code
 }
 


public void simpleUpdate(float tpf) {
 if(!freeze && moving) {
	 moving = station.move();
 }
 
 if(!moving && !freeze && next_robot == false) {
	 
	 // moving=false tarkoittaa että saavuttiin reitin päähän, joten on 2 tapausta:
	 // otetaan lego mukaan tai jätetään se
	 
	 if(goingToLego) { // otetaan lego mukaan
		 
		 if (stack) {
			 
			 if (counter >= 15) {
				 counter = 0;
				 slotIndex++;
				 done ++;
			 }
			 
			 
			 Vector3f v = station.slotPosition(slotIndex);
			 v.setY(-2.4f + counter * 0.4f);

			 

			 station.initMoveToStation(lego, v, counter);
			 
			 counter++;
			 
			 goingToLego = false;
			 moving = true;
			 
			 if (done == 3 && counter == 15) {
				 //next_robot = true;
				 //goingToLego = false;
				 //colorIndex = 0;
			 }
			 
		 } else {
			 // nyt ollaan bufferilla sen legon kohdalla mikä otetaan mukaan
			 // v:hen laitetaan kokoonpanoaseman slot numero ”slotIndex” koordinaatit
			 
			 Vector3f v = station.slotPosition(slotIndex);
			 
			 // suoritetaan APP kohteeseen v
			 
			 station.initMoveToStation(lego, v, 0);
			 slotIndex++;
			
			 
			 goingToLego = false;
			 moving = true;
		 }
		 
	 } else { // jätetään lego tähän
		 if(lego != null) { // käynnistyksen yhteydessä tätä koodia ei suoriteta
			 
		 // lego on nyt toimitettu oikeaan paikkaan kokoonpanoasemalle
		 // otetaan paikka talteen ennen kuin irrotetaan noodi
			 
		 Vector3f loc = lego.node.getWorldTranslation();
		 loc.setY(-3.2f + counter * 0.4f);
		 
		 // irrota legon node tooltipin nodesta
		 // (tämä on pitkä rimpsu jossa käytetään monen olion nimeä
		 
		 station.assemblyArm.tooltipNode.detachChild(lego.node);
		 
		 lego.node.setLocalTranslation(loc);
		 
		 // legon node ei ole nyt kiinni missään nodessa, joten se ei tule
		 // näkyviin ennen kuin korjaat asian
		 
		 rootNode.attachChild(lego.node);
		 
		 }
		 // haetaan bufferista seuraava lego, jonka väri on: colors.get(colorIndex)
		 // eli päivitä muuttujan ’lego’ arvo
		 
		 
		lego = legoBuffer.giveLego(colors.get(colorIndex));
				 
		 moving = true;
		 if (lego == null) {
		 // bufferissa ei ole enempää tämänvärisiä legoja
		 colorIndex++;
		 if(colorIndex >= numColors) {
		 // kaikki legot on siirretty
			freeze = true; // tämän jälkeen mitään ei tapahdu
		 } else {
			 // haetaan bufferista seuraava lego, jonka väri on:
			 // colors.get(colorIndex)
			 lego = legoBuffer.giveLego(colors.get(colorIndex));
		 }
		 }
		 if(!freeze) {
			 station.initMoveToLego(lego);
		 }
		 goingToLego = true;
	 }
 	}
 
 
 
 /*else if (next_robot == true) {
	 
	 // moving=false tarkoittaa että saavuttiin reitin päähän, joten on 2 tapausta:
	 // otetaan lego mukaan tai jätetään se
	 
	 if(goingToLego) { // otetaan lego mukaan
		 
		 if (stack) {
			 
			 if (counter2 >= 15) {
				 counter2 = 0;
				 slotIndex2++;
			 }
			 
			 
			 Vector3f v = lego.node.getWorldTranslation();
			 
			 station.initMoveToBuffer2(lego, v, counter);
			 
			 
			 counter2++;
			 
			 goingToLego = false;
			 moving = true;
			 
			 
		 } else {
			 // nyt ollaan bufferilla sen legon kohdalla mikä otetaan mukaan
			 // v:hen laitetaan kokoonpanoaseman slot numero ”slotIndex” koordinaatit
			 
			 Vector3f v = station.slotPosition(slotIndex);
			 
			 // suoritetaan APP kohteeseen v
			 
			 station.initMoveToBuffer2(lego, v, 0);
			 slotIndex++;
			
			 
			 goingToLego = false;
			 moving = true;
		 }
		 
	 } else { // jätetään lego tähän
		 
		 if(lego != null) { // käynnistyksen yhteydessä tätä koodia ei suoriteta
			 
		 // lego on nyt toimitettu oikeaan paikkaan kokoonpanoasemalle
		 // otetaan paikka talteen ennen kuin irrotetaan noodi
			 
		 Vector3f loc = lego.node.getWorldTranslation();
		 loc.setY(-3.2f + counter * 0.4f);
		 
		 // irrota legon node tooltipin nodesta
		 // (tämä on pitkä rimpsu jossa käytetään monen olion nimeä
		 
		 station.assemblyArm2.tooltipNode.detachChild(lego.node);
		 
		 lego.node.setLocalTranslation(loc);
		 
		 // legon node ei ole nyt kiinni missään nodessa, joten se ei tule
		 // näkyviin ennen kuin korjaat asian
		 
		 rootNode.attachChild(lego.node);
		 
		 }
		 // haetaan bufferista seuraava lego, jonka väri on: colors.get(colorIndex)
		 // eli päivitä muuttujan ’lego’ arvo
		 
		 
		lego = legoBuffer.getLego(colors.get(colorIndex));
				 
		 moving = true;
		 if (lego == null) {
		 // bufferissa ei ole enempää tämänvärisiä legoja
		 colorIndex++;
		 if(colorIndex >= numColors) {
		 // kaikki legot on siirretty
			freeze = true; // tämän jälkeen mitään ei tapahdu
		 } else {
			 // haetaan bufferista seuraava lego, jonka väri on:
			 // colors.get(colorIndex)
			 lego = legoBuffer.getLego(colors.get(colorIndex));
		 }
		 }
		 if(!freeze) {
			 station.initMoveToLego2(lego);
		 }
		 goingToLego = true;
	 }
 	}*/
 
}
 
} 