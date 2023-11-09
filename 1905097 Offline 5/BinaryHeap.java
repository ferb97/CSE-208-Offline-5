import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class BinaryHeapNode{
    private int key;
    private int value;
    private int index;

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

    BinaryHeap(int maxSize){
        this.maxSize = maxSize;
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

    private int leftChild(int k){
        assert k < num / 2 : "No left child";
        return 2 * k + 1;
    }

    private int rightChild(int k){
        assert k < (num - 1) / 2 : "No right child";
        return k * 2 + 2;
    }

    private int parent(int k){
        assert k > 0 : "No parent";
        return (k - 1) / 2;
    }

    private boolean isLeaf(int k){
        return (k >= num / 2) && (k < num);
    }

    private void shiftDown(int k){
        while (!isLeaf(k)){
            int l = leftChild(k);
            int r = rightChild(k);
            if(r < num && bnHeap[r].getKey() < bnHeap[l].getKey())
                l = r;
            if (bnHeap[k].getKey() < bnHeap[l].getKey())
                return;
            BinaryHeapNode tmp = bnHeap[k];
            bnHeap[k] = bnHeap[l];
            bnHeap[l] = tmp;
            bnHeap[l].setIndex(l);
            bnHeap[k].setIndex(k);
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
        bnHeap[cur].setIndex(cur);
        while(cur != 0 && bnHeap[cur].getKey() < bnHeap[parent(cur)].getKey()){
            BinaryHeapNode tmp = bnHeap[cur];
            bnHeap[cur] = bnHeap[parent(cur)];
            bnHeap[parent(cur)] = tmp;
            bnHeap[cur].setIndex(cur);
            bnHeap[parent(cur)].setIndex(parent(cur));
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
        bnHeap[num - 1].setIndex(num - 1);
        bnHeap[0].setIndex(0);
        num--;
        if(num != 0)
           shiftDown(0);
        return bnHeap[num];
    }

    public void decreaseKey(BinaryHeapNode x, int k){
        x.setKey(k);

        int cur = x.getIndex();
        while(cur != 0 && bnHeap[cur].getKey() < bnHeap[parent(cur)].getKey()){
            BinaryHeapNode tmp = bnHeap[cur];
            bnHeap[cur] = bnHeap[parent(cur)];
            bnHeap[parent(cur)] = tmp;
            bnHeap[cur].setIndex(cur);
            bnHeap[parent(cur)].setIndex(parent(cur));
            cur = parent(cur);
        }
    }

    public void delete(BinaryHeapNode x){
        decreaseKey(x, Integer.MIN_VALUE);
        BinaryHeapNode tmp = extractMin();
    }

}
