import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class BinaryHeapNode{
    int key;
    int value;
    int index;

    BinaryHeapNode(){
        key = -1;
        value = -1;
        index = -1;
    }

    BinaryHeapNode(int key, int value, int index){
        this.key = key;
        this.value = value;
        this.index = index;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}


public class BinaryHeap {
    BinaryHeapNode[] bnHeap;
    int maxSize;
    int num;

    BinaryHeap(){
        maxSize = 100000;
        bnHeap = new BinaryHeapNode[maxSize];
        num = 0;
    }

    BinaryHeap(BinaryHeapNode[] bnHeap, int maxSize, int num){
        this.bnHeap = bnHeap;
        this.maxSize = maxSize;
        this.num = num;
    }

    public int heapSize(){
        return num;
    }

    public int leftChild(int k){
        assert k < num/2 : "No left child";
        return 2 * k + 1;
    }

    public int rightChild(int k){
        assert k < (num - 1) / 2 : "No right child";
        return k * 2 + 2;
    }

    public int parent(int k){
        assert k > 0 : "No parent";
        return (k - 1) / 2;
    }

    public boolean isLeaf(int k){
        return (k >= num / 2 ) && (k < num);
    }

    public void shiftDown(int k){
        while (!isLeaf(k)){
            int l = leftChild(k);
            int r = rightChild(k);
            if(r < num && bnHeap[r].key < bnHeap[l].key)
                l = r;
            if (bnHeap[k].key < bnHeap[l].key)
                return;
            BinaryHeapNode tmp = bnHeap[k];
            bnHeap[k] = bnHeap[l];
            bnHeap[l] = tmp;
            bnHeap[l].index = l;
            bnHeap[k].index = k;
            k = l;
        }
    }

    public void buildHeap(){
        for(int i = num / 2 - 1; i >= 0; i--){
            shiftDown(i);
        }
    }

    public void insert(BinaryHeapNode x){
        assert num < maxSize : "Cannot insert more";
        int cur = num++;
        bnHeap[cur] = x;
        bnHeap[cur].index = cur;
        while(cur != 0 && bnHeap[cur].key < bnHeap[parent(cur)].key){
            BinaryHeapNode tmp = bnHeap[cur];
            bnHeap[cur] = bnHeap[parent(cur)];
            bnHeap[parent(cur)] = tmp;
            bnHeap[cur].index = cur;
            bnHeap[parent(cur)].index = parent(cur);
            cur = parent(cur);
        }
    }

    public boolean empty(){
        return num == 0;
    }

    public BinaryHeapNode findMin(){
        if(num == 0)
           return null;
        return bnHeap[0];
    }

    public BinaryHeapNode extractMin(){
        assert num > 0 : "Remove not possible";
        BinaryHeapNode tmp = bnHeap[0];
        bnHeap[0] = bnHeap[num - 1];
        bnHeap[num - 1] = tmp;
        bnHeap[num - 1].index = num - 1;
        bnHeap[0].index = 0;
        num--;
        if(num != 0)
           shiftDown(0);
        return bnHeap[num];
    }

    public void decreaseKey(BinaryHeapNode x, int k){
        x.key = k;

        int cur = x.index;
        while(cur != 0 && bnHeap[cur].key < bnHeap[parent(cur)].key){
            BinaryHeapNode tmp = bnHeap[cur];
            bnHeap[cur] = bnHeap[parent(cur)];
            bnHeap[parent(cur)] = tmp;
            bnHeap[cur].index = cur;
            bnHeap[parent(cur)].index = parent(cur);
            cur = parent(cur);
        }
    }

    public void delete(BinaryHeapNode x){
        decreaseKey(x, Integer.MIN_VALUE);
        BinaryHeapNode tmp = extractMin();
    }

    public static void main(String[] args) {

        Scanner scn = new Scanner(System.in);
        int n = scn.nextInt();
        int m = scn.nextInt();
        int x, y, w;
        List<List<pair>> adj = new ArrayList<List<pair> >();
        for(int i = 0; i < n; i++){
            adj.add(new ArrayList<pair>());
        }
        for(int i = 0; i < m; i++){
            x = scn.nextInt();
            y = scn.nextInt();
            w = scn.nextInt();
            adj.get(x).add(new pair(y, w));
            adj.get(y).add(new pair(x, w));
        }
        int source = scn.nextInt();
        int dest = scn.nextInt();
        /*
        for(int i = 0; i < n; i++){
            for(int j = 0; j < adj.get(i).size(); j++){
                System.out.print(adj.get(i).get(j).to + " ");
            }
            System.out.println();
        }
         */
        BinaryHeapNode[] bnNodeList = new BinaryHeapNode[n];
        int[] dis = new int[n];
        int[] parent = new int[n];
        BinaryHeap binaryHeap = new BinaryHeap();
        for(int i = 0; i < n; i++){
            dis[i] = Integer.MAX_VALUE;
            parent[i] = -1;
            bnNodeList[i] = new BinaryHeapNode(Integer.MAX_VALUE, i, i);
            //nodeList[i].setIndex(i);
            binaryHeap.insert(bnNodeList[i]);
        }
        dis[source] = 0;
        //heap.insert(nodeList[source]);
        binaryHeap.decreaseKey(bnNodeList[source], 0);

        while(!binaryHeap.empty()){
            //System.out.println(heap.size());
            //System.out.println(heap.findMin());
            int v = binaryHeap.extractMin().value;
            //System.out.println(heap.size() + " " + v);
            //System.out.println("Hi");
            //heap.display();

            for(int i = 0; i < adj.get(v).size(); i++){
                int to1 = adj.get(v).get(i).to;
                int wi = adj.get(v).get(i).weight;

                if(dis[v] + wi < dis[to1]){
                    dis[to1] = dis[v] + wi;
                    parent[to1] = v;
                    binaryHeap.decreaseKey(bnNodeList[to1], dis[to1]);
                    //System.out.println("Hi");
                    //System.out.println("After decreasing Index: " + to1);
                    //heap.display();
                    //System.out.println("Decrease sesh");
                }
            }
        }
        System.out.println(dis[dest]);
        /*
        FibHeap heap = new FibHeap();
        heap.insert(5);
        heap.insert(8);
        //heap.display();
        //System.out.println("Hi");
        heap.insert(9);
        heap.insert(6);
        heap.insert(7);
        heap.insert(2);
        heap.insert(3);
        //heap.display();
        System.out.println(heap.extractMin().getKey());
        //heap.display();
        System.out.println(heap.findMin());
        System.out.println(heap.extractMin().getKey());
        System.out.println(heap.extractMin().getKey());
        System.out.println(heap.extractMin().getKey());
        System.out.println(heap.extractMin().getKey());
        //System.out.println(heap.totalNodes);
        */
    }

}
