package mygame;

import java.util.ArrayList;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class Trajectory {
	
	 ArrayList<Vector3f> points;
	 int index; // ‘points’ listan indeksi
	 int size; // kuinka monta waypointtia ‘points’ listassa on

	 // alustaa yllämainitut points ja index muuttujat
	 public Trajectory() {
		 points = new ArrayList<Vector3f>();
		 index = -1;
		 size = 0;
	 }

	 // lisää pisteen listan hännille
	 public void addPoint(Vector3f v) {
		 points.add(v);
		 size += 1;
	 }
	 // nollaa indeksin ja asettaa size muuttujalle oikean arvon
	 public void initTrajectory() {
		 index = -1;
		 size = points.size();
	 }

	 // palauttaa indexin kohdalla olevan pisteen tai null jos ei enää pisteitä
	 public Vector3f nextPoint() {
		index += 1; 
		if (index >= points.size()) {
			return null;
		} else {
			return points.get(index);
		}
	 }

}
