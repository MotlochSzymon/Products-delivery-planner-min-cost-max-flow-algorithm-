package inputElements;

public class VaccineProducer {

    private int producentId;
    private String producentName;
    private int dayilyProduction;

    public VaccineProducer(int producentId, String producentName, int dayilyProduction) {
        this.producentId = producentId;
        this.producentName = producentName;
        this.dayilyProduction = dayilyProduction;
    }

    public int getProducerId() {
        return this.producentId;
    }

    public int getDayilyProduction() {
        return this.dayilyProduction;
    }

    public String getProducerName() {
        return this.producentName;
    }

}
