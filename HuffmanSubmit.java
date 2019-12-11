import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Scanner;


public class HuffmanSubmit implements Huffman {
	/*ENCODES FILE*/
	public void encode(String inputFile, String outputFile, String freqFile){
		// TODO: Your code here
        ArrayList<crypt> charList=buildList(inputFile);
        writefreq(charList,freqFile);
        HuffNode root=buildTree(buildNodesFromFile(freqFile));
        Hashtable<Character,String> BinCodes=new Hashtable<>();
        giveBinEnc(root,BinCodes,"");
        BinaryOut out=new BinaryOut(outputFile);
        BinaryIn in=new BinaryIn(inputFile);
        while(!in.isEmpty()){
            //reads from input and writes codes to output
            char [] code=BinCodes.get(in.readChar()).toCharArray();
            for(int i=0;i<code.length;i++){
                if(code[i]=='0')
                    out.write(false);
                else
                    out.write(true);
            }

        }
        out.close();
        out.flush();
   }

    /*DECODES FILE*/
   public void decode(String inputFile, String outputFile, String freqFile){
		// TODO: Your code here
       HuffNode root=buildTree(buildNodesFromFile(freqFile));
       Hashtable<String, Character> codesToChar=new Hashtable<>();
       giveBinDec(root,codesToChar,"");
       BinaryOut out=new BinaryOut(outputFile);
       BinaryIn in=new BinaryIn(inputFile);
       String key="";
       while(!in.isEmpty()){
           //reads from input and writes to output based off codes
           if(in.readBoolean()==true)
               key+="1";
           else
               key+="0";
           if(codesToChar.containsKey(key)){
               out.write(codesToChar.get(key));
               key="";
           }

       }
       out.flush();
       out.close();
   }



   /**
   * Inner class that creates
   * custom data type called crypt
   * that contains a character and
    * that character's weight
   */
    class crypt{
	    private int weight;
	    private char code;

	     public crypt(char code){
	         this.code=code;
	         weight=1;
         }

         /*Increments weight of character*/
         public void incWeight(){
	         weight++;
         }
        /*GETTERS AND SETTERS*/
       public int getWeight() {
           return weight;
       }

       public void setWeight(int weight) {
           this.weight = weight;
       }

       public char getCode() {
           return code;
       }

       public void setCode(char code) {
           this.code = code;
       }
   }
    /**
     * Inner class for nodes
     * of the huffman tree
    */
    class HuffNode implements Comparable{
        private int weight;
        private char data;
        private HuffNode left;
        private HuffNode right;
        private boolean isLeaf;

        public HuffNode(char data, int weight){
            this.weight=weight;
            this.data=data;
            isLeaf=true;
            this.left=null;
            this.right=null;
        }

        public HuffNode(int weight, HuffNode left, HuffNode right){
            this.weight=weight;
            this.left=left;
            this.right=right;
            isLeaf=false;
        }

        /*Compares nodes based off of their weight
        * will be used to construct priority queue
        * @param o, object to be compared to
        * @return int, -1 if other object is bigger,
        * 0 if they are equal, 1 if thus object has
        * a bigger weight
        * */
        @Override
        public int compareTo(Object o){
            HuffNode node= (HuffNode) o;
            if(node.weight>this.weight)
                return -1;
            else if(node.weight==this.weight)
                return 0;
            else
                return 1;
        }

        /*GETTERS AND SETTERS*/
        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public char getData() {
            return data;
        }

        public void setData(char data) {
            this.data = data;
        }

        public HuffNode getLeft() {
            return left;
        }

        public void setLeft(HuffNode left) {
            this.left = left;
        }

        public HuffNode getRight() {
            return right;
        }

        public void setRight(HuffNode right) {
            this.right = right;
        }

        public boolean isLeaf() {
            return isLeaf;
        }

        public void setLeaf(boolean leaf) {
            isLeaf = leaf;
        }
    }




    /*Method that will search for a character
    * within List of crypt, finds its index
    * @param list, arraylist of type crypt
    * @param c target character
    * @return int index of target element, -1 if not present*/
    public int indexOf(ArrayList<crypt> list, char c){
	    for(int i=0; i<list.size();i++){
	        if(list.get(i).getCode()==c)
	            return i;
        }
	    return -1;
    }

    /*Searches for a specified character within an
    * Arraylist of type crypt
    * @param list, ArrayList of type crypt
    * @param c, target char
    * @return boolean, true if contains target, false otherwise
    * */
    public boolean containsChar(ArrayList<crypt> list, char c){
	    for(int i=0; i<list.size();i++){
	        if(list.get(i).getCode()==c)
	            return true;
        }
	    return false;

    }


    /*Builds list of type crypt from input file
    * @param filepath, path to input file
    * @return charList, built list
    * */
    public ArrayList<crypt> buildList(String filepath){
        ArrayList<crypt> charList=new ArrayList<>();
	    BinaryIn in=new BinaryIn(filepath);
	    while(!in.isEmpty()){//iterates through each character in input file
	       char c=in.readChar();
	        if(containsChar(charList,c)){//checks if list contains the current character
	            int index=indexOf(charList,c);//if it does gets index
	            charList.get(index).incWeight();//increments weight
            }
	        else
	            charList.add(new crypt(c));//if it does not contains current char adds it to list
        }
    return charList;
    }


    /*
    * Writes frequency file from
    * array list of characters with weights
    * @param list, arrayList of custom data
    * type crypt containing character and their weights
    * @param freqfile file path to output for freq file.
    */
    public void writefreq(ArrayList<crypt> list,String freqfile){
        BinaryOut out=new BinaryOut(freqfile);
	    for(int i=0; i<list.size();i++) {
            String bin = Integer.toBinaryString(list.get(i).getCode());//converts character to binary string
            char[] binchar = bin.toCharArray();
            if (binchar.length < 8) {//checks if binary sequence is of length 8
                char[] paddedbinchar = new char[8];
                for (int x = 0; x < 8-binchar.length; x++)//"pads" binary sequence with 0s at the front
                    paddedbinchar[x] = '0';
                int currEl=0;
                for (int y = 8-binchar.length; y < 8 ; y++) {//adds unpadded string to end of 0s, creating entirely padded string
                    paddedbinchar[y]=binchar[currEl];
                    currEl++;
                }
                binchar=paddedbinchar;//sets original binary sequence to padded sequence
            }
            for (int j = 0; j < binchar.length; j++) {//iterates through each character of binary string
                    out.write(binchar[j]);
            }
                out.write(":" + list.get(i).weight + "\n");

            }
	    out.close();
    }

    /*Creates huffman tree from priority queue
    * Works by continuously making subtrees out of
    * nodes with two lowest weights, and the root of the
    * new subtree is combined weights of its children.
    * Does this until there is only one node, root of entire tree
    * @param nodeQueue, Priority Queue of nodes sorted by weight
    * @return nodeQueue.poll(), root of constructed huffman tree
    * */
    public HuffNode buildTree(PriorityQueue<HuffNode> nodeQueue){
        while(nodeQueue.size()>1){//iterates through priority queue
            HuffNode temp =nodeQueue.poll();//gets two nodes with lowest weight
            HuffNode temp1=nodeQueue.poll();
            /*Creates new internal node with weight of two smallest nodes combined*/
            HuffNode newNode =new HuffNode(temp.getWeight()+temp1.getWeight(),temp,temp1);
            nodeQueue.add(newNode);//puts new node back in priority queue and continues
        }
        return nodeQueue.poll();
    }

    /*Traverses Huffman tree and assigns binary codes to characters
    * according to frequency and stores them in HashTable for encoding
    * @param root, the root of the huffman tree
    * @param binCodes, hashtable to store the characters and strings in
    * @param s, string that represents current binary seq, passed as param
    * because method is called recursively
    * */
    public void giveBinEnc(HuffNode root, Hashtable<Character,String> binCodes, String s){
        if(root.isLeaf)
            binCodes.put(root.getData(),s);
        else{
            giveBinEnc(root.left,binCodes,s+"0");
            giveBinEnc(root.right,binCodes,s+"1");
        }

    }

    /*Traverses huffman tree and assigns binary codes to characters for decoding, stores
    * them in hashtable with string as key and character as object
    * @param root, the root of the huffman tree
    * @param binCodes, hashtable to store the characters and strings in
    * @param s, string that represents current binary seq, passed as param
    * because method is called recursively
    * */
    public void giveBinDec(HuffNode root, Hashtable<String, Character> binCodes, String s){
        if(root.isLeaf)
            binCodes.put(s,root.getData());
        else{
            giveBinDec(root.left,binCodes,s+"0");
            giveBinDec(root.right,binCodes,s+"1");
        }

    }

    /*Method that creates the Nodes for the huffman tree
    * from the frequency file
    * @param file, the filepath for frequency file
    * @return nodes, priority queue of huffman nodes*/
    public PriorityQueue<HuffNode> buildNodesFromFile(String file){
        PriorityQueue<HuffNode> nodes=new PriorityQueue<>();
        try{
            Scanner sc=new Scanner(new BufferedReader(new FileReader(file)));
            while (sc.hasNext()){
                String [] freq=sc.nextLine().split(":");
                char c=(char)Integer.parseInt(freq[0],2);//converts binary string to char
                nodes.add(new HuffNode(c,Integer.parseInt(freq[1])));
            }
        }catch (FileNotFoundException e){e.printStackTrace();}
        return nodes;
    }

   public static void main(String[] args) {
      Huffman  huffman = new HuffmanSubmit();
      huffman.encode("Essay.docx", "ur.enc", "freq.txt");
      huffman.decode("ur.enc", "NewEssay.docx", "freq.txt");
      huffman.encode("alice30.txt", "alice30.enc", "freq.txt");
      huffman.decode("alice30.enc", "alice30_dec.txt", "freq.txt");

       JFrame frame=new JFrame();
       Canvas c=new Canvas();
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setSize(800,800);
       frame.setLayout(new GridLayout());
       frame.add(new JTextField());
       frame.add(c);
       frame.add(new JButton("Encode"));
       frame.add(new JButton("decode"));


       frame.setVisible(true);

		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
   }

}
