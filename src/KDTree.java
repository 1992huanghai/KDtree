import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

class TreeNode{
	int split_dim;
	double[] data;
	TreeNode left;
	TreeNode right;
}
public class KDTree {
	TreeNode root;
	private int split;
	public class datac implements Comparator{
		@Override
		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			double[] a=(double[])o1;
			double[] b=(double[])o2;
			if(a[split]>b[split])return 1;
			else if(a[split]<b[split])return -1;
			else return 0;
		}		
	}
	public TreeNode createTree(double[][] data){
		TreeNode temp=new TreeNode();
		if(data.length==1){
			temp=new TreeNode();
			temp.split_dim=-1;
			temp.data=new double[data[0].length];
			for(int i=0;i<data[0].length;i++){
				temp.data[i]=data[0][i];
			}
			temp.left=null;
			temp.right=null;
		}
		else if(data.length<1)temp=null; 
		else{
			double sum,aver,sqe,max=Integer.MIN_VALUE;
			int maxi=0;
			for(int i=0;i<data[0].length;i++){
				sum=0;
				for(int j=0;j<data.length;j++){
					sum=sum+data[j][i];
				}
				aver=sum/data.length;
				sum=0;
				for(int j=0;j<data.length;j++){
					sum=sum+(data[j][i]-aver)*(data[j][i]-aver);
				}
				sqe=sum/data.length;
				if(sqe>max){
					max=sqe;
					maxi=i;
				}
			}
			this.split=maxi;
			Arrays.sort(data,new datac());
			temp=new TreeNode();
			temp.split_dim=split;
			temp.data=new double[data[0].length];
			int mid=data.length/2;
			for(int i=0;i<data[0].length;i++){
				temp.data[i]=data[mid][i];
			}
			double [][]data1=new double[mid][data[0].length];
			for(int i=0;i<mid;i++){
				for(int j=0;j<data[0].length;j++){
					data1[i][j]=data[i][j];
				}
			}
			double [][]data2=new double[data.length-mid-1][data[0].length];
			for(int i=0;i<data.length-mid-1;i++){
				for(int j=0;j<data[0].length;j++){
					data2[i][j]=data[mid+1+i][j];
				}
			}
			temp.left=createTree(data1);
			temp.right=createTree(data2);
		}
		return temp;
	}
	public KDTree(){
		root=null;
	}
	public KDTree(double[][] data){
		root=createTree(data);
	}
	public double[] query(double[] query){
		if(root==null)return null;
		double minDist=Integer.MAX_VALUE;
		TreeNode t=root;
		TreeNode back;
		TreeNode nearest=null;
		Stack<TreeNode> s=new Stack<TreeNode>();
		while(t!=null){
			s.push(t);
			
			//System.out.println(t.data[0]+" "+t.data[1]+" "+t.split_dim);
			
			if(t.split_dim==-1)break;
			if(query[t.split_dim]<=t.data[t.split_dim]){
				t=t.left;
			}
			else t=t.right;
		}
		nearest=s.pop();
		
		//System.out.println(nearest.data[0]+" "+nearest.data[1]);
		
		minDist=dist(query,nearest.data);
		while(!s.empty()){
			back=s.pop();
			if(dist(back.data,query)<minDist){
				nearest=back;
				minDist=dist(nearest.data,query);
			}
			
			if(back.split_dim!=-1){
				if(Math.abs(query[back.split_dim]-back.data[back.split_dim])<minDist){
					if(query[back.split_dim]<=back.data[back.split_dim])t=back.right;
					else t=back.left;
					if(t!=null)s.push(t);	
				}
				while(t!=null){
					s.push(t);
					if(t.split_dim==-1)break;
					if(query[t.split_dim]<=t.data[t.split_dim]){
						t=t.left;
					}
					else t=t.right;
				}
			}
		}
		return nearest.data;
	}
	public double dist(double[] a,double[] b){
		double sum=0;
		for(int i=0;i<a.length;i++){
			sum+=Math.pow((b[i]-a[i]),2);
		}
		return Math.sqrt(sum);
	}
	public static void main(String args[]){
		double [][]data={{2.0,3.0,1.0},{5.0,4.0,2.0},{9.0,6.0,3.0},{4.0,7.0,3.0},{8.0,1.0,2.0},{7.0,2.0,1.0}};
		KDTree kd=new KDTree(data);
		double[] result=kd.query(new double[]{4.1,7.1,2.9});
		System.out.println(result[0]+" "+result[1]+" "+result[2]);
	}
}
