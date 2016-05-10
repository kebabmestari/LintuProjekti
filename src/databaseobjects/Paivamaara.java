package databaseobjects;

public class Paivamaara {
	private int vvvv;
	private int kk;
	private int pp;

	/**
	 * 
	 * @param paiva 1-31
	 * @param kuukausi 1-12
	 * @param vuosi 1900-3000
	 * @throws Exception
	 */
	public Paivamaara(int paiva, int kuukausi, int vuosi) throws Exception{
		pp=paiva;
		kk=kuukausi;
		vvvv=vuosi;
		if(!isValid()){
			throw new Exception("Virheellinen p‰iv‰m‰‰r‰, meni yli rajojen");
		}
	}
	
	/**
	 * Muuttaa merkkijonon p‰iv‰m‰‰r‰ksi, mik‰li p‰iv‰m‰‰r‰ on kirjoitettu j‰rjestyksess‰
	 * p‰iv‰, kuukausi, vuosi tai vuosi, kuukausi, p‰iv‰ ja erotinmerkkin‰ piste tai viiva.
	 * Lis‰ksi alle kymmenen voi olla joko 02 tai 2 muodossa.
	 * Mik‰li annettu merkijono ei ole oikea paiv‰m‰‰r‰, heitet‰‰n poikkeus.
	 * @param paivamaara muotoa 2.2.2002 tai 02.02.2002 tai 2002.2.2 tai 2-2-2002 tai 2002-02-02
	 * @throws Exception virheellinen p‰iv‰m‰‰r‰
	 */
	public Paivamaara(String paivamaara) throws Exception{
		String[] pvm=null;
		if(paivamaara.contains(".")){
			pvm=paivamaara.split("\\.");
		}else if(paivamaara.contains("-")){
			pvm=paivamaara.split("-");
		}else{
			throw new Exception("Virheellinen p‰iv‰m‰‰r‰, k‰yt‰ erotinmerkkin‰ . tai -");
		}
		try{
			if(pvm.length != 3){
				printStringArray(pvm);
				throw new Exception("Virheellinen p‰iv‰m‰‰r‰, v‰‰r‰ m‰‰r‰ numeroita");
			}
			pp=Integer.parseInt(pvm[0]);
			kk=Integer.parseInt(pvm[1]);
			vvvv=Integer.parseInt(pvm[2]);
			if(!isValid()){
				int aupuvuosi=vvvv;
				vvvv=pp;
				pp=aupuvuosi;
				if(!isValid()){
					printStringArray(pvm);
					throw new Exception("Virheellinen p‰iv‰m‰‰r‰, lukualue meni yli sallittujen rajojen");
				}
			}
		}catch (NumberFormatException e){
			throw new Exception("Virheellinen p‰iv‰m‰‰r‰ sis‰lsi v‰‰ri‰ merkkej‰");
		}
	}
	
	private void printStringArray(String[] a){
		System.out.println("[");
		for(int i=0;i<a.length;i++){
			System.out.print(a[i]+", ");
		}
		System.out.println("]");
	}
	
	/**
	 * Tarkastaa, ett‰ p‰iv‰m‰‰r‰ on validi
	 * @return onko validi
	 */
	private boolean isValid(){
		if(pp>0 && pp<29 && kk>0 && kk<13 && vvvv>1899 && vvvv<3001){
			return true;
		}else if(pp==29){
			if(kk==2){
				if (vvvv%4==0){
					return true; //karkausvuosi
				}else {
					return false; //ei karkausvuosi
				}
			}else{
				return true; //muu kuin helmikuu
			}
		}else if(pp==30 && kk!=2){
			return true;
		}else if(pp==31){
			if (kk==2 || kk==4 || kk==6 || kk==9 || kk==11){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	
	public int getVuosi(){
		return vvvv;
	}
	
	@Override
	public String toString(){
		return vvvv+"-"+kk+"-"+pp;
		
	}
	
}
