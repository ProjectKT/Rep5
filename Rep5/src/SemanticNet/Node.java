package SemanticNet;

import java.util.*;

public class Node {
	String name;

	// 自分から出ていくリンク
	ArrayList<Link> departFromMeLinks;
	// 自分に入ってくるリンク
	ArrayList<Link> arriveAtMeLinks;

	Node(String theName) {
		name = theName;
		departFromMeLinks = new ArrayList<Link>();
		arriveAtMeLinks = new ArrayList<Link>();
	}

	public ArrayList<Node> getISATails() {
		ArrayList<Node> isaTails = new ArrayList<Node>();
		for (int i = 0; i < arriveAtMeLinks.size(); i++) {
			Link theLink = (Link) arriveAtMeLinks.get(i);
			if ("is-a".equals(theLink.getLabel())) {
				isaTails.add(theLink.getTail());
			}
		}
		return isaTails;
	}

	public void addDepartFromMeLinks(Link theLink) {
		departFromMeLinks.add(theLink);
	}

	public ArrayList<Link> getDepartFromMeLinks() {
		return departFromMeLinks;
	}

	//追加 kiyo 11/19 15:38
	public int getInheritance(ArrayList<Link> links){

		int count = 0;
		for(Link link : links){
			if(link.inheritance){
				count++;
			}
		}
		
		return(count);
	}
	

	
	
	public void addArriveAtMeLinks(Link theLink) {
		arriveAtMeLinks.add(theLink);
	}

	public ArrayList<Link> getArriveAtMeLinks() {
		return arriveAtMeLinks;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}
}
