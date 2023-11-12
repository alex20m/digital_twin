package XML;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Skeleton {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
	        File inputFile = new File("testing_station.aml");
	        //File inputFile = new File("proteusexample.xml");
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(inputFile);
	        doc.getDocumentElement().normalize();
	        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	        
	        NodeList nList = doc.getElementsByTagName("InternalElement");
	        System.out.println(nList.getLength());

	        for (int temp = 0; temp < nList.getLength(); temp++) {
	        	Node nNode = nList.item(temp);
	        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	        		Element eElement = (Element) nNode;
	        		String componentName = eElement.getAttribute("SystemUnitClassLib/");

	        		//System.out.println(componentName);

	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
