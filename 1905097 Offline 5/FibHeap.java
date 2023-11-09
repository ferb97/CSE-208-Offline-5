import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Node{
    private Node parent;
    private Node child;
    private Node left;
    private Node right;
    private int key;
    private int degree;
    private boolean mark;
    private int index;

    Node(){
        this.parent = null;
        this.child = null;
        this.left = this;
        this.right = this;
        this.key = Integer.MAX_VALUE;
        this.degree = 0;
        this.mark = false;
        this.index = -1;
    }

    Node(int k){
        this();
        this.key = k;
    }

    public void setChild(Node child) {
        this.child = child;
    }

    public Node getChild() {
        return child;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getLeft() {
        return left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getRight() {
        return right;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public boolean isMark() {
        return mark;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}


class pair{
    int to;
    int weight;

    pair(){
        to = -1;
        weight = -1;
    }

    pair(int to, int weight){
        this.to = to;
        this.weight = weight;
    }
}

public class FibHeap {
    Node minNode;
    int totalNodes;

    FibHeap(){
        minNode = null;
        totalNodes = 0;
    }

    public boolean empty(){
        return totalNodes <= 0;
    }

    public int size(){
        return totalNodes;
    }

    public void insert(Node x){
        if(minNode == null){
            minNode = x;
            x.setLeft(x);
            x.setRight(x);
        }
        else{
            x.setRight(minNode);
            x.setLeft(minNode.getLeft());
            minNode.getLeft().setRight(x);
            minNode.setLeft(x);
            if(x.getKey() < minNode.getKey()){
                minNode = x;
            }
        }

        totalNodes += 1;
    }

    private void insert(Node x, boolean inc){
        insert(x);
        totalNodes -= 1;
    }

    public void insert(int key){
        insert(new Node(key));
    }

    public static void unionHeap(FibHeap H1, FibHeap H2, FibHeap H3){
        H3.minNode = H1.minNode;
        if(H1.minNode == null || (H2.minNode != null && H2.minNode.getKey() < H1.minNode.getKey())){
            H3.minNode = H2.minNode;
        }
        if(H1.minNode != null && H2.minNode != null){
            Node tmp1 = H1.minNode.getLeft();
            Node tmp2 = H2.minNode.getLeft();
            H1.minNode.setLeft(tmp2);
            tmp2.setRight(H1.minNode);
            H2.minNode.setLeft(tmp1);
            tmp1.setRight(H2.minNode);
        }
        H3.totalNodes = H1.totalNodes + H2.totalNodes;
    }

    public Node findMin(){
        return minNode;
    }

    public Node extractMin(){
        Node n1 = this.minNode;
        if(n1 != null) {
            Node n2 = n1.getChild();

            if (n2 != null) {

                int cnt2 = 1;
                while(cnt2 <= n1.getDegree()) {
                    Node tmp = n2.getRight();

                    n2.getRight().setLeft(n2.getLeft());
                    n2.getLeft().setRight(n2.getRight());
                    n2.setParent(null);
                    insert(n2, true);

                    n2 = tmp;
                    cnt2++;
                }
            }
            Node tmp3 = n1.getRight();

            n1.getLeft().setRight(n1.getRight());
            n1.getRight().setLeft(n1.getLeft());
            n1.setChild(null);

            totalNodes -= 1;
            if(n1 == tmp3){
                this.minNode = null;
            }
            else {
                this.minNode = tmp3;

                consolidate();
            }
            return n1;
        }
        return null;
    }

    private void fibHeapLink(Node x, Node y){

        y.getRight().setLeft(y.getLeft());
        y.getLeft().setRight(y.getRight());
        Node c = x.getChild();

        if(c == null){
            y.setLeft(y);
            y.setRight(y);
        }
        else{
            y.setRight(c);
            y.setLeft(c.getLeft());

            c.getLeft().setRight(y);
            c.setLeft(y);
        }

        y.setParent(x);
        x.setChild(y);
        x.setDegree(x.getDegree() + 1);
        y.setMark(false);
    }

    private void consolidate(){
        double phi = (1 + Math.sqrt(5)) / 2;
        int Dn = (int) (Math.log(totalNodes) / Math.log(phi));
        Node[] A = new Node[Dn + 1];

        for(int i = 0; i <= Dn; i++){
            A[i] = null;
        }
        Node tmp = minNode;
        if(tmp != null){
            Node tmp1 = tmp;
            int cnt = 1;
            tmp = tmp.getRight();
            while(tmp != tmp1){
                cnt++;
                tmp = tmp.getRight();
            }
            for(int i = 0; i < cnt; i++){
                Node x = tmp;
                tmp1 = tmp.getRight();
                int d = x.getDegree();
                while(A[d] != null){
                    Node y = A[d];
                    if(y.getKey() < x.getKey()){
                        Node tmp2 = x;
                        x = y;
                        y = tmp2;
                        tmp = x;
                    }
                    fibHeapLink(x, y);

                    A[d] = null;
                    d += 1;
                }
                A[d] = x;

                tmp = tmp1;
            }

            minNode = null;

            for(int i = 0; i <= Dn; i++){
                if(A[i] != null) {
                    A[i].setLeft(null);
                    A[i].setRight(null);

                    insert(A[i], true);
                }
            }
        }
    }

    public void decreaseKey(Node x, int k){
        if(k > x.getKey())
           return;
        x.setKey(k);
        Node p = x.getParent();
        if(p != null && x.getKey() < p.getKey()){
            cut(x, p);
            cascadeCut(p);

        }

        if(x.getKey() < minNode.getKey()){
            minNode = x;
        }
    }

    private void cascadeCut(Node p) {
        Node p1 = p.getParent();
        if(p1 != null){
            if(!p.isMark()){
                p.setMark(true);
            }
            else{
                cut(p, p1);
                cascadeCut(p1);
            }
        }
    }

    private void cut(Node x, Node p) {
        x.getLeft().setRight(x.getRight());
        x.getRight().setLeft(x.getLeft());
        if(p.getDegree() > 1){
            p.setChild(x.getRight());
        }
        else{
            p.setChild(null);
        }
        p.setDegree(p.getDegree() - 1);
        x.setLeft(null);
        x.setRight(null);
        insert(x, true);
        x.setParent(null);
        x.setMark(false);
    }

    public void delete(Node x){
        decreaseKey(x, Integer.MIN_VALUE);
        Node n1 = extractMin();
    }


    public static void main(String[] args){

        int n = 10,m;
        List<List<pair> > adj = new ArrayList<List<pair> >();

        File file1 = new File("TestFile1.txt");
        try {

            FileReader fr1 = new FileReader(file1);
            BufferedReader br1 = new BufferedReader(fr1);
            String line = br1.readLine();
            String[] arr = line.split(" ");

            n = Integer.parseInt(arr[0]);
            m = Integer.parseInt(arr[1]);

            for(int i1 = 0; i1 < n; i1++){
                adj.add(new ArrayList<pair>());
            }

            for(int i = 0; i < m; i++){
                line = br1.readLine();
                arr = line.split(" ");
                int x = Integer.parseInt(arr[0]);
                int y = Integer.parseInt(arr[1]);
                int w = Integer.parseInt(arr[2]);
                adj.get(x).add(new pair(y, w));
                adj.get(y).add(new pair(x, w));
            }

            br1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Node[] nodeList = new Node[n];
        int[] dis = new int[n];
        int[] parent = new int[n];
        FibHeap heap = new FibHeap();
        BinaryHeapNode[] bnNodeList = new BinaryHeapNode[n];
        BinaryHeap binaryHeap = new BinaryHeap(n);
        int testcase = 10;

        File file2 = new File("TestFile2.txt");
        try {
            FileReader fr2 = new FileReader(file2);
            BufferedReader br2 = new BufferedReader(fr2);
            FileWriter fw = new FileWriter("Output.txt");
            String line = br2.readLine();
            testcase = Integer.parseInt(line);

            for(int j = 0; j < testcase; j++) {

                line = br2.readLine();
                String arr[] = line.split(" ");
                int source = Integer.parseInt(arr[0]);
                int dest = Integer.parseInt(arr[1]);

                for(int i = 0; i < n; i++){
                    dis[i] = Integer.MAX_VALUE;
                    parent[i] = -1;
                }

                long fibStart, fibEnd, binStart, binEnd;

                binStart = System.nanoTime();
                for(int i = 0; i < n; i++){
                    bnNodeList[i] = new BinaryHeapNode(Integer.MAX_VALUE, i, i);
                    binaryHeap.insert(bnNodeList[i]);
                }
                dis[source] = 0;
                binaryHeap.decreaseKey(bnNodeList[source], 0);

                while(!binaryHeap.empty()){
                    int v = binaryHeap.extractMin().getValue();

                    for(int i = 0; i < adj.get(v).size(); i++){
                        int to1 = adj.get(v).get(i).to;
                        int wi = adj.get(v).get(i).weight;

                        if(dis[v] + wi < dis[to1]){
                            dis[to1] = dis[v] + wi;
                            parent[to1] = v;
                            binaryHeap.decreaseKey(bnNodeList[to1], dis[to1]);
                        }
                    }
                }

                binEnd = System.nanoTime();

                for(int i = 0; i < n; i++){
                    dis[i] = Integer.MAX_VALUE;
                    parent[i] = -1;
                }

                fibStart = System.nanoTime();
                for(int i = 0; i < n; i++){
                    nodeList[i] = new Node();
                    nodeList[i].setIndex(i);
                    heap.insert(nodeList[i]);
                }

                dis[source] = 0;
                heap.decreaseKey(nodeList[source], 0);

                while (!heap.empty()) {

                    int v = heap.extractMin().getIndex();

                    for (int i = 0; i < adj.get(v).size(); i++) {
                        int to1 = adj.get(v).get(i).to;
                        int wi = adj.get(v).get(i).weight;

                        if (dis[v] + wi < dis[to1]) {
                            dis[to1] = dis[v] + wi;
                            parent[to1] = v;
                            heap.decreaseKey(nodeList[to1], dis[to1]);

                        }
                    }
                }

                fibEnd = System.nanoTime();

                int pathLength = 0;
                int pos = dest;

                while(pos != source){
                    pos = parent[pos];
                    pathLength++;
                }

                fw.write(pathLength + " " + dis[dest] + " " + (binEnd - binStart) + " " + (fibEnd - fibStart) + "\n");
            }
            br2.close();
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
