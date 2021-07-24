package fileElements;

public class VaccineProducent {

    private int producentId;
    private String producentName;
    private int dayilyProduction;

    public VaccineProducent(int producentId, String producentName, int dayilyProduction) {
        this.producentId = producentId;
        this.producentName = producentName;
        this.dayilyProduction = dayilyProduction;
    }

    public int getProducentId() {
        return this.producentId;
    }

    public int getDayilyProduction() {
        return this.dayilyProduction;
    }
}
