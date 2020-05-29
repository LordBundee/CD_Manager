package cdmanager;

public class BinaryTree
{
    BinaryNode root;

    public void addNode(int key, String name)
    {
        // Create a new BinaryNode and initialize it
        BinaryNode newNode = new BinaryNode(key, name);
        // If there is no root this becomes root
        if (root == null)
        {
            root = newNode;
        } else
        {
            // Set root as the BinaryNode we will start
            // with as we traverse the tree
            BinaryNode focusNode = root;
            // Future parent for our new BinaryNode
            BinaryNode parent; 

            while (true)
            {
                // root is the top parent so we start
                // there
                parent = focusNode;
                // Check if the new node should go on
                // the left side of the parent node
                if (key < focusNode.key)
                {
                    // Switch focus to the left child
                    focusNode = focusNode.leftChild;
                    // If the left child has no children
                    if (focusNode == null)
                    {
                        // then place the new node on the left of it
                        parent.leftChild = newNode;
                        return; // All Done
                    }
                } else
                { 
                    // If we get here put the node on the right
                    focusNode = focusNode.rightChild;
                    // If the right child has no children
                    if (focusNode == null)
                    {
                        // then place the new node on the right of it
                        parent.rightChild = newNode;
                        return; // All Done
                    }
                }
            }
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Binary Tree Traversal Methods"> 
    
    // All nodes are visited in ascending order
    // Recursion is used to go to one node and
    // then go to its child nodes and so forth
    public void inOrderTraverseTree(BinaryNode focusNode)
    {
        if (focusNode != null)
        {
            inOrderTraverseTree(focusNode.leftChild);
            System.out.println(focusNode);
            inOrderTraverseTree(focusNode.rightChild);
        }
    }
    // All nodes are visited in pre order
    // Recursion is used to go to one node and
    // then go to its child nodes and so forth
    public void preOrderTraverseTree(BinaryNode focusNode)
    {
        if (focusNode != null)
        {
            System.out.println(focusNode);
            preOrderTraverseTree(focusNode.leftChild);
            preOrderTraverseTree(focusNode.rightChild);
        }
    }

    // All nodes are visited in post order
    // Recursion is used to go to one node and
    // then go to its child nodes and so forth
    public void postOrderTraverseTree(BinaryNode focusNode)
    {
        if (focusNode != null)
        {
            postOrderTraverseTree(focusNode.leftChild);
            postOrderTraverseTree(focusNode.rightChild);
            System.out.println(focusNode);
        }
    }
    
    //Traversals as above but Returning printable string
    //in line-by-line format
    //
    public String inOrderTraverseTree(String myString, BinaryNode focusNode)
    {
        if (focusNode != null)
        {
            myString = inOrderTraverseTree(myString,focusNode.leftChild);
            myString += focusNode.myText + "\n";
            myString = inOrderTraverseTree(myString,focusNode.rightChild);
        }
        return myString;
    }
     
    public String preOrderTraverseTree(String myString, BinaryNode focusNode)
    {
        if (focusNode != null)
        {
            myString += focusNode.myText + "\n";
            myString = preOrderTraverseTree(myString, focusNode.leftChild);
            myString = preOrderTraverseTree(myString, focusNode.rightChild);
        }
        return myString;
    }

    public String postOrderTraverseTree(String myString, BinaryNode focusNode)
    {
        if (focusNode != null)
        {
            myString = postOrderTraverseTree(myString, focusNode.leftChild);
            myString = postOrderTraverseTree(myString, focusNode.rightChild);
            myString += focusNode.myText + "\n";
        }
        return myString;
    }

    //</editor-fold>  
     
    //<editor-fold defaultstate="collapsed" desc="Node Search">
    public BinaryNode findNode(int key)
    {
        // Start at the top of the tree
        BinaryNode focusNode = root;
        // While we haven't found the BinaryNode
        // keep looking
        while (focusNode.key != key)
        {
            // If we should search to the left
            if (key < focusNode.key)
            {
                // Shift the focus BinaryNode to the left child
                focusNode = focusNode.leftChild;
            } else
            {
                // Shift the focus BinaryNode to the right child
                focusNode = focusNode.rightChild;
            }
            
            // The node wasn't found
            if (focusNode == null)
            {
                return null;
            }
        }
        return focusNode;
    }
//</editor-fold>
    
    //Nested Binary Node Class To Be Utilised By Main Class
    class BinaryNode
    {
        //Binary Node Class Variables
        int key;
        String myText;

        BinaryNode leftChild;
        BinaryNode rightChild;

        //Binary Node Constructor
        BinaryNode(int key, String name)
        {
            this.key = key;
            this.myText = name;
        }
    }
}


