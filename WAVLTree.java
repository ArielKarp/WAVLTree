/*
 * Uri Geva, username: urigeva, ID: 204570071
 * Ariel Karpilovski, username: karpilovski, ID: 308552454
 */
import java.io.IOException;

/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree with distinct integer keys and info
 *
 *
 */

public class WAVLTree {

	/*
	 * WAVLTree Params: WAVLNode sentinel- Used to contain the root of the tree.
	 * WAVLNode min_Node, max_Node- Nodes that contain minimum and maximum keys,
	 * respectively WAVLNode EX_LEAF- Is our external leaf with rank "-1". int
	 * size- Contains the size of the WAVLTree.
	 */
	private WAVLNode sentinel;
	private WAVLNode min_Node, max_Node;
	private final WAVLNode EX_LEAF = new WAVLNode();
	private int size;

	// Tester
	public static void main(String[] args) throws IOException {
		WAVLTree wtree = new WAVLTree();
		System.out.println("The number of reb ins: " + wtree.insert(3, "a"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb ins: " + wtree.insert(100, "b"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb ins: " + wtree.insert(40, "c"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb ins: " + wtree.insert(120, "d"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb ins: " + wtree.insert(56, "e"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb ins: " + wtree.insert(70, "f"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb ins: " + wtree.insert(49, "g"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb ins: " + wtree.insert(0, "h"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb ins: " + wtree.insert(45, "i"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb ins: " + wtree.insert(140, "j"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb ins: " + wtree.insert(110, "k"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb ins: " + wtree.insert(80, "l"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb ins: " + wtree.insert(75, "m"));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb del: " + wtree.delete(75));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb del: " + wtree.delete(100));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb del: " + wtree.delete(120));
		wtree.printTree();
		printLines();
		System.out.println("The number of reb del: " + wtree.delete(49));
		wtree.printTree();
		printLines();
		System.out.println(wtree.search(57));

	}

	/// for testing
	public static void printLines() {
		for (int i = 0; i <= 20; i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	public void printTree() throws IOException {
		printTree(this.getRoot());
	}

	public void printTree(WAVLNode root) throws IOException {
		if (!is_Ex(root.getRight())) {
			printTree(root.getRight(), true, "");
		}
		root.printNodeValue();
		if (!is_Ex(root.getLeft())) {
			printTree(root.getLeft(), false, "");
		}
	}

	// use string and not stringbuffer on purpose as we need to change the
	// indent at each recursion
	private void printTree(WAVLNode root, boolean isRight, String indent) throws IOException {
		if (!is_Ex(root.getRight())) {
			printTree(root.getRight(), true, indent + (isRight ? "        " : " |      "));
		}
		System.out.print(indent);
		if (isRight) {
			System.out.print(" /");
		} else {
			System.out.print(" \\");
		}
		System.out.print("----- ");
		root.printNodeValue();
		if (!is_Ex(root.getLeft())) {
			printTree(root.getLeft(), false, indent + (isRight ? " |      " : "        "));
		}
	}
	///// end for testing

	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *
	 */

	// Return true if the root is external leaf, false otherwise
	public boolean empty() {
		return (is_Ex(this.getRoot()));
	}

	// Creates a new WAVLTree with a sentinel
	public WAVLTree() {
		this.sentinel = new WAVLNode(Integer.MAX_VALUE, null);
		this.min_Node = null;
		this.max_Node = null;
		this.size = 0;
	}

	// Return the root of the tree (will always be the left child of the sentinel)
	public WAVLNode getRoot() {
		return this.sentinel.getLeft();
	}

	//getters ans setters
	public WAVLNode getMin_Node() {
		return this.min_Node;
	}

	public void setMin_Node(WAVLNode min_Node) {
		this.min_Node = min_Node;
	}

	public WAVLNode getMax_Node() {
		return this.max_Node;
	}

	public void setMax_Node(WAVLNode max_Node) {
		this.max_Node = max_Node;
	}

	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 */
	// Utilizes search with the root of the tree and k as key
	public String search(int k) {
		return search(this.getRoot(), k);
	}

	private String search(WAVLNode search_node, int k) {
		// End search if found the node OR reached an external leaf
		if (is_Ex(search_node) || k == search_node.getKey())
			return search_node.getInfo();
		//decide whether to search to the right or the left according the key
		if (search_node.getKey() < k)
			return search(search_node.getRight(), k);
		else
			return search(search_node.getLeft(), k);
	}

	/**
	 * public int insert(int k, String i)
	 *
	 * inserts an item with key k and info i to the WAVL tree. the tree must
	 * remain valid (keep its invariants). returns the number of rebalancing
	 * operations, or 0 if no rebalancing operations were necessary. returns -1
	 * if an item with key k already exists in the tree.
	 */
	public int insert(int k, String i) {
		WAVLNode root_Node = this.sentinel;
		// Find where should key k be inserted
		WAVLNode parent = tree_position(root_Node, k);
		// Returns -1 if key was found in the tree, no need to insert a new node
		if (parent.getKey() == k)
			return -1;
		// Define nNode- new Node to inserted into the tree
		WAVLNode nNode = new WAVLNode(k, i);
		nNode.setParent(parent);
		// Set nNode to be left/right child of parent
		if (nNode.getKey() < parent.getKey())
			parent.setLeft(nNode);
		else
			parent.setRight(nNode);

		// Update size of the tree
		this.size++;
		// Update min/max nodes if necessary
		if (min_Node == null || k < min_Node.getKey())
			this.setMin_Node(nNode);
		if (max_Node == null || k > max_Node.getKey())
			this.setMax_Node(nNode);

		// reblancing the tree and return number of reb required
		return insertFixUp(nNode);
	}

	// Return the number of rebalancing operations
	private int insertFixUp(WAVLNode nNode) {
		int numReb = 0;
		WAVLNode child = nNode;
		WAVLNode parent = nNode.getParent();
		WAVLNode brother, grandchild;
		boolean isBalanced = false;
		boolean leftCase;
		int flag = 0;
		// If after insertion ranks are leager, return 0
		if (parent.getRank() - child.getRank() != 0)
			return numReb;
		// Run while tree is not balanced OR did not reach sentinel
		while (!isBalanced && parent != this.sentinel) {
			leftCase = child.isLeftChild();
			brother = leftCase ? parent.getRight() : parent.getLeft();
			flag = Math.abs(parent.getRank() - brother.getRank());

			// Case 1: Promote
			if (flag == 1) {
				parent.promote();
				numReb++;
				child = parent;
				parent = parent.getParent();
				// If Case 1 fixes the balance
				if (parent == this.sentinel || Math.abs(parent.getRank() - child.getRank()) != 0)
					isBalanced = true;
			} else {
				// Case 2: right/left rotation
				// Test which is the current case (left/right)
				grandchild = leftCase ? child.getRight() : child.getLeft();
				if (Math.abs(child.getRank() - grandchild.getRank()) == 2) {
					if (leftCase)
						rotateRight(child);
					else
						rotateLeft(child);
					parent.demote();
					numReb += 1;
				}
				// Case 3: Double right/left rotation
				else {
					//doubleRotate to the left/ right according to which case
					if (leftCase)
						doubleRotateRight(child.getRight());
					else
						doubleRotateLeft(child.getLeft());

					child.demote();
					parent.demote();
					child.getParent().promote();
					numReb += 2;
				}
				isBalanced = true;
			}
		}
		return numReb;
	}

	// Rotate right the rotated node- rotateNode
	private void rotateRight(WAVLNode rotateNode) {
		WAVLNode rotateParent = rotateNode.getParent();
		transplant(rotateParent, rotateNode);
		setAsLeftChild(rotateParent, rotateNode.getRight());
		setAsRightChild(rotateNode, rotateParent);
	}

	// Rotate left the rotated node- rotateNode
	private void rotateLeft(WAVLNode rotateNode) {
		WAVLNode rotateParent = rotateNode.getParent();
		transplant(rotateParent, rotateNode);
		setAsRightChild(rotateParent, rotateNode.getLeft());
		setAsLeftChild(rotateNode, rotateParent);
	}

	// Double rotate right contains, by definition:
	// 1 rotation left
	// 1 rotation right
	private void doubleRotateRight(WAVLNode rotateNode) {
		rotateLeft(rotateNode);
		rotateRight(rotateNode);
	}

	// Double rotate right contains, by definition:
	// 1 rotation right
	// 1 rotation left
	private void doubleRotateLeft(WAVLNode rotateNode) {
		rotateRight(rotateNode);
		rotateLeft(rotateNode);
	}

	// Tree_position procedure as defined in class- find the parent node to
	// insert to (or the node itself if found)
	private WAVLNode tree_position(WAVLNode node, int k) {
		WAVLNode parent = null;
		while (!is_Ex(node)) {
			parent = node;
			if (k == node.getKey())
				return node;
			else if (k < node.getKey())
				node = node.getLeft();
			else
				node = node.getRight();
		}
		return parent;
	}

	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there; the tree
	 * must remain valid (keep its invariants). returns the number of
	 * rebalancing operations, or 0 if no rebalancing operations were needed.
	 * returns -1 if an item with key k was not found in the tree.
	 */
	public int delete(int k) {
		WAVLNode root_Node = this.sentinel;
		// Find the node in the tree
		WAVLNode node = tree_position(root_Node, k);
		// If the key is not found in the tree- return -1
		if (node.getKey() != k)
			return -1;

		// In case deleted node is min/max define two booleans to be tested
		// later
		final boolean delMin = (min_Node == node);
		final boolean delMax = (max_Node == node);

		WAVLNode delNode;
		// if node is a leaf or an unary node then define delNode to be node
		if (is_Ex(node.getLeft()) || is_Ex(node.getRight()))
			delNode = node;
		// else, find the successor to be deleted instead
		else
			delNode = successor(node);

		WAVLNode delChildNode;
		if (is_Ex(delNode.getLeft()))
			delChildNode = delNode.getRight();
		else
			delChildNode = delNode.getLeft();

		// If is not external child, create a detour for delChildNode to parent
		// of delNode
		delChildNode.setParent(delNode.getParent());

		WAVLNode delParent = delNode.getParent();
		if (delNode.isLeftChild())
			delParent.setLeft(delChildNode);
		else
			delParent.setRight(delChildNode);

		// If we deleted the successor, needs to replace it with the node
		if (delNode != node) {
			node.setKey(delNode.getKey());
			node.setInfo(delNode.getInfo());
		}

		//update the size of the tree
		this.size--;

		// Update min_Node, max_Node if necessary
		if (delMin)
			this.setMin_Node(is_Ex(getRoot()) ? null : minNode(getRoot()));
		if (delMax)
			this.setMax_Node(is_Ex(getRoot()) ? null : maxNode(getRoot()));

		//reblancing the tree after deletion and return number of reb required
		return deleteFixUp(delChildNode);

	}

	private int deleteFixUp(WAVLNode childDeletedNode) {
		int numReb = 0;
		WAVLNode child = childDeletedNode;
		WAVLNode parent = childDeletedNode.getParent();
		WAVLNode brother, grandchild1, grandchild2;
		boolean isBalanced = false;
		boolean leftCase;
		int flag = 0;

		leftCase = child.isLeftChild();
		brother = leftCase ? parent.getRight() : parent.getLeft();

		// immediate handle cases, no further reb required
		int rankdiff = Math.abs(parent.getRank() - brother.getRank());
		if (Math.abs(parent.getRank() - child.getRank()) == 2) {
			if (is_Ex(child) && rankdiff == 1)
				return numReb;

			if (!is_Ex(child) && (rankdiff == 1 || rankdiff == 2))
				return numReb;
		}

		// Run while tree is not balanced OR did not reach sentinel
		while (!isBalanced && parent != this.sentinel) {
			leftCase = child.isLeftChild();
			brother = leftCase ? parent.getRight() : parent.getLeft();
			flag = Math.abs(parent.getRank() - brother.getRank());

			// case 1: Demote
			if (flag == 2) {
				parent.demote();
				numReb++;
				child = parent;
				parent = parent.getParent();
				//if case 1 fixed the tree
				if (parent == this.sentinel || Math.abs(parent.getRank() - child.getRank()) == 2)
					isBalanced = true;

			} else {
				// case 2: double demote
				//update nodes depends which case 
				grandchild1 = leftCase ? brother.getRight() : brother.getLeft();
				grandchild2 = leftCase ? brother.getLeft() : brother.getRight();

				if (Math.abs(brother.getRank() - grandchild1.getRank()) == 2) {
					if (Math.abs(brother.getRank() - grandchild2.getRank()) == 2) {

						parent.demote();
						brother.demote();
						numReb += 2;
						child = parent;
						parent = parent.getParent();
						//if case 2 fixed the tree
						if (parent == this.sentinel || Math.abs(parent.getRank() - child.getRank()) == 2)
							isBalanced = true;
					} else {
						// case 4: double rotate

						//doubleRotate left/ right depends which case
						if (leftCase) {
							brother.getLeft().promote();
							brother.getLeft().promote();
							doubleRotateLeft(brother.getLeft());

						} else {
							brother.getRight().promote();
							brother.getRight().promote();
							doubleRotateRight(brother.getRight());
						}
						parent.demote();
						parent.demote();
						brother.demote();

						numReb += 2;
						isBalanced = true;
					}
				} else {
					// case 3: rotate

					 //rotate right/left depends which case
					if (leftCase)
						rotateLeft(brother);
					else
						rotateRight(brother);

					parent.demote();
					brother.promote();
					numReb++;
					// special case, if a 2-2 leaf was created during rotate
					if (is_Ex(parent.getLeft()) && is_Ex(parent.getRight())
							&& (Math.abs(parent.getRank() - parent.getRight().getRank()) == 2)
							&& (Math.abs(parent.getRank() - parent.getLeft().getRank()) == 2)) {
						parent.demote();
					}

					isBalanced = true;
				}
			}
		}
		return numReb;
	}

	private WAVLNode successor(WAVLNode node) {
		//if the node has a right child, the successor is the 
		//left most child in the right sub-tree ("once right, then all the way to the left")
		if (!is_Ex(node.getRight())) {
			return minNode(node.getRight());
		}
		//if not, the successor is the first node we are in its left sub-tree 
		//("all the way up, then to the right once")
		WAVLNode parent = node.getParent();
		while (!is_Ex(parent) && node.isRightChild()) {
			node = parent;
			parent = parent.getParent();
		}
		return parent;
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree, or null
	 * if the tree is empty
	 */
	//we have members of min_max_Nodes in the tree, so just return the info of them
	public String min() {
		if (min_Node == null)
			return null;
		return this.min_Node.getInfo();
	}

	//in case the min_Node was deleted, 
	//find the new min_Node by go down all the way to the left
	private WAVLNode minNode(WAVLNode root) {
		WAVLNode node = root;
		while (!is_Ex(node.getLeft()))
			node = node.getLeft();
		return node;
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if
	 * the tree is empty
	 */
	//return the info of the max_node
	public String max() {
		if (max_Node == null)
			return null;
		return this.max_Node.getInfo();
	}

	//in casw the max_Node was deleted,
	//find the new max_Node by go down all the way to thr right
	private WAVLNode maxNode(WAVLNode root) {
		WAVLNode node = root;
		while (!is_Ex(node.getRight()))
			node = node.getRight();
		return node;
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty
	 * array if the tree is empty.
	 */
	public int[] keysToArray() {
		int[] sorted_keys = new int[this.size()];
		inOrderKeysInfoToArray(this.getRoot(), sorted_keys, null, 0);
		return sorted_keys;
	}

	//putting the keys, infos in array recursively
	private int inOrderKeysInfoToArray(WAVLNode node, int[] keys, String[] infos, int index) {
		if (is_Ex(node)) 
			return index;

		index = inOrderKeysInfoToArray(node.getLeft(), keys, infos, index);
		if (keys != null)
			keys[index] = node.getKey();
		if (infos != null)
			infos[index] = node.getInfo();
		index++;
		
		return inOrderKeysInfoToArray(node.getRight(), keys, infos, index);
	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 */
	public String[] infoToArray() {
		String[] sorted_info = new String[this.size()];
		inOrderKeysInfoToArray(this.getRoot(), null, sorted_info, 0);
		return sorted_info;
	}

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 *
	 * precondition: none postcondition: none
	 */
	//we have a member of size, so just need to return it
	public int size() {
		return this.size;
	}

	//return true, iff the node is a EX_LEAF type
	public boolean is_Ex(WAVLNode node) {
		return (node == EX_LEAF);
	}

	//set the second node as the new son of parent first node (for rotate)
	private void transplant(WAVLNode k, WAVLNode l) {
		if (k.isLeftChild())
			setAsLeftChild(k.getParent(), l);
		else
			setAsRightChild(k.getParent(), l);
	}

	//set the second child as the new right child of the first node (for rotate)
	private void setAsRightChild(WAVLNode parent, WAVLNode child) {
		parent.setRight(child);
		if (!is_Ex(child))
			child.setParent(parent);
	}

	//set the second child as the new left child of the first node (for rotate)
	private void setAsLeftChild(WAVLNode parent, WAVLNode child) {
		parent.setLeft(child);
		if (!is_Ex(child))
			child.setParent(parent);
	}

	/**
	 * public class WAVLNode
	 *
	 * If you wish to implement classes other than WAVLTree (for example
	 * WAVLNode), do it in this file, not in another file. This is an example
	 * which can be deleted if no such classes are necessary.
	 */
	public class WAVLNode {

		/*
		 * WAVLNode Params: int key- Contains the key of the node. String info-
		 * Contains the info of the node. WAVLNode parent. WAVLNode left, right-
		 * Contain the left and right child nodes, respectively. int rank-
		 * Contains the rank of the node, as defined in class.
		 */
		private int key;
		private String info;
		private WAVLNode parent, left, right;
		private int rank;

		// Regular node constructor, as defined in class
		public WAVLNode(int key, String info) {
			this.key = key;
			this.info = info;
			this.left = EX_LEAF;
			this.right = EX_LEAF;
			this.rank = 0;
		}

		// special external leaf constructor, with rank -1
		public WAVLNode() {
			this.key = Integer.MIN_VALUE;
			this.info = null;
			this.left = null;
			this.right = null;
			this.rank = -1;
		}

		//return true iff the node is a left child of its parent
		public boolean isLeftChild() {
			return (this == this.parent.left);
		}

		//return true iff the node is a right child of its parent 
		public boolean isRightChild() {
			return (this == this.parent.right);
		}

		//promote the rank of the node by 1
		private void promote() {
			this.setRank(this.getRank() + 1);
		}

		//demote the rank of the node by 1
		private void demote() {
			this.setRank(this.getRank() - 1);
		}

		//getters and setters
		public WAVLNode getRight() {
			return this.right;
		}

		public WAVLNode getLeft() {
			return this.left;
		}

		public int getKey() {
			return this.key;
		}

		public String getInfo() {
			return this.info;
		}

		public int getRank() {
			return this.rank;
		}

		public WAVLNode getParent() {
			return this.parent;
		}

		public void setParent(WAVLNode parent) {
			this.parent = parent;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public void setInfo(String info) {
			this.info = info;
		}

		public void setLeft(WAVLNode left) {
			this.left = left;
		}

		public void setRight(WAVLNode right) {
			this.right = right;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}

		// for testing
		public void printNodeValue() throws IOException {
			if (this.getKey() == -17) {
				System.out.print("ExLeaf");
			} else {
				System.out.print(this.getKey() + "," + this.getRank());
			}
			System.out.print("\n");
		}

	}

}
