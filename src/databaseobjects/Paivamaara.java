package databaseobjects;

public class Paivamaara {
	private int vvvv;
	private int kk;
	private int pp;

	public Paivamaara(int paiva, int kuukausi, int vuosi){
		pp=paiva;
		kk=kuukausi;
		vvvv=vuosi;
	}
	
	public Paivamaara(String paivamaara){
		//TODO: parsi paiv�m��r� hy�dynt�en montaa sy�tett�
	}
	
	@Override
	public String toString(){
		return vvvv+"-"+kk+"-"+pp;
		
	}
	
}
