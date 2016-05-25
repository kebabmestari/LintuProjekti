package databaseobjects;

import databaseconnection.DB_connection;

public class Lintuhavainto implements Havainto, Json {
    /**
     * Havaitun linnun id
     */
    private int lintuid;
    /**
     * Havainnointikunta
     */
    private Kunta paikka;
    /**
     * Havainnon id
     */
    private int id;
    /**
     * Havaitsijan id
     */
    private final int havaitsija;
    /**
     * Havainnointipvm
     */
    private Paivamaara pvm;
    /**
     * Onko ekopinna?
     * (Eko=lintu havaittu ilman moottorikulkuneuvon apua)
     */
    private boolean eko;
    /**
     * Onko sopndepinna
     * (Sponde=spontaani, ei bongattu eli itse loydetty)
     */
    private boolean sponde;

    /**
     * Luo havainnon annetuilla arvoilla.
     * Eko ja sponde = false
     * @param lintuid
     * @param paikka
     * @param havaitsija
     * @param pvm 
     */
    public Lintuhavainto(int lintuid, String paikka, int havaitsija, Paivamaara pvm) {
        this.lintuid = lintuid;
        this.paikka = new Kunta(paikka);
        this.havaitsija = havaitsija;
        this.pvm=pvm;
        eko = sponde = false;
    }

    /**
     * Luo havainnon annetuilla arvoilla.
     * Eko ja sponde = false
     * @param lintuid
     * @param paikka
     * @param paivamaara
     * @param id
     * @param havaitsija 
     */
    public Lintuhavainto(int lintuid, String paikka, Paivamaara paivamaara, int id, int havaitsija) {
        this.lintuid = lintuid;
        this.paikka = new Kunta(paikka);
        this.pvm = paivamaara;
        this.id = id;
        this.havaitsija = havaitsija;
        eko = sponde = false;
    }

    /**
     * Luo havainnon annetuilla parametreilla
     * @param lintu
     * @param paikka
     * @param pvm
     * @param havaitsija
     * @param eko
     * @param sponde
     * @param con 
     */
    public Lintuhavainto(String lintu, String paikka, Paivamaara pvm,
            int havaitsija, Boolean eko, Boolean sponde, DB_connection con) {
        this.lintuid = con.searchBirdId(lintu);
        this.paikka = new Kunta(paikka);
        this.pvm=pvm;
        this.havaitsija = havaitsija;
        this.eko = eko;
        this.sponde = sponde;
    }

    /**
     * Luo havainnon tietokannasta kaikilla arvoilla
     * @param lintuid
     * @param paikka
     * @param paivamaara
     * @param id
     * @param havaitsija
     * @param eko
     * @param sponde 
     */
    public Lintuhavainto(int lintuid, String paikka, Paivamaara paivamaara, int id, int havaitsija, boolean eko, boolean sponde) {
        this.lintuid = lintuid;
        this.paikka = new Kunta(paikka);
        this.id = id;
        this.havaitsija = havaitsija;
        this.eko = eko;
        this.sponde = sponde;
        this.pvm = paivamaara;
    }

    /**
     * Palauttaa linnun id:n
     * @return lintuid
     */
    public int getLintuId() {
        return lintuid;
    }

    /**
     * Asettaa uuden linnun id:n
     * @param lintuid 
     */
    public void setLintuId(int lintuid) {
        this.lintuid = lintuid;
    }

    /**
     * Palauttaa kunnan nimen
     * @return paikka
     */
    public String getPaikka() {
        return paikka.getNimi();
    }

    /**
     * Asettaa uuden havaintopaikan
     * @param paikka 
     */
    public void setPaikka(String paikka) {
        this.paikka = new Kunta(paikka);
    }

    /**
     * Palauttaa havainnon id:n
     * @return 
     */
    public int getId() {
        return id;
    }

    /**
     * Asettaa havainnon id:n
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Palauttaa havaintopmv:n
     * @return pvm
     */
    public Paivamaara getPvm() {
        return pvm;
    }

    /**
     * Asettaa uuden havaintopvm
     * @param pvm 
     */
    public void setPvm(Paivamaara pvm) {
        this.pvm = pvm;
    }

    /**
     * Palauttaa havaitsijan id:n
     * @return havaitsijan id
     */
    public int getHavaitsija() {
        return havaitsija;
    }

    /**
     * Palauttaa onko havainto ekopinna
     * @return 
     */
    public boolean isEko() {
        return eko;
    }

    /**
     * Asettaa ekopinnan parametrina annetuksi
     * @param eko 
     */
    public void setEko(boolean eko) {
        this.eko = eko;
    }

    /**
     * Palauttaa onko spontaani havainto
     * @return 
     */
    public boolean isSponde() {
        return sponde;
    }

    /**
     * Asettaa spondepinnan parametrina annetuksi
     * @param sponde 
     */
    public void setSponde(boolean sponde) {
        this.sponde = sponde;
    }

    /**
     * Palauttaa olion tiedot SQL-muodossa
     * @return muotoa ('lintuid','paikka','pvm','havaitsija','eko','sponde')
     */
    @Override
    public String toInsertableString() {
        return "('" + lintuid + "','" + paikka + "','" + pvm.toString() + "','" + havaitsija + "','" + (eko ? 1 : 0) + "','" + (sponde ? 1 : 0) + "')";
    }

    /**
     * Palauttaa taulun nimen ja attribuuttilistan
     * @return lintuhavainto(lintuid,paikka,paivamaara,havaitsija,eko,sponde)
     */
    @Override
    public String toInsertHeader() {
        return "lintuhavainto(lintuid,paikka,paivamaara,havaitsija,eko,sponde)";
    }

    /**
     * Palauttaa yksiselitteisesti havainnon identifioivan attribuuttilistan arvoineen
     * @return esim lintuid='1' AND paivamaara='2001-1-1' AND havaitsija='1'
     */
    @Override
    public String getUniqueAttributesWithValues() {
        return "lintuid='" + lintuid + "' AND paivamaara='" + pvm.toString() + "' AND havaitsija='" + havaitsija + "'";
    }

    /**
     * Palauttaa taulun nimen
     * @return lintuhavainto
     */
    @Override
    public String getTable() {
        return "lintuhavainto";
    }

    /**
     * Palauttaa listan attribuuteista ja arvoista,
     * jotka voidaan paivittaa tietokantaan
     * @return esim. paikka='Turku', eko='1', sponde='1'
     */
    @Override
    public String getAllUpdatableAttributesWithValues() {
        return "paikka='" + paikka + "', eko='" + (eko ? 1 : 0) + "', sponde='" + (sponde ? 1 : 0) + "'";
    }

    /**
     * Palauttaa havainnoon loppukayttajan tarvimassa muodossa
     * @return JSON
     */
    @Override
    public String toJSON(DB_connection connection) {
        return "{\n"
                + "\"id\":\"" + id + "\",\n"
                + "\"lintu\":\"" + connection.getBirdNameById(lintuid) + "\",\n"
                + "\"paikka\":\"" + paikka + "\",\n"
                + "\"paivamaara\":\"" + pvm.toJSON() + "\",\n"
                + "\"eko\":\"" + eko + "\",\n"
                + "\"sponde\":\"" + sponde + "\"\n"
                + "}";
    }
}
