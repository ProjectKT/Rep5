package util;

import java.util.ArrayList;

import semanticnet.Link;
import semanticnet.Node;

public class SemanticNetUtils {
	/**
	 * 繋がってる全部のリンクを返す
	 * @param node
	 * @return
	 */
	public static ArrayList<Link> getConnectedLinks(Node node) {
		ArrayList<Link> links = new ArrayList<Link>(node.getDepartFromMeLinks());
		links.addAll(node.getArriveAtMeLinks());
		return links;
	}
}
